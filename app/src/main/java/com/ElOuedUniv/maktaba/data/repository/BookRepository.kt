package com.ElOuedUniv.maktaba.data.repository

import com.ElOuedUniv.maktaba.data.model.Book

/**
 * Repository for managing book data
 * This follows the Repository pattern to abstract data sources
 */
class BookRepository {

    /**
     *
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

        // ----- Previous 5 books -----

        Book(
            isbn = "978-0-262-03384-8",
            title = "Introduction to Algorithms",
            nbPages = 1312
        ),

        Book(
            isbn = "978-0-596-00712-6",
            title = "Head First Design Patterns",
            nbPages = 694
        ),

        Book(
            isbn = "978-1-449-37464-8",
            title = "Learning SQL",
            nbPages = 338
        ),

        Book(
            isbn = "978-1-491-92424-9",
            title = "Android Programming with Kotlin",
            nbPages = 450
        ),

        Book(
            isbn = "978-1-59327-599-0",
            title = "Automate the Boring Stuff with Python",
            nbPages = 504
        )
    )

    private val booksLit = listOf(
        Book(isbn = "", title = "Clean Code", nbPages = 0),
        Book(isbn = "", title = "The Pragmatic Programmer", nbPages = 0),
        Book(isbn = "", title = "Design Patterns", nbPages = 0),
        Book(isbn = "", title = "Refactoring", nbPages = 0),
        Book(isbn = "", title = "Head First Design Patterns", nbPages = 0)

    )

    /**
     *
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
}
