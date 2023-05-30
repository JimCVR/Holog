package com.reactorsolutions.holog.services.impl

import com.reactorsolutions.holog.exceptions.category.CategoryNotFoundException
import com.reactorsolutions.holog.exceptions.item.ItemNotFoundException
import com.reactorsolutions.holog.models.Category
import com.reactorsolutions.holog.models.Item
import com.reactorsolutions.holog.repositories.CategoriesRepository
import com.reactorsolutions.holog.repositories.ItemsRepository
import com.reactorsolutions.holog.services.api.ItemsServiceAPI
import org.springframework.stereotype.Service

@Service
class ItemsServiceImpl(var itemsRepository: ItemsRepository, var categoriesRepository: CategoriesRepository) :
    ItemsServiceAPI {

    override fun getAllItems(): Set<Item> {
        return itemsRepository.findAllByOrderByIdAsc().toSet()
    }

    override fun getItemById(id: Long): Item {
        val item: Item? = itemsRepository.findById(id).orElseThrow { ItemNotFoundException("Item Not found") }
        return item!!
    }

    override fun getItemByCategory(categoryId: Long): Set<Item> {
        val category =
            categoriesRepository.findById(categoryId).orElseThrow { CategoryNotFoundException("Category not found") }
        return category.items!!
    }

    override fun createItem(categoryId: Long, item: Item): Item {
        categoriesRepository.findById(categoryId).ifPresentOrElse({
            item.category = it
        }, {
            throw CategoryNotFoundException("Category not found")
        })
        return itemsRepository.save(item)
    }

    override fun updateItem(categoryId: Long,id: Long, itemUpdated: Item): Boolean {
        itemsRepository.findById(id).orElseThrow { ItemNotFoundException("Item not found") }
        itemUpdated.category = categoriesRepository.findById(categoryId).orElseThrow { CategoryNotFoundException("Category not found") }
        itemUpdated.id = id
        itemsRepository.save(itemUpdated)
        return true
    }

    override fun deleteItem(id: Long): Item {
        val item: Item? = itemsRepository.findById(id).orElseThrow { ItemNotFoundException("Item not found") }
        itemsRepository.delete(item!!)
        return item!!
    }

}