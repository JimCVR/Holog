package com.reactorsolutions.holog.utils.mapper.category

import com.reactorsolutions.holog.dto.ResponseCategoryDTO
import com.reactorsolutions.holog.models.Category
import com.reactorsolutions.holog.utils.mapper.Mapper
import org.springframework.stereotype.Component

@Component
class CategoryToResponse : Mapper<Category, ResponseCategoryDTO> {
    override fun transform(entity: Category): ResponseCategoryDTO = ResponseCategoryDTO(
        entity.id!!,
        entity.name,
        entity.iconId
    )
}