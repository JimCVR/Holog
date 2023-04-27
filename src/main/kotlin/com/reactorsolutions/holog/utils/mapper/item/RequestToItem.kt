package com.reactorsolutions.holog.utils.mapper.item

import com.reactorsolutions.holog.dto.RequestItemDTO
import com.reactorsolutions.holog.models.Category
import com.reactorsolutions.holog.models.Item
import com.reactorsolutions.holog.repositories.CategoriesRepository
import com.reactorsolutions.holog.utils.mapper.Mapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class RequestToItem : Mapper<RequestItemDTO, Item> {

    @Autowired
    lateinit var categoryRepository: CategoriesRepository

    override fun transform(domain: RequestItemDTO): Item = Item(
        domain.name,
        mutableSetOf<Category>(),
        domain.description,
        domain.author
    )
}