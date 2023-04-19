package com.reactorsolutions.holog.controllers

import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CategoriesControllerTest {
    private val categoriesController = CategoriesController()
    @Test
    fun getAllCategories() {
        val categories = categoriesController.getCategories(1)
        assertEquals(1,categories)
    }
}