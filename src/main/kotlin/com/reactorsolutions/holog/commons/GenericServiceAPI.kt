package com.reactorsolutions.holog.commons

interface GenericServiceAPI <T,ID> {
    fun save(entity:T):T
    fun delete(id:ID)
    operator fun get(id:ID):T?
    val all:MutableList<T>
}