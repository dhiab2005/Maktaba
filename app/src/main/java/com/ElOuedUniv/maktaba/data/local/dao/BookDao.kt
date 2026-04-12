// data/local/dao/BookDao.kt
package com.ElOuedUniv.maktaba.data.local.dao

import androidx.room.*
import com.ElOuedUniv.maktaba.data.local.entity.BookEntity
import com.ElOuedUniv.maktaba.data.local.entity.ReadingStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    // ── إضافة ──────────────────────────────────
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity)

    // ── تعديل ──────────────────────────────────
    @Update
    suspend fun updateBook(book: BookEntity)

    // ── حذف ────────────────────────────────────
    @Delete
    suspend fun deleteBook(book: BookEntity)

    @Query("DELETE FROM books WHERE id = :bookId")
    suspend fun deleteBookById(bookId: Int)

    // ── جلب كل الكتب ───────────────────────────
    @Query("SELECT * FROM books ORDER BY addedDate DESC")
    fun getAllBooks(): Flow<List<BookEntity>>

    // ── جلب كتاب واحد ──────────────────────────
    @Query("SELECT * FROM books WHERE id = :bookId")
    fun getBookById(bookId: Int): Flow<BookEntity?>

    // ── البحث ───────────────────────────────────
    @Query("""
        SELECT * FROM books 
        WHERE title LIKE '%' || :query || '%' 
        OR author LIKE '%' || :query || '%'
        ORDER BY addedDate DESC
    """)
    fun searchBooks(query: String): Flow<List<BookEntity>>

    // ── المفضلة ─────────────────────────────────
    @Query("SELECT * FROM books WHERE isFavorite = 1 ORDER BY addedDate DESC")
    fun getFavoriteBooks(): Flow<List<BookEntity>>

    @Query("UPDATE books SET isFavorite = :isFavorite WHERE id = :bookId")
    suspend fun updateFavoriteStatus(bookId: Int, isFavorite: Boolean)

    // ── حالة القراءة ────────────────────────────
    @Query("SELECT * FROM books WHERE readingStatus = :status ORDER BY addedDate DESC")
    fun getBooksByStatus(status: ReadingStatus): Flow<List<BookEntity>>

    @Query("UPDATE books SET readingStatus = :status WHERE id = :bookId")
    suspend fun updateReadingStatus(bookId: Int, status: ReadingStatus)

    // ── الإحصائيات ──────────────────────────────
    @Query("SELECT COUNT(*) FROM books")
    fun getTotalBooksCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM books WHERE isFavorite = 1")
    fun getFavoritesCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM books WHERE readingStatus = :status")
    fun getCountByStatus(status: ReadingStatus): Flow<Int>

    @Query("SELECT DISTINCT genre FROM books WHERE genre != ''")
    fun getAllGenres(): Flow<List<String>>
}