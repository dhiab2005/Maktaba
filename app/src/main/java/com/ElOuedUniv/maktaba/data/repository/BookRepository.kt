package com.ElOuedUniv.maktaba.data.repository

import com.ElOuedUniv.maktaba.data.model.Book

/**
 * Repository for managing book data
 * This follows the Repository pattern to abstract data sources
 */
class BookRepository {

    /**
     * Completed books list (Exercise 1 + Exercise 2)
     */
    private val booksList = listOf(

        // ===== Exercise 1 : Complete information =====
        Book(
            isbn = "978-0-13-235088-4",
            title = "Clean Code",
            nbPages = 464
        ),
        Book(
            isbn = "978-0-201-61622-4",
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

        // ===== Exercise 2 : Add 5 more books =====
        Book(
            isbn = "978-0-262-03384-8",
            title = "Introduction to Algorithms",
            nbPages = 1312
        ),
        Book(
            isbn = "978-1-491-95424-9",
            title = "Kotlin Programming: The Big Nerd Ranch Guide",
            nbPages = 448
        ),
        Book(
            isbn = "978-1-449-35933-3",
            title = "Learning Android Development",
            nbPages = 504
        ),
        Book(
            isbn = "978-0-596-52068-7",
            title = "Head First Java",
            nbPages = 720
        ),
        Book(
            isbn = "978-1-59327-599-0",
            title = "Eloquent JavaScript",
            nbPages = 472
        )
    )

    /**
     * Get all books from the repository
     * @return List of all books
     */
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