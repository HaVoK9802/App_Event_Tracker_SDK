package com.example.app_event_tracker.domain.models

public data class AppEvent(
    val appEventType: AppEventType,
    val sessionId: String,
    val data: String?,
    val timestamp: Long,
)