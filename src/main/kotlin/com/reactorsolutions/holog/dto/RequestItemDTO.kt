package com.reactorsolutions.holog.dto

import java.sql.Date

data class RequestItemDTO(
    val name: String,
    val description: String?,
    val picture: String?,
    val score: Double?,
    val date: Date?,
    val status: String?,
    val custom:Boolean?,
    val categoryId: Long
)