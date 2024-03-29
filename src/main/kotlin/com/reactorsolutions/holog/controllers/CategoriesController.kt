package com.reactorsolutions.holog.controllers

import com.reactorsolutions.holog.dto.RequestCategoryDTO
import com.reactorsolutions.holog.dto.ResponseCategoryDTO
import com.reactorsolutions.holog.models.Category
import com.reactorsolutions.holog.services.api.CategoriesServiceAPI
import com.reactorsolutions.holog.utils.mapper.Mapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/user")
class CategoriesController(

    var categoryServiceAPI: CategoriesServiceAPI,
    var toCategory: Mapper<RequestCategoryDTO, Category>,
    var toResponseCategory: Mapper<Category, ResponseCategoryDTO>
) {

    @GetMapping("/{userId}/categories")
    fun getCategories(@PathVariable userId: String): ResponseEntity<List<ResponseCategoryDTO>> {
        val categories = categoryServiceAPI.getAllCategories().filter { it.userId == userId }
        val categoriesDTO = categories.map {
            toResponseCategory.transform(it)
        }
        return ResponseEntity(categoriesDTO, HttpStatus.OK)
    }

    @GetMapping("/{userId}/categories/{id}")
    fun getCategoryById(@PathVariable userId: String, @PathVariable id: Long): ResponseEntity<ResponseCategoryDTO> {
        val category = categoryServiceAPI.getCategoryById(id)
        val categoryDTO = toResponseCategory.transform(category)
        return ResponseEntity(categoryDTO, HttpStatus.OK)
    }

    @PostMapping("/{userId}/categories")
    fun insertCategory(
        @PathVariable userId: String,
        @RequestBody requestCategoryDTO: RequestCategoryDTO
    ): ResponseEntity<String> {
        if (requestCategoryDTO.name.isBlank())
            return ResponseEntity("Category not created", HttpStatus.PRECONDITION_FAILED)

        val category = toCategory.transform(requestCategoryDTO)
        val categoryCreated = categoryServiceAPI.createCategory(userId, category)
        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(categoryCreated.id)
            .toUri()
        return ResponseEntity.created(location).body("Category created")
    }

    @PutMapping("/{userId}/categories/{id}")
    fun updateCategory(
        @PathVariable userId: String,
        @PathVariable id: Long,
        @RequestBody requestCategoryDTO: RequestCategoryDTO
    ): ResponseEntity<String> {
        if (requestCategoryDTO.name.isBlank())
            return ResponseEntity("Category not modified", HttpStatus.PRECONDITION_FAILED)

        val category = toCategory.transform(requestCategoryDTO)
        return if (categoryServiceAPI.updateCategory(id, category))
            ResponseEntity("Category updated", HttpStatus.OK)
        else
            ResponseEntity("Category id not found", HttpStatus.NOT_MODIFIED)
    }

    @DeleteMapping("/{userId}/categories/{id}")
    fun deleteCategory(@PathVariable userId: String,@PathVariable id: Long): ResponseEntity<ResponseCategoryDTO> {
        val deletedCategory = categoryServiceAPI.deleteCategory(id)
        val deletedCategoryDTO = toResponseCategory.transform(deletedCategory)
        return ResponseEntity(deletedCategoryDTO, HttpStatus.OK)
    }
}