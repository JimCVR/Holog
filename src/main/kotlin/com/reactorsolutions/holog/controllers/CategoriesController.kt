package com.reactorsolutions.holog.controllers

import com.reactorsolutions.holog.dto.CategoryDTO
import com.reactorsolutions.holog.models.Category
import com.reactorsolutions.holog.services.api.CategoriesServiceAPI
import com.reactorsolutions.holog.utils.mapper.Mapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class CategoriesController {

    @Autowired
    lateinit var categoryServiceAPI: CategoriesServiceAPI

    @Autowired
    lateinit var mapper: Mapper<CategoryDTO, Category>

    constructor(categoryServiceAPI: CategoriesServiceAPI, mapper: Mapper<CategoryDTO, Category>) {
        this.categoryServiceAPI = categoryServiceAPI
        this.mapper = mapper
    }


    @GetMapping("/{userId}/categories")
    fun getCategories(@PathVariable userId: Long): ResponseEntity<MutableList<CategoryDTO>> {
        val categories = categoryServiceAPI.getAllCategories()
        var categoriesDTO: MutableList<CategoryDTO> = mutableListOf()
        categories.forEach {
            val categoryDTO = mapper.fromEntity(it)
            categoriesDTO.add(categoryDTO)
        }
        return ResponseEntity(categoriesDTO, HttpStatus.OK)
    }

    @GetMapping("/{userId}/categories/{id}")
    fun getCategoryById(@PathVariable userId: Long, @PathVariable id: Long): ResponseEntity<Any> {
        val category = categoryServiceAPI.getCategoryById(id)
        val categoryDTO = mapper.fromEntity(category)
        return ResponseEntity(categoryDTO, HttpStatus.OK)
    }

    @PostMapping("/{userId}/categories")
    fun insertCategory(@PathVariable userId: Long, @RequestBody categoryDTO: CategoryDTO): ResponseEntity<Any> {
        val category = mapper.toEntity(categoryDTO)
        return if (categoryServiceAPI.createCategory(category)!= null)
            ResponseEntity("Category created", HttpStatus.OK)
        else
            ResponseEntity("Category not created", HttpStatus.BAD_REQUEST)
    }

    @PutMapping("/{userId}/categories/{id}")
    fun updateCategory(
        @PathVariable userId: Long,
        @PathVariable id: Long,
        @RequestBody categoryDTO: CategoryDTO
    ): ResponseEntity<Any> {
        val category = mapper.toEntity(categoryDTO)
        return if (categoryServiceAPI.updateCategory(category))
            ResponseEntity("Category updated", HttpStatus.OK)
        else
            ResponseEntity("Category id not found",HttpStatus.NOT_MODIFIED)
    }

    @DeleteMapping("/{userId}/categories/{id}")
    fun deleteCategory(@PathVariable userId: Long, id: Long): ResponseEntity<Any> {
        val deletedCategory = categoryServiceAPI.deleteCategory(id)
        val deletedCategoryDTO = mapper.fromEntity(deletedCategory)
        return ResponseEntity(deletedCategoryDTO, HttpStatus.OK)
    }
}