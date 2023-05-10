package com.reactorsolutions.holog.services.impl

import com.reactorsolutions.holog.exceptions.category.CategoryNotFoundException
import com.reactorsolutions.holog.models.Category
import com.reactorsolutions.holog.repositories.CategoriesRepository
import com.reactorsolutions.holog.repositories.ItemsRepository
import com.reactorsolutions.holog.services.api.CategoriesServiceAPI
import org.springframework.stereotype.Service

@Service
class CategoriesServiceImpl(var categoriesRepository: CategoriesRepository, var itemsRepository: ItemsRepository) :
    CategoriesServiceAPI {

    override fun getAllCategories(): Set<Category> {
        return categoriesRepository.findAll().toSet()
    }

    override fun getCategoryById(id: Long): Category {
        return categoriesRepository.findById(id).orElseThrow { CategoryNotFoundException("Category not found") }
    }

    override fun createCategory(category: Category): Category {
        return categoriesRepository.save(category)
    }

    override fun updateCategory(id: Long, categoryUpdated: Category): Boolean {
        val category = categoriesRepository.findById(id)
            .orElseThrow { CategoryNotFoundException("Category not found") }
        categoryUpdated.id = id
        categoriesRepository.save(categoryUpdated)
        return true
    }

    override fun deleteCategory(id: Long): Category {
        val category: Category =
            categoriesRepository.findById(id).orElseThrow { CategoryNotFoundException("Category not found") }
        categoriesRepository.deleteById(id)
        return category
    }

}