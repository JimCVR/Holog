package com.reactorsolutions.holog.controllers

import com.reactorsolutions.holog.models.Category
import com.reactorsolutions.holog.models.Item
import com.reactorsolutions.holog.services.api.ItemsServiceAPI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
class ItemsController {
    @Autowired
    lateinit var ItemsService: ItemsServiceAPI

    @GetMapping("/{userId}/items")
    fun getItems(@PathVariable userId: Long): ResponseEntity<MutableList<Item>> {
        return ResponseEntity(ItemsService.all, HttpStatus.OK)
    }

    @GetMapping("/{userId}/items/{id}")
    fun getItemById(@PathVariable userId: Long, @PathVariable id: String): ResponseEntity<Any> {
        val itemId = id.toLongOrNull()
        if (itemId != null) {
            val item = ItemsService[itemId]
            if (item != null) {
                return ResponseEntity<Any>(item, HttpStatus.OK)
            } else {
                return ResponseEntity("ITEM NOT FOUND", HttpStatus.NOT_FOUND)
            }

        }
        return ResponseEntity("INVALID ID SUPPLIED", HttpStatus.BAD_REQUEST)
    }

    @GetMapping("/{userId}/items/categories/{categoryId}")
    fun getItemByCategory(@PathVariable userId: Long, @PathVariable categoryId: String): ResponseEntity<Any> {
        /*val catId = categoryId.toLongOrNull()
        if (catId != null) {
            var itemsFiltered: MutableSet<Item> = mutableSetOf()
            var item =
                ItemsService.all
            item.forEach { it ->
                val newItem = it.categories!!.find {
                    it.id == catId
                }
                if (newItem != null) {
                    itemsFiltered.add(it)
                }
            }
            if (item != null) {
                return ResponseEntity<Any>(item, HttpStatus.OK)
            } else {
                return ResponseEntity("ITEM NOT FOUND", HttpStatus.NOT_FOUND)
            }
        }*/
        return ResponseEntity("INVALID ID SUPPLIED", HttpStatus.BAD_REQUEST)
    }


    @PostMapping("/{userId}/items")
    fun insertItem(@PathVariable userId: Long, @RequestBody item: Item): ResponseEntity<Any> {
        val hasItem = ItemsService[item.id]
        if (hasItem != null) {
            return ResponseEntity<Any>("ITEM ALREADY EXISTS", HttpStatus.BAD_REQUEST)
        }
        val newItem = ItemsService.save(item)
        return ResponseEntity<Any>(newItem, HttpStatus.OK)
    }

    @PutMapping("/{userId}/items/{id}")
    fun updateItem(@PathVariable userId: Long, @PathVariable id: String, @RequestBody item: Item): ResponseEntity<Any> {
        var updatedItem = ItemsService[item.id]
        if (updatedItem != null) {
            ItemsService.save(updatedItem)
            return ResponseEntity<Any>("ITEM MODIFIED", HttpStatus.OK)
        }
        return ResponseEntity<Any>("ITEM NOT FOUND", HttpStatus.NOT_MODIFIED)
    }

    @DeleteMapping("/{userId}/items/{id}")
    fun deleteItem(@PathVariable userId: Long, id: String): ResponseEntity<Any> {
        val itemId = id.toLongOrNull()
        if (itemId != null) {
            val deletedItem = ItemsService[itemId]
            if (itemId != null) {
                ItemsService.delete(itemId)
                return ResponseEntity<Any>(deletedItem, HttpStatus.OK)
            }
        }
        return ResponseEntity<Any>("INVALID ID ITEM VALUE", HttpStatus.BAD_REQUEST)
    }
}