package com.reactorsolutions.holog.dto

import jakarta.persistence.Column

data class ItemDTO(
    var id: Long,
    var name: String,
    var description: String,
    var author: String,
) {
}