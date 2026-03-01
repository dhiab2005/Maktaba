package com.ElOuedUniv.maktaba.data.repository

import com.ElOuedUniv.maktaba.data.model.Category

class CategoryRepositoryImpl : CategoryRepository {

    private val categoriesList = listOf(
        Category(
            id = "1",
            name = "Programming",
            description = "Books about software development and programming languages"
        ),
        Category(
            id = "2",
            name = "History",
            description = "Books covering historical events and civilizations"
        ),
        Category(
            id = "3",
            name = "Science",
            description = "Books about physics, chemistry, biology and more"
        ),
        Category(
            id = "4",
            name = "Literature",
            description = "Novels, poetry, and literary analysis"
        )
    )

    override fun getAllCategories(): List<Category> {
        return categoriesList
    }

    override fun getCategoryById(id: String): Category? {
        return categoriesList.find { it.id == id }
    }
}
