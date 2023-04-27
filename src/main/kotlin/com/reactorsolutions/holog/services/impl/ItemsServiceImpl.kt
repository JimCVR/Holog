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

    override fun getAllItems(): MutableList<Item> {
        return itemsRepository.findAll() as MutableList<Item>
    }

    override fun getItemById(id: Long): Item {
        val item: Item? = itemsRepository.findById(id).orElse(null)
        if (item != null)
            return item
        else
            throw ItemNotFoundException("Item no found")

    }

    override fun getItemByCategory(categoryId: Long): MutableSet<Item> {
        val category = categoriesRepository.findById(categoryId).orElseThrow{CategoryNotFoundException("")}

        if (category != null) {
            val items = category.items
            if (items != null) {
                return items
            }
        }
        return mutableSetOf()

        //esto itera sobre los items y sobre las categorias de items, comparando id
        // y añadiendolo a la lista de items que devolvemos
        /* val item = itemsRepository.findAll()
            val itemsByCategory: MutableSet<Item> = mutableSetOf()

            item.forEach {
                it.categories.forEach { it2 ->
                    if (it2.id == categoryId)
                        itemsByCategory.add(it)
                }
            }
            return itemsByCategory*/
    }

    override fun createItem(categoriesId: MutableList<Long>, item: Item): Item {
        val itemCreated = itemsRepository.save(item)
        var exceptions = mutableSetOf<Long>()
        var category: Category? = null
        categoriesId.forEach {
            category = categoriesRepository.findById(it).orElse(null)
            if (category == null) {
                exceptions.add(it)
            } else {
                category!!.items!!.add(itemCreated)
            }
        }

        if (exceptions.isNotEmpty()) {
            itemsRepository.delete(itemCreated)
            throw CategoryNotFoundException("Category not found for id: $exceptions")
        } else categoriesRepository.save(category!!)

        return itemCreated
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