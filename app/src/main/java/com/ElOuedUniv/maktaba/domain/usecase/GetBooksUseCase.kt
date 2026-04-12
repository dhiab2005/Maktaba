// domain/usecase/GetBooksUseCase.kt
package com.ElOuedUniv.maktaba.domain.usecase

import com.ElOuedUniv.maktaba.data.model.Book
import com.ElOuedUniv.maktaba.data.repository.BookRepository
import kotlinx.coroutines.flow.Flow

class GetBooksUseCase(private val repository: BookRepository) {

    // جلب كل الكتب
    fun getAllBooks(): Flow<List<Book>> =
        repository.getAllBooks()

    // جلب المفضلة
    fun getFavoriteBooks(): Flow<List<Book>> =
        repository.getFavoriteBooks()

    // البحث
    fun searchBooks(query: String): Flow<List<Book>> =
        repository.searchBooks(query)
}