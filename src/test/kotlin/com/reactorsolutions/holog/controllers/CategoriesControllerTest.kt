package com.reactorsolutions.holog.controllers

import com.reactorsolutions.holog.dto.CategoryDTO
import com.reactorsolutions.holog.exceptions.category.CategoryNotFoundException
import com.reactorsolutions.holog.models.Category
import com.reactorsolutions.holog.services.api.CategoriesServiceAPI
import com.reactorsolutions.holog.utils.mapper.CategoryMapper
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class CategoriesControllerTest {

    private val mapper = CategoryMapper()

    @Test
    fun getAllCategoriesReturn200WhenThereAreTwoElements() {

        val serviceMock = mockk<CategoriesServiceAPI>()
        every { serviceMock.getAllCategories() } returns mutableListOf(
            Category(1, "peliculas", 0),
            Category(2, "videojuegos", 1)
        )

        val categoriesController = CategoriesController(serviceMock, mapper)
        val categories = categoriesController.getCategories(1)
        assertEquals(HttpStatus.OK, categories.statusCode)
        assertEquals(mutableListOf<CategoryDTO>(CategoryDTO(1, "peliculas", 0), CategoryDTO(2, "videojuegos", 1)), categories.body)
    }

    @Test
    fun getAllCategoriesReturn200WhenThereIsOneElements() {
        val serviceMock = mockk<CategoriesServiceAPI>()
        every { serviceMock.getAllCategories() } returns mutableListOf(Category(1, "peliculas", 0))
        val categoriesController = CategoriesController(serviceMock, mapper)
        val categories = categoriesController.getCategories(1)
        assertEquals(HttpStatus.OK, categories.statusCode)
        assertEquals(mutableListOf<CategoryDTO>(CategoryDTO(1, "peliculas", 0)), categories.body)
    }

    @Test
    fun getAllCategoriesReturn200WhenThereAreNoElements() {
        val serviceMock = mockk<CategoriesServiceAPI>()
        every { serviceMock.getAllCategories() } returns mutableListOf()
        val categoriesController = CategoriesController(serviceMock, mapper)
        val categories = categoriesController.getCategories(1)
        assertEquals(HttpStatus.OK, categories.statusCode)
        assertEquals(mutableListOf<CategoryDTO>(), categories.body)
    }

    //GET CATEGORY BY ID TESTS
    @Test
    fun getCategoryByIdReturn200WhenThereAreOneElement() {
        val serviceMock = mockk<CategoriesServiceAPI>()
        every { serviceMock.getCategoryById(1) } returns Category(1, "peliculas", 1)
        val categoriesController = CategoriesController(serviceMock, mapper)
        val categories = categoriesController.getCategoryById(1, 1)
        assertEquals(HttpStatus.OK, categories.statusCode)
        assertEquals(CategoryDTO(1,"peliculas",1), categories.body)
    }

    @Test
    fun getCategoryByIdReturn404WhenThereAreNoElement() {
        val serviceMock = mockk<CategoriesServiceAPI>()
        every { serviceMock.getCategoryById(1) } throws CategoryNotFoundException("Category not found")
        val categoriesController = CategoriesController(serviceMock, mapper)
        assertThrows<CategoryNotFoundException> { categoriesController.getCategoryById(1, 1) }
    }

    //CREATE CATEGORY
    @Test
    fun createCategoryByIdReturn200WhenElementCreated() {
        val serviceMock = mockk<CategoriesServiceAPI>()
        every { serviceMock.createCategory(Category(1, "peliculas", 2)) } returns Category(1, "peliculas", 2)
        val categoriesController = CategoriesController(serviceMock, mapper)
        val categories = categoriesController.insertCategory(1, CategoryDTO(1, "peliculas", 2))
        assertEquals(HttpStatus.OK, categories.statusCode)
        assertEquals(Category(1,"peliculas",2), categories.body)
    }


    @Test
    fun createCategoryReturn404WhenNotCreated() {
        val serviceMock = mockk<CategoriesServiceAPI>()
        every {
            serviceMock.createCategory(
                Category(
                    1,
                    "peliculas",
                    1
                )
            )
        } throws CategoryNotFoundException("Category not found")
        val categoriesController = CategoriesController(serviceMock, mapper)
        assertThrows<CategoryNotFoundException> {
            categoriesController.insertCategory(
                1,
                CategoryDTO(1, "peliculas", 1)
            )
        }
    }
    @Test
    fun createCategoryReturn407WhenNameIsBlank() {
        val serviceMock = mockk<CategoriesServiceAPI>()
        every {
            serviceMock.createCategory(
                Category(
                    1,
                    "",
                    1
                )
            )
        } returns Category(1, "", 1)
        val categoriesController = CategoriesController(serviceMock, mapper)
        val category = categoriesController.insertCategory(1,CategoryDTO(1,"",1))
        assertEquals(HttpStatus.PRECONDITION_FAILED,category.statusCode)
        assertEquals("Category not created",category.body)
    }

    //UPDATE CATEGORY

    @Test
    fun updateCategoryByIdReturn200WhenElementModified() {
        val serviceMock = mockk<CategoriesServiceAPI>()
        every { serviceMock.updateCategory(Category(1, "peliculas", 2)) } returns true
        val categoriesController = CategoriesController(serviceMock, mapper)
        val categories = categoriesController.updateCategory(1, 1, CategoryDTO(1, "peliculas", 2))
        assertEquals(HttpStatus.OK, categories.statusCode)
        assertEquals(Category(1,"peliculas",2), categories.body)
    }

    @Test
    fun updateCategoryByIdReturn407WhenNameIsBlank() {
        val serviceMock = mockk<CategoriesServiceAPI>()
        every { serviceMock.updateCategory(Category(1, "", 2)) } returns true
        val categoriesController = CategoriesController(serviceMock, mapper)
        val categories = categoriesController.updateCategory(1, 1, CategoryDTO(1, "", 2))
        assertEquals(HttpStatus.PRECONDITION_FAILED, categories.statusCode)
        assertEquals("Category not modified", categories.body)
    }

    @Test
    fun updateCategoryByIdReturn404WhenIdNotFound() {
        val serviceMock = mockk<CategoriesServiceAPI>()
        every {
            serviceMock.updateCategory(
                Category(
                    1,
                    "peliculas",
                    2
                )
            )
        } throws CategoryNotFoundException("Category not found")
        val categoriesController = CategoriesController(serviceMock, mapper)
        assertThrows<CategoryNotFoundException> {
            categoriesController.updateCategory(
                1,
                1,
                CategoryDTO(1, "peliculas", 2)
            )
        }
    }

    //DELETE CATEGORY
    @Test
    fun deleteCategoryByIdReturn200WhenElementDeleted() {
        val serviceMock = mockk<CategoriesServiceAPI>()
        every { serviceMock.deleteCategory(1) } returns Category(1, "peliculas", 1)
        val categoriesController = CategoriesController(serviceMock, mapper)
        val categories = categoriesController.deleteCategory(1, 1)
        assertEquals(HttpStatus.OK, categories.statusCode)
        assertEquals(CategoryDTO(1,"peliculas",1), categories.body)
    }

    @Test
    fun deleteCategoryByIdReturn404WhenElementNotDeleted() {
        val serviceMock = mockk<CategoriesServiceAPI>()
        every { serviceMock.deleteCategory(1) } throws CategoryNotFoundException("Category not found")
        val categoriesController = CategoriesController(serviceMock, mapper)
        assertThrows<CategoryNotFoundException> { categoriesController.deleteCategory(1, 1) }
    }
}
