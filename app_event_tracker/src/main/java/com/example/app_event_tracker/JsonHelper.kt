package com.example.app_event_tracker

import com.example.app_event_tracker.data.InvalidEventJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json.Default.parseToJsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

internal object JsonHelper {

    suspend fun getJsonObject(json: String): JsonObject {
        return try {
            withContext(Dispatchers.Default) {
                parseToJsonElement(json).jsonObject
            }
        } catch (_: SerializationException) {
            throw InvalidEventJson()
        }
    }
}