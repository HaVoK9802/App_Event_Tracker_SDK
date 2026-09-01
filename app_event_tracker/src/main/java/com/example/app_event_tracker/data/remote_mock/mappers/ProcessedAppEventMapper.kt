package com.example.app_event_tracker.data.remote_mock.mappers

import com.example.app_event_tracker.data.remote_mock.dto.ProcessedAppEvent
import com.example.app_event_tracker.domain.models.AppEvent
import com.example.app_event_tracker.domain.models.AppEventType

internal fun ProcessedAppEvent.toAppEvent(): AppEvent {
    return AppEvent(
        appEventType = AppEventType.valueOf(appEventType),
        sessionId = sessionId,
        data = data,
        timestamp = timestamp
    )
}