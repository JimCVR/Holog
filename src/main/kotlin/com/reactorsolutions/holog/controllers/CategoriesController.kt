package com.reactorsolutions.holog.controllers

import com.reactorsolutions.holog.dto.CategoryDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class CategoriesController {


    @GetMapping("/{userId}/categories")
    fun getCategories(@PathVariable userId: Long): ResponseEntity<MutableList<CategoryDTO>> {
        return ResponseEntity(mutableListOf(), HttpStatus.OK)
    }

    @GetMapping("/{userId}/categories/{id}")
    fun getCategoryById(@PathVariable userId: Long, @PathVariable id: String): ResponseEntity<Any> {
        return ResponseEntity(CategoryDTO(0,"",0), HttpStatus.OK)
    }

    @PostMapping("/{userId}/categories")
    fun insertCategory(@PathVariable userId: Long, @RequestBody category: CategoryDTO): ResponseEntity<Any> {
        return ResponseEntity(CategoryDTO(0,"",0), HttpStatus.OK)
    }

    @PutMapping("/{userId}/categories/{id}")
    fun updateCategory(
        @PathVariable userId: Long,
        @PathVariable id: String,
        @RequestBody category: CategoryDTO
    ): ResponseEntity<Any> {
        return ResponseEntity(CategoryDTO(0,"",0), HttpStatus.OK)
    }

    @DeleteMapping("/{userId}/categories/{id}")
    fun deleteCategory(@PathVariable userId: Long, id: String): ResponseEntity<Any> {
        return ResponseEntity(CategoryDTO(0,"",0), HttpStatus.OK)
    }
}