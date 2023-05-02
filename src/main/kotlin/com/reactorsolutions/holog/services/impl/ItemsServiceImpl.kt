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
        return itemsRepository.findAll().toSet()
    }

    override fun getItemById(id: Long): Item {
        val item: Item? = itemsRepository.findById(id).orElse(null)

        if (item != null)
            return item
        else
            throw ItemNotFoundException("Item no found")
    }

    override fun getItemByCategory(categoryId: Long): Set<Item> {
        val category = categoriesRepository.findById(categoryId).orElseThrow{CategoryNotFoundException("")}

        if (category != null) {
            val items = category.items
            if (items != null) {
                return items
            }
        }
        return mutableSetOf()
    }

    override fun createItem(categoriesId: List<Long>, item: Item): Item {
        var exceptions = mutableSetOf<Long>()
        var category: Category? = null
        categoriesId.forEach {
            category = categoriesRepository.findById(it).orElse(null)
            if (category == null) {
                exceptions.add(it)
            } else {
                category!!.items.add(itemsRepository.save(item))
            }
        }

        if (exceptions.isNotEmpty()) {
            throw CategoryNotFoundException("Category not found for id: $exceptions")
        } else categoriesRepository.save(category!!)

        return item
    }

    override fun updateItem(id: Long, itemUpdated: Item): Boolean {
        val item = itemsRepository.findById(id).orElseThrow { ItemNotFoundException("Item not found") }
        itemUpdated.id = id
        itemsRepository.save(itemUpdated)
        return true
    }

    override fun deleteItem(id: Long): Item {
        val item: Item? = itemsRepository.findById(id).orElse(null)

        if (item != null) {
            itemsRepository.deleteById(id)
            return item
        } else
            throw ItemNotFoundException("Item no found")
    }

}