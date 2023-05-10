package com.reactorsolutions.holog.dto

data class RequestItemDTO(
    val name: String,
    val description: String?,
    val author: String?,
    val categoriesId: MutableList<Long>
)