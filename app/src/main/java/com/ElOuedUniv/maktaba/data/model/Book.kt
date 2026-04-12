// data/model/Book.kt
package com.ElOuedUniv.maktaba.data.model

import com.ElOuedUniv.maktaba.data.local.entity.BookEntity
import com.ElOuedUniv.maktaba.data.local.entity.ReadingStatus

data class Book(
    val id: Int = 0,
    val title: String,
    val author: String,
    val isbn: String = "",
    val genre: String = "",
    val description: String = "",
    val coverImageUrl: String = "",
    val isFavorite: Boolean = false,
    val readingStatus: ReadingStatus = ReadingStatus.TO_READ,
    val totalPages: Int = 0,
    val currentPage: Int = 0,
    val addedDate: Long = System.currentTimeMillis()
) {
    // نسبة القراءة
    val readingProgress: Float
        get() = if (totalPages > 0) currentPage.toFloat() / totalPages else 0f
}

// ── Extensions للتحويل بين Entity و Model ───────
fun BookEntity.toBook() = Book(
    id = id,
    title = title,
    author = author,
    isbn = isbn,
    genre = genre,
    description = description,
    coverImageUrl = coverImageUrl,
    isFavorite = isFavorite,
    readingStatus = readingStatus,
    totalPages = totalPages,
    currentPage = currentPage,
    addedDate = addedDate
)

fun Book.toEntity() = BookEntity(
    id = id,
    title = title,
    author = author,
    isbn = isbn,
    genre = genre,
    description = description,
    coverImageUrl = coverImageUrl,
    isFavorite = isFavorite,
    readingStatus = readingStatus,
    totalPages = totalPages,
    currentPage = currentPage,
    addedDate = addedDate
)