package com.reactorsolutions.holog.exceptions.item

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code= HttpStatus.NOT_FOUND, reason = "Item Not Found")
class ItemNotFoundException(message: String) : Exception(message) {

}
