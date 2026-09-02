package com.example.app_event_tracker

import com.example.app_event_tracker.data.InvalidEventJson
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json.Default.parseToJsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

internal object JsonHelper {

    fun getJsonObject(json: String): JsonObject {
        return try {
            parseToJsonElement(json).jsonObject
        } catch (_: SerializationException) {
            throw InvalidEventJson()
        }
    }
}