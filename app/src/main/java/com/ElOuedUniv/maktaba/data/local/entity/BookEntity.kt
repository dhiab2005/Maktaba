// data/local/entity/BookEntity.kt
package com.ElOuedUniv.maktaba.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ReadingStatus {
    TO_READ,
    READING,
    COMPLETED
}

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
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
)