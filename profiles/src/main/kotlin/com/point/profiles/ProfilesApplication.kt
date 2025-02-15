package com.point.profiles

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ProfilesApplication

fun main(args: Array<String>) {
    runApplication<ProfilesApplication>(*args)
}
