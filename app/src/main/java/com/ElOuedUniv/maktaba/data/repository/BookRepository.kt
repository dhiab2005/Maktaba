package com.ElOuedUniv.maktaba

// ================= IMPORTS =================
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.ElOuedUniv.maktaba.BookViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// ================= MODEL =================
data class Book(
    val isbn: String,
    val title: String,
    val nbPages: Int
)

// ================= REPOSITORY =================
class BookRepository {

    private val booksList = listOf(

        // Exercise 1 (completed books)
        Book("978-0-13-235088-4","Clean Code",464),
        Book("978-0-201-61622-4","The Pragmatic Programmer",352),
        Book("978-0-201-63361-0","Design Patterns",395),
        Book("978-0-13-475759-9","Refactoring",448),
        Book("978-0-596-00712-6","Head First Design Patterns",694),

        // Exercise 2 (added books)
        Book("978-0-262-03384-8","Introduction to Algorithms",1312),
        Book("978-1-491-95424-9","Kotlin Programming",448),
        Book("978-1-449-35933-3","Learning Android Development",504),
        Book("978-0-596-52068-7","Head First Java",720),
        Book("978-1-59327-599-0","Eloquent JavaScript",472)
    )

    fun getAllBooks(): List<Book> = booksList

    fun getBookByIsbn(isbn: String): Book? {
        return booksList.find { it.isbn == isbn }
    }

    // ✅ BONUS 3 : Filter books > 400 pages
    fun getLongBooks(): List<Book> {
        return booksList.filter { it.nbPages > 400 }
    }
}

// ================= VIEWMODEL =================
class BookViewModel(
    private val repository: BookRepository = BookRepository()
) : ViewModel() {

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books

    init {
        loadBooks()
    }

    fun loadBooks() {
        _books.value = repository.getAllBooks()
    }

    fun loadLongBooks() {
        _books.value = repository.getLongBooks()
    }

    // ✅ BONUS 2 : Calculate total pages
    fun getTotalPages(): Int {
        return books.value.sumOf { it.nbPages }
    }
}

// ================= UI SCREEN =================
@Composable
fun BookListScreen(
    viewModel: BookViewModel = viewModel ()
) {

    val books by viewModel.books.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // ✅ BONUS 1 : Book Counter
        Text(text = "Total Books: ${books.size}")

        Spacer(modifier = Modifier.height(8.dp))

        // ✅ BONUS 2 : Total Pages
        Text(text = "Total Pages: ${viewModel.getTotalPages()}")

        Spacer(modifier = Modifier.height(16.dp))

        // Books List
        LazyColumn {
            items(books) { book ->
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = book.title)
                    Text(text = "ISBN: ${book.isbn}")
                    Text(text = "Pages: ${book.nbPages}")
                }
            }
        }
    }
}