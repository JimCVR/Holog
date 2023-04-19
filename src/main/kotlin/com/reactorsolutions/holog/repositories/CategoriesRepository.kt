package com.reactorsolutions.holog.repositories

import com.reactorsolutions.holog.models.Category
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoriesRepository : CrudRepository<Category, Long> {
}