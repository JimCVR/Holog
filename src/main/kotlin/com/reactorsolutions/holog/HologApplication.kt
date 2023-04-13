package com.reactorsolutions.holog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HologApplication

fun main(args: Array<String>) {
	runApplication<HologApplication>(*args)
}
