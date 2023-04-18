package com.reactorsolutions.holog.controllers

import com.reactorsolutions.holog.models.Category
import com.reactorsolutions.holog.services.api.CategoriesServiceAPI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
class CategoriesController {
    @Autowired
    lateinit var categoriesService: CategoriesServiceAPI


    @GetMapping("")
    fun getHelloworld(): ResponseEntity<Any> {
        return ResponseEntity("Hello world", HttpStatus.OK)
    }

    @GetMapping("/{userId}/categories")
    fun getCategories(@PathVariable userId: Long): ResponseEntity<MutableList<Category>> {
        return ResponseEntity(categoriesService.all, HttpStatus.OK)
    }

    @GetMapping("/{userId}/categories/{id}")
    fun getCategoryById(@PathVariable userId: Long, @PathVariable id: String): ResponseEntity<Any> {
        val categoryId = id.toLongOrNull()
        if (categoryId != null) {
            val category = categoriesService[categoryId]
            if (category != null) {
                return ResponseEntity<Any>(category, HttpStatus.OK)
            } else {
                return ResponseEntity("CATEGORY NOT FOUND", HttpStatus.NOT_FOUND)
            }

        }
        return ResponseEntity("INVALID ID SUPPLIED", HttpStatus.BAD_REQUEST)
    }

    @PostMapping("/{userId}/categories")
    fun insertCategory(@PathVariable userId: Long, @RequestBody category: Category): ResponseEntity<Any> {
        val hasCategory = categoriesService[category.id]
        if (hasCategory != null) {
            return ResponseEntity<Any>("CATEGORY ALREADY EXISTS", HttpStatus.BAD_REQUEST)
        }
        val newCategory = categoriesService.save(category)
        return ResponseEntity<Any>(newCategory, HttpStatus.OK)
    }

    @PutMapping("/{userId}/categories/{id}")
    fun updateCategory(
        @PathVariable userId: Long,
        @PathVariable id: String,
        @RequestBody category: Category
    ): ResponseEntity<Any> {
        var updatedCategory = categoriesService[category.id]
        if (updatedCategory != null) {
            categoriesService.save(updatedCategory)
            return ResponseEntity<Any>("CATEGORY MODIFIED", HttpStatus.OK)
        }
        return ResponseEntity<Any>("CATEGORY NOT FOUND", HttpStatus.NOT_MODIFIED)
    }

    @DeleteMapping("/{userId}/categories/{id}")
    fun deleteCategory(@PathVariable userId: Long, id: String): ResponseEntity<Any> {
        val categoryId = id.toLongOrNull()
        if (categoryId != null) {
            val deletedCategory = categoriesService[categoryId]
            if (categoryId != null){
                categoriesService.delete(categoryId)
                return ResponseEntity<Any>(deletedCategory, HttpStatus.OK)
            }
        }
        return ResponseEntity<Any>("INVALID ID CATEGORY VALUE", HttpStatus.BAD_REQUEST)
    }
}