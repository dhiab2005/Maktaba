// presentation/state/BookUiState.kt
package com.ElOuedUniv.maktaba.presentation.state

import com.ElOuedUniv.maktaba.data.local.entity.ReadingStatus
import com.ElOuedUniv.maktaba.data.model.Book

data class BookListUiState(
    val books: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedFilter: BookFilter = BookFilter.ALL,
    val totalBooks: Int = 0,
    val favoritesCount: Int = 0
)

data class BookFormUiState(
    val title: String = "",
    val author: String = "",
    val isbn: String = "",
    val genre: String = "",
    val description: String = "",
    val totalPages: String = "",
    val readingStatus: ReadingStatus = ReadingStatus.TO_READ,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null,
    val titleError: String? = null,
    val authorError: String? = null
)

enum class BookFilter {
    ALL, FAVORITES, TO_READ, READING, COMPLETED
}