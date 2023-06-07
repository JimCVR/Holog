package com.reactorsolutions.holog.controllers

import com.reactorsolutions.holog.dto.RequestCategoryDTO
import com.reactorsolutions.holog.dto.ResponseCategoryDTO
import com.reactorsolutions.holog.exceptions.category.CategoryNotFoundException
import com.reactorsolutions.holog.models.Category
import com.reactorsolutions.holog.models.Item
import com.reactorsolutions.holog.services.api.CategoriesServiceAPI
import com.reactorsolutions.holog.utils.mapper.category.CategoryToResponse
import com.reactorsolutions.holog.utils.mapper.category.RequestToCategory
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

class CategoriesControllerTest {

    private val fromRequestToCategory = RequestToCategory()
    private val fromCategoryToResponse = CategoryToResponse()

    @Test
    fun getAllCategoriesReturn200WhenThereAreTwoElements() {

        val serviceMock = mockk<CategoriesServiceAPI>()
        every { serviceMock.getAllCategories() } returns mutableSetOf(
            Category("peliculas", 1, mutableSetOf(), "1",1),
            Category("videojuegos", 2, mutableSetOf(), "1",2)
        )

        val categoriesController = CategoriesController(
            serviceMock,
            this.fromRequestToCategory, this.fromCategoryToResponse
        )
        val categories = categoriesController.getCategories("1")
        assertEquals(HttpStatus.OK, categories.statusCode)
        assertEquals(
            mutableListOf<ResponseCategoryDTO>(
                ResponseCategoryDTO(1, "peliculas", "1",1),
                ResponseCategoryDTO(2, "videojuegos", "1",2)
            ), categories.body
        )
    }

    @Test
    fun getAllCategoriesReturn200WhenThereIsOneElements() {
        val serviceMock = mockk<CategoriesServiceAPI>()
        every { serviceMock.getAllCategories() } returns mutableSetOf(Category("peliculas", 1, mutableSetOf(), "1",1))
        val categoriesController = CategoriesController(
            serviceMock,
            this.fromRequestToCategory, this.fromCategoryToResponse
        )
        val categories = categoriesController.getCategories("1")
        assertEquals(HttpStatus.OK, categories.statusCode)
        assertEquals(mutableListOf<ResponseCategoryDTO>(ResponseCategoryDTO(1, "peliculas", "1",1)), categories.body)
    }

    @Test
    fun getAllCategoriesReturn200WhenThereAreNoElements() {
        val serviceMock = mockk<CategoriesServiceAPI>()
        every { serviceMock.getAllCategories() } returns mutableSetOf()
        val categoriesController = CategoriesController(
            serviceMock,
            this.fromRequestToCategory, this.fromCategoryToResponse
        )
        val categories = categoriesController.getCategories("1")
        assertEquals(HttpStatus.OK, categories.statusCode)
        assertEquals(mutableListOf<RequestCategoryDTO>(), categories.body)
    }

    //GET CATEGORY BY ID TESTS
    @Test
    fun getCategoryByIdReturn200WhenThereAreOneElement() {
        val serviceMock = mockk<CategoriesServiceAPI>()
        every { serviceMock.getCategoryById(1) } returns Category("peliculas", 1, mutableSetOf(), "1",1)
        val categoriesController = CategoriesController(
            serviceMock,
            this.fromRequestToCategory, this.fromCategoryToResponse
        )
        val categories = categoriesController.getCategoryById("1", 1)
        assertEquals(HttpStatus.OK, categories.statusCode)
        assertEquals(ResponseCategoryDTO(1, "peliculas", "1",1), categories.body)
    }

    @Test
    fun getCategoryByIdReturn404WhenThereAreNoElement() {
        val serviceMock = mockk<CategoriesServiceAPI>()
        every { serviceMock.getCategoryById(1) } throws CategoryNotFoundException("Category not found")
        val categoriesController = CategoriesController(
            serviceMock,
            this.fromRequestToCategory, this.fromCategoryToResponse
        )
        assertThrows<CategoryNotFoundException> { categoriesController.getCategoryById("1", 1) }
    }

    //CREATE CATEGORY
    @Test
    fun createCategoryByIdReturn201WhenElementCreated() {

        val serviceMock = mockk<CategoriesServiceAPI>()
        every { serviceMock.createCategory("1",Category("peliculas", 2)) } returns Category(
            "peliculas",
            2,
            mutableSetOf(),
            "1"
        )
        val mockServletRequest = MockHttpServletRequest()
        RequestContextHolder.setRequestAttributes(ServletRequestAttributes(mockServletRequest))
        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(1)
            .toUri()

        val categoriesController = CategoriesController(
            serviceMock,
            this.fromRequestToCategory, this.fromCategoryToResponse
        )
        val categories = categoriesController.insertCategory("1", RequestCategoryDTO("peliculas", 2))
        assertEquals(HttpStatus.CREATED, categories.statusCode)
        assertEquals("Category created", categories.body)
    }


