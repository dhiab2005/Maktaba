package com.ElOuedUniv.maktaba.data.repository

import com.ElOuedUniv.maktaba.data.model.Book

class BookRepositoryImpl : BookRepository {

    private val booksList = listOf(
        Book(isbn = "11111", title = "Clean Code", nbPages = 10),
        Book(isbn = "", title = "The Pragmatic Programmer", nbPages = 0),
        Book(isbn = "", title = "Design Patterns", nbPages = 0),
        Book(isbn = "", title = "Refactoring", nbPages = 0),
        Book(isbn = "", title = "Head First Design Patterns", nbPages = 0)
    )

    override fun getAllBooks(): List<Book> {
        return booksList
    }

    override fun getBookByIsbn(isbn: String): Book? {
        return booksList.find { it.isbn == isbn }
    }
}