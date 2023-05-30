package com.reactorsolutions.holog.dto

import java.sql.Date

data class ResponseItemDTO(
    val id: Long,
    val name: String,
    val description: String? = null,
    val author: String? = null,
    val picture: String?,
    val score: Double?,
    val date: Date?,
    val status: String?,
    val custom:Boolean?,
    val categoryId: Long
)
