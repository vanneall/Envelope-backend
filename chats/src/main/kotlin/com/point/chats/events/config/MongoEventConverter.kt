package com.point.chats.events.config

import com.point.chats.events.data.converters.EventReadingConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.mongodb.core.convert.MongoCustomConversions

@Configuration
class MongoEventConverter {
    @Bean
    fun mongoCustomConversions(): MongoCustomConversions {
        val converterList: MutableList<Converter<*, *>> = ArrayList()
        converterList.add(EventReadingConverter())
        return MongoCustomConversions(converterList)
    }
}