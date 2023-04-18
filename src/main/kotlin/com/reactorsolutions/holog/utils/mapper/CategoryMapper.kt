package com.reactorsolutions.holog.utils.mapper

import com.reactorsolutions.holog.dto.CategoryDTO
import com.reactorsolutions.holog.models.Category

class CategoryMapper : Mapper <CategoryDTO,Category> {
    override fun fromEntity(entity: Category): CategoryDTO = CategoryDTO(
        entity.id,
        entity.name,
        entity.iconId
    )

    override fun toEntity(domain: CategoryDTO): Category = Category(
        domain.id,
        domain.name,
        domain.iconId
    )

}