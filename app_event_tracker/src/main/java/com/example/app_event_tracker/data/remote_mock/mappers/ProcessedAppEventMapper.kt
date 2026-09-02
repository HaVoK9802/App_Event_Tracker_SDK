package com.example.app_event_tracker.data.remote_mock.mappers

import com.example.app_event_tracker.data.remote_mock.dto.ProcessedAppEvent
import com.example.app_event_tracker.domain.models.AppEvent
import com.example.app_event_tracker.domain.models.AppEventType

internal fun ProcessedAppEvent.toAppEvent(): AppEvent {
    return AppEvent(
        id = id,
        appEventType = AppEventType.fromString(appEventType),
        sessionId = sessionId,
        data = data,
        timestamp = timestamp
    )
}

internal fun AppEvent.toProcessedAppEvent(): ProcessedAppEvent {
    return ProcessedAppEvent(
        id = id,
        appEventType = appEventType.name,
        sessionId = sessionId,
        data = data,
        timestamp = timestamp
    )
}