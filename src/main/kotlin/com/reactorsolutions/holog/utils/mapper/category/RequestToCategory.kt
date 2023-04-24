package com.reactorsolutions.holog.utils.mapper.category

import com.reactorsolutions.holog.dto.RequestCategoryDTO
import com.reactorsolutions.holog.models.Category
import com.reactorsolutions.holog.utils.mapper.Mapper
import org.springframework.stereotype.Component

@Component
class RequestToCategory : Mapper<RequestCategoryDTO, Category> {

    override fun transform(domain: RequestCategoryDTO): Category = Category(
        domain.name,
        domain.iconId
    )

}