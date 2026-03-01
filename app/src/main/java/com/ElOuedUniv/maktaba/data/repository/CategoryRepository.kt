package com.ElOuedUniv.maktaba.data.repository

import com.ElOuedUniv.maktaba.data.model.Category

interface CategoryRepository {

    fun getAllCategories(): List<Category>

    fun getCategoryById(id: String): Category?
}