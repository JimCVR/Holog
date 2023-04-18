package com.reactorsolutions.holog.controllers

import com.reactorsolutions.holog.dto.CategoryDTO
import com.reactorsolutions.holog.dto.ItemDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")

class ItemsController {

    @GetMapping("/{userId}/items")
    fun getItems(@PathVariable userId: Long): ResponseEntity<MutableList<ItemDTO>> {
        return ResponseEntity(mutableListOf(), HttpStatus.OK)
    }

    @GetMapping("/{userId}/items/{id}")
    fun getItemById(@PathVariable userId: Long, @PathVariable id: String): ResponseEntity<Any> {
        return ResponseEntity(ItemDTO(0,"","",""), HttpStatus.OK)
    }

    @GetMapping("/{userId}/items/categories/{categoryId}")
    fun getItemByCategory(@PathVariable userId: Long, @PathVariable categoryId: String): ResponseEntity<Any> {

        return ResponseEntity(CategoryDTO(0,"",0), HttpStatus.OK)
    }


    @PostMapping("/{userId}/items")
    fun insertItem(@PathVariable userId: Long, @RequestBody item: ItemDTO): ResponseEntity<Any> {
        return ResponseEntity<Any>(item, HttpStatus.OK)
    }

    @PutMapping("/{userId}/items/{id}")
    fun updateItem(@PathVariable userId: Long, @PathVariable id: String, @RequestBody item: ItemDTO): ResponseEntity<Any> {
        return ResponseEntity<Any>(item, HttpStatus.OK)
    }

    @DeleteMapping("/{userId}/items/{id}")
    fun deleteItem(@PathVariable userId: Long, id: String): ResponseEntity<Any> {
        return ResponseEntity<Any>(ItemDTO(0,"","",""), HttpStatus.OK)
    }
}