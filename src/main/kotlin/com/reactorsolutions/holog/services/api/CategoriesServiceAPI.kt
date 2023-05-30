package com.reactorsolutions.holog.services.api

import com.reactorsolutions.holog.models.Category

interface CategoriesServiceAPI {
    fun getAllCategories(): Set<Category>
    fun getCategoryById(id:Long):Category
    fun createCategory(userId:String,category: Category):Category
    fun updateCategory(id: Long,category: Category):Boolean
    fun deleteCategory(id:Long):Category

}