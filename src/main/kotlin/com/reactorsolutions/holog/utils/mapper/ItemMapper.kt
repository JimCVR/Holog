package com.reactorsolutions.holog.utils.mapper

import com.reactorsolutions.holog.dto.ItemDTO
import com.reactorsolutions.holog.models.Item

class ItemMapper: Mapper<ItemDTO, Item> {
    override fun fromEntity(entity: Item): ItemDTO = ItemDTO(
        entity.id,
        entity.name,
        entity.description,
        entity.author
    )

    override fun toEntity(domain: ItemDTO): Item = Item (
        domain.id,
        domain.name,
        domain.description,
        domain.author
    )
}