package com.reactorsolutions.holog.services.impl

import com.reactorsolutions.holog.exceptions.category.CategoryNotFoundException
import com.reactorsolutions.holog.models.Category
import com.reactorsolutions.holog.repositories.CategoriesRepository
import com.reactorsolutions.holog.services.api.CategoriesServiceAPI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.function.Supplier

@Service
class CategoriesServiceImpl : CategoriesServiceAPI {

    @Autowired
    lateinit var categoriesRepository: CategoriesRepository

    override fun getAllCategories(): MutableList<Category> {
        return categoriesRepository.findAll() as MutableList<Category>
    }

    override fun getCategoryById(id: Long): Category {
        return categoriesRepository.findById(id).orElseThrow { CategoryNotFoundException("Category not found") }
    }

    override fun createCategory(category: Category): Category {
        return categoriesRepository.save(category)
    }

    override fun updateCategory(categoryUpdated: Category): Boolean {
        val category = categoriesRepository.findById(categoryUpdated.id!!)
            .orElseThrow { CategoryNotFoundException("Category not found") }
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