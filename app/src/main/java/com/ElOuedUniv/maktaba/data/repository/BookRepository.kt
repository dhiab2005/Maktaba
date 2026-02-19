package com.ElOuedUniv.maktaba.data.repository
// ---------------- Imports ----------------
import androidx.compose.runtime.*
import androidx.compose.material3.Text
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
// ----------------------------------------

import com.ElOuedUniv.maktaba.data.model.Book

/**
 * Repository for managing book data
 * This follows the Repository pattern to abstract data sources
 */
class BookRepository {

    /**
     * TODO for Students (TP1 - Exercise 1):
     * Complete the book information for each book in the list below.
     * Add the following information for each book:
     * - isbn: Use a valid ISBN-13 format (e.g., "978-3-16-148410-0")
     * - nbPages: Add the actual number of pages
     *
     * Example:
     * Book(
     *     isbn = "978-0-13-468599-1",
     *     title = "Clean Code",
     *     nbPages = 464
     * )
     */

    private val booksList = listOf(
        Book(isbn = "", title = "Clean Code", nbPages = 0),
        Book(isbn = "", title = "The Pragmatic Programmer", nbPages = 0),
        Book(isbn = "", title = "Design Patterns", nbPages = 0),
        Book(isbn = "", title = "Refactoring", nbPages = 0),
        Book(isbn = "", title = "Head First Design Patterns", nbPages = 0),
        Book(isbn = "", title = "Effective Java", nbPages = 0),
        Book(isbn = "", title = "Java Concurrency in Practice", nbPages = 0),
        Book(isbn = "", title = "Spring in Action", nbPages = 0),
        Book(isbn = "", title = "Cracking the Coding Interview", nbPages = 0),
        Book(isbn = "", title = "Introduction to Algorithms", nbPages = 0)
    )
    private val booksLis = listOf(
        Book(
            isbn = "978-0-13-468599-1",
            title = "Clean Code",
            nbPages = 464
        ),
        Book(
            isbn = "978-0-13-595705-9",
            title = "The Pragmatic Programmer",
            nbPages = 352
        ),
        Book(
            isbn = "978-0-201-63361-0",
            title = "Design Patterns",
            nbPages = 395
        ),
        Book(
            isbn = "978-0-13-475759-9",
            title = "Refactoring",
            nbPages = 448
        ),
        Book(
            isbn = "978-0-596-00712-6",
            title = "Head First Design Patterns",
            nbPages = 694
        ),
        Book(
            isbn = "978-0-13-468599-2",
            title = "Effective Java",
            nbPages = 416
        ),
        Book(
            isbn = "978-0-321-34960-6",
            title = "Java Concurrency in Practice",
            nbPages = 432
        ),
        Book(
            isbn = "978-1-61729-494-5",
            title = "Spring in Action",
            nbPages = 520
        ),
        Book(
            isbn = "978-0-9847828-5-7",
            title = "Cracking the Coding Interview",
            nbPages = 706
        ),
        Book(
            isbn = "978-0-262-03384-8",
            title = "Introduction to Algorithms",
            nbPages = 1312
        )
    )

    /**
     * TODO for Students (TP1 - Exercise 2):
     * Add 5 more books to the list above.
     * Choose books related to Computer Science, Programming, or any topic you like.
     * Remember to include complete information (ISBN, title, nbPages).
     *
     * Tip: You can find ISBN numbers for books on:
     * - Google Books
     * - Amazon
     * - GoodReads
     */

    /**
     * Get all books from the repository
     * @return List of all books
     */
    private val booksList2 = listOf(
        Book(
            isbn = "978-0-13-468599-1",
            title = "Clean Code",
            nbPages = 464
        ),
        Book(
            isbn = "978-0-13-595705-9",
            title = "The Pragmatic Programmer",
            nbPages = 352
        ),
        Book(
            isbn = "978-0-201-63361-0",
            title = "Design Patterns",
            nbPages = 395
        ),
        Book(
            isbn = "978-0-13-475759-9",
            title = "Refactoring",
            nbPages = 448
        ),
        Book(
            isbn = "978-0-596-00712-6",
            title = "Head First Design Patterns",
            nbPages = 694
        ),

        // ✅ 5 NEW BOOKS (Exercise 2)

        Book(
            isbn = "978-1-491-94728-9",
            title = "Learning Python",
            nbPages = 1648
        ),
        Book(
            isbn = "978-1-59327-599-0",
            title = "Automate the Boring Stuff with Python",
            nbPages = 592
        ),
        Book(
            isbn = "978-1-492-05635-2",
            title = "Fluent Python",
            nbPages = 792
        ),
        Book(
            isbn = "978-1-491-92424-2",
            title = "Kotlin in Action",
            nbPages = 360
        ),
        Book(
            isbn = "978-1-59327-928-8",
            title = "Python Crash Course",
            nbPages = 544
        )
    )
    data class Book(
        val isbn: String,
        val title: String,
        val nbPages: Int
    )

    class BookRepository {

        private val booksList = listOf(
            Book("978-0-13-468599-1", "Clean Code", 464),
            Book("978-0-13-595705-9", "The Pragmatic Programmer", 352),
            Book("978-0-201-63361-0", "Design Patterns", 395),
            Book("978-0-13-475759-9", "Refactoring", 448),
            Book("978-0-596-00712-6", "Head First Design Patterns", 694),

            // Exercise 2 (5 books added)
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

        // search by title (Challenge)
        fun searchBooksByTitle(query: String): List<Book> {
            return booksList.filter {
                it.title.contains(query, ignoreCase = true)
            }
        }
    }
    class GetBooksUseCase(
        private val repository: BookRepository
    ) {

        // operator invoke
        operator fun invoke(): List<Book> {
            // business logic: books > 300 pages
            return repository
                .getAllBooks()
                .filter { it.nbPages > 300 }
        }
    }


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

    fun getAllBooks(): List<Book> {
        return booksList
    }

    /**
     * Get a book by ISBN
     * @param isbn The ISBN of the book to find
     * @return The book if found, null otherwise
     */
    fun getBookByIsbn(isbn: String): Book? {
        return booksList.find { it.isbn == isbn }
    }
}
