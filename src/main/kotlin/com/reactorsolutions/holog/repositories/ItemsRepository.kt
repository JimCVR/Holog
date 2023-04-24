package com.reactorsolutions.holog.repositories

import com.reactorsolutions.holog.models.Item
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemsRepository : CrudRepository<Item, Long> {
}