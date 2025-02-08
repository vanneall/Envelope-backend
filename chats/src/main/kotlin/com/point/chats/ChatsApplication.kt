package com.point.chats

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ChatsApplication

fun main(args: Array<String>) {
	runApplication<ChatsApplication>(*args)
}
