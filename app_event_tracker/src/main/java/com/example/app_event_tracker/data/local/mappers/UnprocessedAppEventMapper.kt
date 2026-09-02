package com.example.app_event_tracker.data.local.mappers

import com.example.app_event_tracker.data.local.entity.UnprocessedAppEvent
import com.example.app_event_tracker.domain.models.AppEvent
import com.example.app_event_tracker.domain.models.AppEventType
import com.example.app_event_tracker.domain.models.AppEventWithStatus

internal fun UnprocessedAppEvent.toAppEventWithStatus(): AppEventWithStatus {
    return AppEventWithStatus(
        appEvent = AppEvent(
            id = id,
            appEventType = AppEventType.fromString(appEventType),
            sessionId = sessionId,
            data = data,
            timestamp = timestamp
        ),
        uploadStatus = status
    )
}

internal fun AppEventWithStatus.toUnprocessedAppEvent(): UnprocessedAppEvent {
    return UnprocessedAppEvent(
        id = appEvent.id,
        appEventType = appEvent.appEventType.name,
        sessionId = appEvent.sessionId,
        data = appEvent.data,
        timestamp = appEvent.timestamp,
        status = uploadStatus
    )
}