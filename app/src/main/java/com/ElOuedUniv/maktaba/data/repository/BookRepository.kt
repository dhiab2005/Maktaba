package com.ElOuedUniv.maktaba

// ================= IMPORTS =================

import androidx.compose.runtime.*
import androidx.compose.material3.Text
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ================= DATA CLASS =================

data class Book(
    val isbn: String,
    val title: String,
    val nbPages: Int
)

// ================= REPOSITORY =================

class BookRepository {

    private val booksList = listOf(

        // Exercise 1 (completed books)
        Book("978-0-13-468599-1", "Clean Code", 464),
        Book("978-0-13-595705-9", "The Pragmatic Programmer", 352),
        Book("978-0-201-63361-0", "Design Patterns", 395),
        Book("978-0-13-475759-9", "Refactoring", 448),
        Book("978-0-596-00712-6", "Head First Design Patterns", 694),

        // Exercise 2 (5 added books)
        Book("978-1-491-94728-9", "Learning Python", 1648),
        Book("978-1-59327-599-0", "Automate the Boring Stuff with Python", 592),
        Book("978-1-492-05635-2", "Fluent Python", 792),
        Book("978-1-491-92424-2", "Kotlin in Action", 360),
        Book("978-1-59327-928-8", "Python Crash Course", 544)
    )

    fun getAllBooks(): List<Book> = booksList

       fun getBookByIsbn(isbn: String): Book? {
        return booksList.find { it.isbn == isbn }
    }


    fun searchBooksByTitle(query: String): List<Book> {
        return booksList.filter {
            it.title.contains(query, ignoreCase = true)
        }
    }
}

// ================= USE CASE =================

class GetBooksUseCase(
    private val repository: BookRepository
) {
    operator fun invoke(): List<Book> {
        // business logic: only books > 300 pages
        return repository
            .getAllBooks()
            .filter { it.nbPages > 300 }
    }
}

// ================= VIEWMODEL =================

class BookViewModel(
    private val getBooksUseCase: GetBooksUseCase
) : ViewModel() {

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books

    init {
        loadBooks()
    }

    private fun loadBooks() {
        viewModelScope.launch {
            _books.value = getBooksUseCase()
        }
    }
}

// ================= UI (JETPACK COMPOSE) =================

@Composable
fun BookListScreen(viewModel: BookViewModel) {

    val books by viewModel.books.collectAsState()

    if (books.isEmpty()) {
        Text("No books available")
    } else {
        LazyColumn {
            items(books) { book ->
                Text("${book.title} - ${book.nbPages} pages")
            }
        }
    }
}

// ================= SIMPLE TEST (OPTIONAL) =================

// يمكنك استعماله داخل Activity للتجربة
fun createViewModel(): BookViewModel {
    val repository = BookRepository()
    val useCase = GetBooksUseCase(repository)
    return BookViewModel(useCase)
}