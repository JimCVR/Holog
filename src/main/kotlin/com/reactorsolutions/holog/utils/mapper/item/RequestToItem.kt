package com.reactorsolutions.holog.utils.mapper.item

import com.reactorsolutions.holog.dto.RequestItemDTO
import com.reactorsolutions.holog.models.Item
import com.reactorsolutions.holog.utils.mapper.Mapper
import org.springframework.stereotype.Component

@Component
class RequestToItem : Mapper<RequestItemDTO, Item> {

    override fun transform(domain: RequestItemDTO): Item = Item(
        domain.name,
        domain.description,
        domain.author
    )
}