package com.example.app_event_tracker.domain.models


public data class AppEventUploadStatus(
    val uploadStatus: UploadStatus,
    val attempt: Int = 0,
    val retryingInterval: Long = 0L
)
public enum class UploadStatus {
    PROCESSED,
    PROCESSING,
    FAILED,
    RETRYING
}