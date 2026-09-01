package com.example.app_event_tracker.data.local.mappers

import com.example.app_event_tracker.data.local.entity.UnprocessedAppEvent
import com.example.app_event_tracker.domain.models.AppEvent
import com.example.app_event_tracker.domain.models.AppEventType
import com.example.app_event_tracker.domain.models.AppEventWithStatus

internal fun UnprocessedAppEvent.toAppEventWithStatus(): AppEventWithStatus {
    return AppEventWithStatus(
        appEvent = AppEvent(
            appEventType = AppEventType.valueOf(appEventType),
            sessionId = sessionId,
            data = data,
            timestamp = timestamp
        ),
        uploadStatus = status
    )
}