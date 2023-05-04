package com.reactorsolutions.holog.controllers

import com.reactorsolutions.holog.dto.RequestItemDTO
import com.reactorsolutions.holog.dto.ResponseItemDTO
import com.reactorsolutions.holog.models.Item
import com.reactorsolutions.holog.services.api.ItemsServiceAPI
import com.reactorsolutions.holog.utils.mapper.Mapper
import com.reactorsolutions.holog.utils.mapper.category.CategoryToResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/user")
class ItemsController(

    var itemsServiceAPI: ItemsServiceAPI,
    var toItem: Mapper<RequestItemDTO, Item>,
    var toResponseItem: Mapper<Item, ResponseItemDTO>
) {

    @GetMapping("/{userId}/items")
    fun getItems(@PathVariable userId: Long): ResponseEntity<List<ResponseItemDTO>> {
        val items = itemsServiceAPI.getAllItems()
        val responseItemsDTO = items.map { toResponseItem.transform(it) }
        return ResponseEntity(responseItemsDTO, HttpStatus.OK)
    }

    @GetMapping("/{userId}/items/{id}")
    fun getItemById(@PathVariable userId: Long, @PathVariable id: Long): ResponseEntity<ResponseItemDTO> {
        val item = itemsServiceAPI.getItemById(id)
        val itemDTO = toResponseItem.transform(item)
        return ResponseEntity(itemDTO, HttpStatus.OK)
    }

    @GetMapping("/{userId}/categories/{categoryId}/items")
    fun getItemByCategory(
        @PathVariable userId: Long,
        @PathVariable categoryId: Long
    ): ResponseEntity<Set<ResponseItemDTO>> {
        val items = itemsServiceAPI.getItemByCategory(categoryId)
        val responseItemDTO = items.map {  toResponseItem.transform(it)}.toMutableSet()
        return ResponseEntity(responseItemDTO, HttpStatus.OK)
    }


    @PostMapping("/{userId}/items")
    fun insertItem(@PathVariable userId: Long, @RequestBody requestItemDTO: RequestItemDTO): ResponseEntity<String> {
        if (requestItemDTO.name.isBlank())
            return ResponseEntity("Item not created", HttpStatus.PRECONDITION_FAILED)

        val item = toItem.transform(requestItemDTO)
        val itemCreated = itemsServiceAPI.createItem(requestItemDTO.categoriesId,item)
        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(itemCreated.id)
            .toUri()
        return ResponseEntity.created(location).body("Item created")
    }

    @PutMapping("/{userId}/items/{id}")
    fun updateItem(
        @PathVariable userId: Long,
        @PathVariable id: Long,
        @RequestBody requestItemDTO: RequestItemDTO
    ): ResponseEntity<String> {
        if (requestItemDTO.name.isBlank())
            return ResponseEntity("Item not modified", HttpStatus.PRECONDITION_FAILED)

        val item = toItem.transform(requestItemDTO)
        return if (itemsServiceAPI.updateItem(id,item))
            ResponseEntity("Item updated", HttpStatus.OK)
        else
            ResponseEntity("Item id not found", HttpStatus.NOT_MODIFIED)
    }

    @DeleteMapping("/{userId}/items/{id}")
    fun deleteItem(@PathVariable userId: Long, id: Long): ResponseEntity<ResponseItemDTO> {
        val deletedItem = itemsServiceAPI.deleteItem(id)
        val deletedItemDTO = toResponseItem.transform(deletedItem)
        return ResponseEntity(deletedItemDTO, HttpStatus.OK)
    }
}