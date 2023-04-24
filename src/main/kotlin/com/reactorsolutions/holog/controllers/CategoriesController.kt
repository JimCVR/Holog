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
    var toResponse: Mapper<Category, ResponseCategoryDTO>
) {
    @GetMapping("/{userId}/categories")
    fun getCategories(@PathVariable userId: Long): ResponseEntity<List<ResponseCategoryDTO>> {
        val categories = categoryServiceAPI.getAllCategories()
        val categoriesDTO = categories.map {
            toResponse.transform(it)
        }
        return ResponseEntity(categoriesDTO, HttpStatus.OK)
    }

    @GetMapping("/{userId}/categories/{id}")
    fun getCategoryById(@PathVariable userId: Long, @PathVariable id: Long): ResponseEntity<ResponseCategoryDTO> {
        val category = categoryServiceAPI.getCategoryById(id)
        val categoryDTO = toResponse.transform(category)
        return ResponseEntity(categoryDTO, HttpStatus.OK)
    }

    @PostMapping("/{userId}/categories")
    fun insertCategory(
        @PathVariable userId: Long,
        @RequestBody requestCategoryDTO: RequestCategoryDTO
    ): ResponseEntity<String> {
        if (requestCategoryDTO.name.isBlank())
            return ResponseEntity("Category not created", HttpStatus.PRECONDITION_FAILED)
        val category = toCategory.transform(requestCategoryDTO)
        val categoryCreated = categoryServiceAPI.createCategory(category)
        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(categoryCreated.id)
            .toUri()
        return ResponseEntity.created(location).body("Category created")
        //return ResponseEntity("Category created",HttpStatus.OK)
    }

    @PutMapping("/{userId}/categories/{id}")
    fun updateCategory(
        @PathVariable userId: Long,
        @PathVariable id: Long,
        @RequestBody requestCategoryDTO: RequestCategoryDTO
    ): ResponseEntity<String> {
        if (requestCategoryDTO.name.isBlank())
            return ResponseEntity("Category not modified", HttpStatus.PRECONDITION_FAILED)
        var category = toCategory.transform(requestCategoryDTO)
        category.id = id
        return if (categoryServiceAPI.updateCategory(category))
            ResponseEntity("Category updated", HttpStatus.OK)
        else
            ResponseEntity("Category id not found", HttpStatus.NOT_MODIFIED)
    }

    @DeleteMapping("/{userId}/categories/{id}")
    fun deleteCategory(@PathVariable userId: Long, id: Long): ResponseEntity<ResponseCategoryDTO> {
        val deletedCategory = categoryServiceAPI.deleteCategory(id)
        val deletedCategoryDTO = toResponse.transform(deletedCategory)
        return ResponseEntity(deletedCategoryDTO, HttpStatus.OK)
    }
}