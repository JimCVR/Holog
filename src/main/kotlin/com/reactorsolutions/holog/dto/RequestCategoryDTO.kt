package com.reactorsolutions.holog.dto

import com.reactorsolutions.holog.models.Item

data class RequestCategoryDTO(
    val name: String,
    val iconId: Int?=null
)