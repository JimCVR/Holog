package com.reactorsolutions.holog.utils.mapper

interface Mapper<F, T> {

    fun transform(entity: F): T
}