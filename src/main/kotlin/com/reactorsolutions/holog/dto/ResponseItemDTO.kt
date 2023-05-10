package com.reactorsolutions.holog.dto

data class ResponseItemDTO (
    val id: Long,
    val name: String,
    val description: String?=null,
    val author: String?=null,
    //val categories_id: MutableList<Long>
)
