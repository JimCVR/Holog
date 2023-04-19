package com.reactorsolutions.holog.services.api

import com.reactorsolutions.holog.models.Category

interface CategoriesServiceAPI {
    fun getAllCategories():MutableList<Category>
    fun getCategoryById(id:Long):Category
    fun createCategory(category: Category)
    fun updateCategory(category: Category)
    fun deleteCategory(id:Long):Category

}