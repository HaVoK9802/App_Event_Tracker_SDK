package com.example.app_event_tracker.data

import com.example.app_event_tracker.domain.models.AppEventType
import kotlinx.serialization.SerializationException


public class MissingAppEventType: IllegalArgumentException("event_type key in json data is missing")

public class MissingData(key: String, appEventType: AppEventType) : IllegalArgumentException("$key in json data is missing for ${appEventType.name} event")

public class UnknownAppEvent : IllegalArgumentException("Unknown event_type was sent in app event JSON")

public class InvalidEventJson : SerializationException("Event data contains invalid JSON")