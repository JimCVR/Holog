package com.reactorsolutions.holog.services.impl

import com.reactorsolutions.holog.commons.GenericServiceImpl
import com.reactorsolutions.holog.models.Item
import com.reactorsolutions.holog.repositories.ItemsRepository
import com.reactorsolutions.holog.services.api.ItemsServiceAPI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service

@Service
class ItemsServiceImpl : ItemsServiceAPI, GenericServiceImpl<Item, Long>(){
    @Autowired
    lateinit var itemsRepository: ItemsRepository

    override val dao: CrudRepository<Item, Long>
        get() = itemsRepository
}