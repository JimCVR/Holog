package com.reactorsolutions.holog.dto

import jakarta.persistence.Column

data class CategoryDTO(
    var id: Long,
    var name: String,
    var iconId: Int
) {

}