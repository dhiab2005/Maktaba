// presentation/viewmodel/BookViewModel.kt
package com.ElOuedUniv.maktaba.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ElOuedUniv.maktaba.data.local.entity.ReadingStatus
import com.ElOuedUniv.maktaba.data.model.Book
import com.ElOuedUniv.maktaba.data.repository.BookRepository
import com.ElOuedUniv.maktaba.presentation.state.BookFilter
import com.ElOuedUniv.maktaba.presentation.state.BookFormUiState
import com.ElOuedUniv.maktaba.presentation.state.BookListUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BookViewModel(private val repository: BookRepository) : ViewModel() {

    // ── UI State ─────────────────────────────────
    private val _listState = MutableStateFlow(BookListUiState())
    val listState: StateFlow<BookListUiState> = _listState.asStateFlow()

    private val _formState = MutableStateFlow(BookFormUiState())
    val formState: StateFlow<BookFormUiState> = _formState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    private val _selectedFilter = MutableStateFlow(BookFilter.ALL)

    init {
        loadBooks()
        loadStats()
    }

    // ── تحميل الكتب ──────────────────────────────
    private fun loadBooks() {
        viewModelScope.launch {
            _listState.update { it.copy(isLoading = true) }
            combine(_searchQuery, _selectedFilter) { query, filter ->
                Pair(query, filter)
            }.flatMapLatest { (query, filter) ->
                when {
                    query.isNotEmpty() -> repository.searchBooks(query)
                    filter == BookFilter.FAVORITES -> repository.getFavoriteBooks()
                    filter == BookFilter.TO_READ ->
                        repository.getBooksByStatus(ReadingStatus.TO_READ)
                    filter == BookFilter.READING ->
                        repository.getBooksByStatus(ReadingStatus.READING)
                    filter == BookFilter.COMPLETED ->
                        repository.getBooksByStatus(ReadingStatus.COMPLETED)
                    else -> repository.getAllBooks()
                }
            }.catch { e ->
                _listState.update { it.copy(isLoading = false, error = e.message) }
            }.collect { books ->
                _listState.update {
                    it.copy(books = books, isLoading = false, error = null)
                }
            }
        }
    }

    // ── تحميل الإحصائيات ─────────────────────────
    private fun loadStats() {
        viewModelScope.launch {
            repository.getTotalBooksCount().collect { count ->
                _listState.update { it.copy(totalBooks = count) }
            }
        }
        viewModelScope.launch {
            repository.getFavoritesCount().collect { count ->
                _listState.update { it.copy(favoritesCount = count) }
            }
        }
    }

    // ── البحث ────────────────────────────────────
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        _listState.update { it.copy(searchQuery = query) }
    }

    // ── الفلتر ───────────────────────────────────
    fun onFilterChange(filter: BookFilter) {
        _selectedFilter.value = filter
        _listState.update { it.copy(selectedFilter = filter) }
    }

    // ── المفضلة ──────────────────────────────────
    fun toggleFavorite(book: Book) {
        viewModelScope.launch {
            repository.toggleFavorite(book.id, !book.isFavorite)
        }
    }

    // ── حذف ──────────────────────────────────────
    fun deleteBook(book: Book) {
        viewModelScope.launch {
            repository.deleteBook(book)
        }
    }

    // ── تحديث حالة القراءة ───────────────────────
    fun updateReadingStatus(bookId: Int, status: ReadingStatus) {
        viewModelScope.launch {
            repository.updateReadingStatus(bookId, status)
        }
    }

    // ── Form ─────────────────────────────────────
    fun loadBookForEdit(book: Book) {
        _formState.update {
            BookFormUiState(
                title = book.title,
                author = book.author,
                isbn = book.isbn,
                genre = book.genre,
                description = book.description,
                totalPages = if (book.totalPages > 0) book.totalPages.toString() else "",
                readingStatus = book.readingStatus
            )
        }
    }

    fun onTitleChange(value: String) =
        _formState.update { it.copy(title = value, titleError = null) }

    fun onAuthorChange(value: String) =
        _formState.update { it.copy(author = value, authorError = null) }

    fun onIsbnChange(value: String) =
        _formState.update { it.copy(isbn = value) }

    fun onGenreChange(value: String) =
        _formState.update { it.copy(genre = value) }

    fun onDescriptionChange(value: String) =
        _formState.update { it.copy(description = value) }

    fun onTotalPagesChange(value: String) =
        _formState.update { it.copy(totalPages = value) }

    fun onReadingStatusChange(status: ReadingStatus) =
        _formState.update { it.copy(readingStatus = status) }

    fun saveBook(existingBookId: Int? = null) {
        if (!validateForm()) return
        viewModelScope.launch {
            _formState.update { it.copy(isLoading = true) }
            val book = Book(
                id = existingBookId ?: 0,
                title = _formState.value.title.trim(),
                author = _formState.value.author.trim(),
                isbn = _formState.value.isbn.trim(),
                genre = _formState.value.genre.trim(),
                description = _formState.value.description.trim(),
                totalPages = _formState.value.totalPages.toIntOrNull() ?: 0,
                readingStatus = _formState.value.readingStatus
            )
            if (existingBookId != null) repository.updateBook(book)
            else repository.addBook(book)
            _formState.update { it.copy(isLoading = false, isSaved = true) }
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true
        if (_formState.value.title.isBlank()) {
            _formState.update { it.copy(titleError = "عنوان الكتاب مطلوب") }
            isValid = false
        }
        if (_formState.value.author.isBlank()) {
            _formState.update { it.copy(authorError = "اسم المؤلف مطلوب") }
            isValid = false
        }
        return isValid
    }

    fun resetForm() { _formState.value = BookFormUiState() }

    fun clearError() { _listState.update { it.copy(error = null) } }

    // ── Factory ──────────────────────────────────
    companion object {
        fun factory(repository: BookRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return BookViewModel(repository) as T
                }
            }
    }
}