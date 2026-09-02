package com.example.app_event_tracker.domain.models


public data class AppEventWithStatus(
    val appEvent: AppEvent,
    val uploadStatus: AppEventUploadStatus,
)