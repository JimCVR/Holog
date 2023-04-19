package com.reactorsolutions.holog.services.impl

import com.reactorsolutions.holog.exceptions.category.CategoryNotFoundException
import com.reactorsolutions.holog.models.Category
import com.reactorsolutions.holog.repositories.CategoriesRepository
import com.reactorsolutions.holog.services.api.CategoriesServiceAPI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CategoriesServiceImpl : CategoriesServiceAPI {
    @Autowired
    lateinit var categoriesRepository: CategoriesRepository

    override fun getAllCategories(): MutableList<Category> {
        return categoriesRepository.findAll() as MutableList<Category>
    }

    override fun getCategoryById(id: Long): Category {
        val category: Category? = categoriesRepository.findById(id).orElse(null)
        if (category != null)
            return category
        else
            throw CategoryNotFoundException("Category not found")

    }

    override fun createCategory(category: Category) {
        categoriesRepository.save(category)
    }

    override fun updateCategory(categoryUpdated: Category) {
        val category: Category? = categoriesRepository.findById(categoryUpdated.id).orElse(null)
        if (category != null)
            categoriesRepository.save(categoryUpdated)
        else
            throw CategoryNotFoundException("Category Not Found")
    }

    override fun deleteCategory(id: Long): Category {
        val category: Category? = categoriesRepository.findById(id).orElse(null)
        if (category != null) {
            categoriesRepository.deleteById(id)
            return category
        } else
            throw CategoryNotFoundException("Category Not Found")
    }
}