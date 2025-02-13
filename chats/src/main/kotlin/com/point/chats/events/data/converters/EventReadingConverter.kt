package com.point.chats.events.data.converters

import com.point.chats.common.data.entities.Event
import com.point.chats.events.data.entities.Message
import com.point.chats.events.data.entities.Notification
import org.bson.Document
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter

@ReadingConverter
class EventReadingConverter : Converter<Document, Event> {
    override fun convert(source: Document): Event {
        return when (source.getString("_class")) {
            "message" -> Message(
                senderId = source.getString("senderId"),
                content = source.getString("content")
            )

            "notification" -> Notification(
                text = source.getString("text"),
                type = Notification.Type.valueOf(source.getString("type")),
            )

            else -> throw IllegalArgumentException("Unknown Event type: ${source.getString("type")}")
        }
    }
}



