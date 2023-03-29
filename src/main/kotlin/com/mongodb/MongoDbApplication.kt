package com.mongodb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(
	exclude = [
		DataSourceAutoConfiguration::class,
		XADataSourceAutoConfiguration::class
	]
)
class MongoDbApplication

fun main(args: Array<String>) {
	runApplication<MongoDbApplication>(*args)
}
