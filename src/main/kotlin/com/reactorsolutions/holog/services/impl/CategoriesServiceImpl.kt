package com.reactorsolutions.holog.services.impl

import com.reactorsolutions.holog.commons.GenericServiceImpl
import com.reactorsolutions.holog.models.Category
import com.reactorsolutions.holog.repositories.CategoriesRepository
import com.reactorsolutions.holog.services.api.CategoriesServiceAPI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service

@Service
class CategoriesServiceImpl : CategoriesServiceAPI , GenericServiceImpl<Category, Long>() {
    @Autowired
    lateinit var categoriesRepository: CategoriesRepository

    override val dao: CrudRepository<Category, Long>
        get() = categoriesRepository
}