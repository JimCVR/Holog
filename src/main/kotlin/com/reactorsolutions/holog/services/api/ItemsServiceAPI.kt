package com.reactorsolutions.holog.services.api


import com.reactorsolutions.holog.models.Category
import com.reactorsolutions.holog.models.Item

interface ItemsServiceAPI {
    fun getAllItems():MutableList<Item>
    fun getItemById(id:Long): Item
    fun getItemByCategory(id:Long): Category
    fun createItem(categoriesId:MutableList<Long>,item: Item): Item
    fun updateItem(id: Long, item: Item):Boolean
    fun deleteItem(id:Long): Item
}