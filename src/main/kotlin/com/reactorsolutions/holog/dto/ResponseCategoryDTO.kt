package com.reactorsolutions.holog.dto

data class ResponseCategoryDTO(
    val id: Long,
    val name: String,
    val userId: String?,
    val iconId: Int?=null
)