package com.point.chats.events.data.converters

import com.point.chats.chatsv2.data.entity.event.BaseEvent
import com.point.chats.chatsv2.data.entity.event.MessageSentEvent
import org.bson.Document
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import java.util.*

@ReadingConverter
class EventReadingConverter : Converter<Document, BaseEvent> {

    override fun convert(source: Document) = when (source.getString("_class")) {
        "message" -> MessageSentEvent(
            id = source.getString("_id"),
            timestamp = source.get("timestamp", Date::class.java).toInstant(),
            senderId = source.getString("senderId"),
            text = source.getString("text"),
            attachments = source.getList("attachments", Any::class.java)
                ?.map { (it as Number).toLong() }
                ?.toMutableList()
                ?: mutableListOf(),
        )

        else -> throw IllegalArgumentException("Unknown Event type: ${source.getString("type")}")
    }
}



