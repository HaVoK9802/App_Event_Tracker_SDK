package com.example.app_event_tracker

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.parseToJsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

internal object JsonHelper {

    fun getJsonObject(json: String): JsonObject {
        return parseToJsonElement(json).jsonObject
    }
}