    @Test
    fun createCategoryReturn404WhenNotCreated() {
        val serviceMock = mockk<CategoriesServiceAPI>()
        every {
            serviceMock.createCategory(
                "1",
                Category(
                    "peliculas",
                    1
                )
            )
        } throws CategoryNotFoundException("Category not found")
        val categoriesController = CategoriesController(
            serviceMock,
            this.fromRequestToCategory, this.fromCategoryToResponse
        )
        assertThrows<CategoryNotFoundException> {
            categoriesController.insertCategory(
                "1",
                RequestCategoryDTO("peliculas", 1)
            )
        }
    }

    @Test
    fun createCategoryReturn407WhenNameIsBlank() {
        val serviceMock = mockk<CategoriesServiceAPI>()
        every {
            serviceMock.createCategory(
                "1",
                Category(
                    "",
                    1
                )
            )
        } returns Category("", 1)
        val categoriesController = CategoriesController(
            serviceMock,
            this.fromRequestToCategory, this.fromCategoryToResponse
        )
        val category = categoriesController.insertCategory("1", RequestCategoryDTO("", 1))
        assertEquals(HttpStatus.PRECONDITION_FAILED, category.statusCode)
        assertEquals("Category not created", category.body)
    }

    @Test
    fun createCategoryReturn407WhenIconIdIsBlank() {
        val serviceMock = mockk<CategoriesServiceAPI>()
        every {
            serviceMock.createCategory(
                "1",
                Category(
                    "libros2023"
                )
            )
        } returns Category("libros2023")
        val mockServletRequest = MockHttpServletRequest()
        RequestContextHolder.setRequestAttributes(ServletRequestAttributes(mockServletRequest))
        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(1)
            .toUri()
        val categoriesController = CategoriesController(
            serviceMock,
            this.fromRequestToCategory, this.fromCategoryToResponse
        )
        val category = categoriesController.insertCategory("1", RequestCategoryDTO("libros2023"))
        assertEquals(HttpStatus.CREATED, category.statusCode)
        assertEquals("Category created", category.body)
    }

    //UPDATE CATEGORY

    @Test
    fun updateCategoryByIdReturn200WhenElementModified() {
        val serviceMock = mockk<CategoriesServiceAPI>()
        every { serviceMock.updateCategory(1,Category("peliculas", 2)) } returns true
        val categoriesController = CategoriesController(
            serviceMock,
            this.fromRequestToCategory, this.fromCategoryToResponse
        )
        val categories = categoriesController.updateCategory("1", 1, RequestCategoryDTO("peliculas", 2))
        assertEquals(HttpStatus.OK, categories.statusCode)
        assertEquals("Category updated", categories.body)
    }

    @Test
    fun updateCategoryByIdReturn407WhenNameIsBlank() {
        val serviceMock = mockk<CategoriesServiceAPI>()
        every { serviceMock.updateCategory(1,Category("", 2)) } returns true
        val categoriesController = CategoriesController(
            serviceMock,
            this.fromRequestToCategory, this.fromCategoryToResponse
        )
        val categories = categoriesController.updateCategory("1", 1, RequestCategoryDTO("", 2))
        assertEquals(HttpStatus.PRECONDITION_FAILED, categories.statusCode)
        assertEquals("Category not modified", categories.body)
    }

    @Test
    fun updateCategoryByIdReturn404WhenIdNotFound() {
        val serviceMock = mockk<CategoriesServiceAPI>()
        every {
            serviceMock.updateCategory(
                1,
                Category(
                    "peliculas",
                    2
                )
            )
        } throws CategoryNotFoundException("Category not found")
        val categoriesController = CategoriesController(
            serviceMock,
            this.fromRequestToCategory, this.fromCategoryToResponse
        )
        assertThrows<CategoryNotFoundException> {
            categoriesController.updateCategory(
                "1",
                1,
                RequestCategoryDTO("peliculas", 2)
            )
        }
    }

    //DELETE CATEGORY
    @Test
    fun deleteCategoryByIdReturn200WhenElementDeleted() {
        val serviceMock = mockk<CategoriesServiceAPI>()
        every { serviceMock.deleteCategory(1) } returns Category("peliculas", null, mutableSetOf<Item>(), "1",1)
        val categoriesController = CategoriesController(
            serviceMock,
            this.fromRequestToCategory, this.fromCategoryToResponse
        )
        val categories = categoriesController.deleteCategory("1", 1)
        assertEquals(HttpStatus.OK, categories.statusCode)
        assertEquals(ResponseCategoryDTO(1, "peliculas", "1"), categories.body)
    }

    @Test
    fun deleteCategoryByIdReturn404WhenElementNotDeleted() {
        val serviceMock = mockk<CategoriesServiceAPI>()
        every { serviceMock.deleteCategory(1) } throws CategoryNotFoundException("Category not found")
        val categoriesController = CategoriesController(
            serviceMock,
            this.fromRequestToCategory, this.fromCategoryToResponse
        )
        assertThrows<CategoryNotFoundException> { categoriesController.deleteCategory("1", 1) }
    }
}
