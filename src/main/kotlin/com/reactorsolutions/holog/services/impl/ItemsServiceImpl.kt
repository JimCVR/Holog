package com.reactorsolutions.holog.services.impl

import com.reactorsolutions.holog.exceptions.category.CategoryNotFoundException
import com.reactorsolutions.holog.exceptions.item.ItemNotFoundException
import com.reactorsolutions.holog.models.Category
import com.reactorsolutions.holog.models.Item
import com.reactorsolutions.holog.repositories.CategoriesRepository
import com.reactorsolutions.holog.repositories.ItemsRepository
import com.reactorsolutions.holog.services.api.ItemsServiceAPI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class ItemsServiceImpl : ItemsServiceAPI {

    @Autowired
    lateinit var itemsRepository: ItemsRepository

    @Autowired
    lateinit var categoriesRepository: CategoriesRepository
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

    override fun getItemByCategory(id: Long): Category {
        val items = itemsRepository.findAll()
        var category: Category?
        items.forEach { it ->
            if (it.categories != null) {
                category = it.categories!!.first { it2 ->
                    it2.id == id
                }
            }
        }
        return Category(" ", 1)
    }

    override fun createItem(categoriesId: MutableList<Long>, item: Item): Item {
        val itemCreated = itemsRepository.save(item)
        var exceptions = mutableSetOf<Long>()
        var category :Category? = null
        //por cada category id voy a buscar si esta,
        categoriesId.forEach {
             category = categoriesRepository.findById(it).orElse(null)
            if (category == null) {
                exceptions.add(it)
            } else {
                category!!.items!!.add(itemCreated)
            }

        }
        if (exceptions.isNotEmpty()){
            itemsRepository.delete(itemCreated)
            throw CategoryNotFoundException("Category not found for id: $exceptions")
        }
        else  categoriesRepository.save(category!!)

        return item
    }

    override fun updateItem(id: Long, itemUpdated: Item): Boolean {
        val item = itemsRepository.findById(id).orElseThrow { ItemNotFoundException("Item not found") }
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