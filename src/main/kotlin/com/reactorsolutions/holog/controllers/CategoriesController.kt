package com.reactorsolutions.holog.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class CategoriesController {


    @GetMapping("/{userId}/categories")
    fun getCategories(@PathVariable userId: Long): String {
        return "RETURNS ALL CATEGORIES"
    }

    @GetMapping("/{userId}/categories/{id}")
    fun getCategoryById(@PathVariable userId: Long, @PathVariable id: String): String {

        return return "RETURNS CATEGORY BY ID"
    }

    @PostMapping("/{userId}/categories")
    fun insertCategory(@PathVariable userId: Long): String {
        return "CATEGORY INSERTED"
    }

    @PutMapping("/{userId}/categories/{id}")
    fun updateCategory(
        @PathVariable userId: Long,
        @PathVariable id: String
    ): String {
        return "CATEGORY MODIFIED"
    }

    @DeleteMapping("/{userId}/categories/{id}")
    fun deleteCategory(@PathVariable userId: Long, id: String): String {
        return "CATEGORY DELETED"
    }
}