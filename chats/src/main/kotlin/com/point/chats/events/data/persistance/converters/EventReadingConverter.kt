package com.point.chats.events.data.persistance.converters

import com.point.chats.events.data.persistance.entities.BaseEvent
import com.point.chats.events.data.persistance.entities.ChatCreatedEvent
import com.point.chats.events.data.persistance.entities.MessageEvent
import org.bson.Document
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import java.util.*

@ReadingConverter
class EventReadingConverter : Converter<Document, BaseEvent> {

    override fun convert(source: Document) = when (source.getString("_class")) {
        "message" -> MessageEvent(
            id = source.getString("_id"),
            timestamp = source.get("timestamp", Date::class.java).toInstant(),
            senderId = source.getString("senderId"),
            content = source.getString("content"),
            isPinned = source.getBoolean("isPinned"),
            isEdited = source.getBoolean("isEdited"),
            attachments = source.getList("attachments", Any::class.java)
                ?.map { (it as Number).toLong() }
                ?.toMutableList()
                ?: mutableListOf(),
        )
        "created" -> {
            ChatCreatedEvent(
                id = source.getString("_id"),
                timestamp = source.get("timestamp", Date::class.java).toInstant(),
            )
        }

        else -> throw IllegalArgumentException("Unknown Event type: ${source.getString("type")}")
    }
}



