package com.reactorsolutions.holog.exceptions.category

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code= HttpStatus.NOT_FOUND, reason = "Category Not Found")
class CategoryNotFoundException(message:String) : Exception(message) {
}