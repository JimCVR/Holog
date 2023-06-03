package com.reactorsolutions.holog.utils.mapper.item

import com.reactorsolutions.holog.dto.ResponseItemDTO
import com.reactorsolutions.holog.models.Item
import com.reactorsolutions.holog.utils.mapper.Mapper
import org.springframework.stereotype.Component

@Component
class ItemToResponse : Mapper<Item, ResponseItemDTO> {
    override fun transform(entity: Item): ResponseItemDTO = ResponseItemDTO(
        entity.id!!,
        entity.name,
        entity.description,
        entity.picture,
        entity.score,
        entity.date,
        entity.status,
        entity.custom,
        entity.category!!.id!!
    )
}