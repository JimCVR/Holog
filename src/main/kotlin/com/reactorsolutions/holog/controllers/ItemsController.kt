package com.reactorsolutions.holog.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
class ItemsController {

    @GetMapping("/{userId}/items/{id}")
    fun getItemById(@PathVariable userId: Long, @PathVariable id: String): String {
        return "RETURNS"
    }

    @GetMapping("/{userId}/items/categories/{categoryId}")
    fun getItemByCategory(@PathVariable userId: Long, @PathVariable categoryId: String): String {
        return "RETURNS ITEMS BY CATEGORY"
    }


    @PostMapping("/{userId}/items")
    fun insertItem(@PathVariable userId: Long): String {
        return "ITEM ADDED"
    }

    @PutMapping("/{userId}/items/{id}")
    fun updateItem(@PathVariable userId: Long, @PathVariable id: String): String {
        return "ITEM MODIFIED"
    }

    @DeleteMapping("/{userId}/items/{id}")
    fun deleteItem(@PathVariable userId: Long, id: String): String {
        return "ITEM DELETED"
    }
}