// data/repository/BookRepository.kt
package com.ElOuedUniv.maktaba.data.repository

import com.ElOuedUniv.maktaba.data.local.dao.BookDao
import com.ElOuedUniv.maktaba.data.local.di.ReadingStatus
import com.ElOuedUniv.maktaba.data.model.Book
import com.ElOuedUniv.maktaba.data.model.toBook
import com.ElOuedUniv.maktaba.data.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookRepository(private val bookDao: BookDao) {

    // ── CRUD ────────────────────────────────────
    suspend fun addBook(book: Book) =
        bookDao.insertBook(book.toEntity())

    suspend fun updateBook(book: Book) =
        bookDao.updateBook(book.toEntity())

    suspend fun deleteBook(book: Book) =
        bookDao.deleteBook(book.toEntity())

    suspend fun deleteBookById(id: Int) =
        bookDao.deleteBookById(id)

    // ── Queries ─────────────────────────────────
    fun getAllBooks(): Flow<List<Book>> =
        bookDao.getAllBooks().map { list -> list.map { it.toBook() } }

    fun getBookById(id: Int): Flow<Book?> =
        bookDao.getBookById(id).map { it?.toBook() }

    fun searchBooks(query: String): Flow<List<Book>> =
        bookDao.searchBooks(query).map { list -> list.map { it.toBook() } }

    // ── المفضلة ─────────────────────────────────
    fun getFavoriteBooks(): Flow<List<Book>> =
        bookDao.getFavoriteBooks().map { list -> list.map { it.toBook() } }

    suspend fun toggleFavorite(bookId: Int, isFavorite: Boolean) =
        bookDao.updateFavoriteStatus(bookId, isFavorite)

    // ── حالة القراءة ────────────────────────────
    fun getBooksByStatus(status: ReadingStatus): Flow<List<Book>> =
        bookDao.getBooksByStatus(status).map { list -> list.map { it.toBook() } }

    suspend fun updateReadingStatus(bookId: Int, status: ReadingStatus) =
        bookDao.updateReadingStatus(bookId, status)

    // ── الإحصائيات ──────────────────────────────
    fun getTotalBooksCount(): Flow<Int> = bookDao.getTotalBooksCount()
    fun getFavoritesCount(): Flow<Int> = bookDao.getFavoritesCount()
    fun getCountByStatus(status: ReadingStatus): Flow<Int> =
        bookDao.getCountByStatus(status)
    fun getAllGenres(): Flow<List<String>> = bookDao.getAllGenres()
}