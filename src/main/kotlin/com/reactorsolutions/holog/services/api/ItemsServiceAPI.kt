package com.reactorsolutions.holog.services.api

import com.reactorsolutions.holog.models.Item

interface ItemsServiceAPI {
    fun getAllItems(): Set<Item>
    fun getItemById(id:Long): Item
    fun getItemByCategory(id:Long): Set<Item>
    fun recommendation(): Set<Item>
    fun createItem(categoryId:Long, item: Item): Item
    fun updateItem(categoryId: Long,id: Long, item: Item):Boolean
    fun deleteItem(id:Long): Item
}