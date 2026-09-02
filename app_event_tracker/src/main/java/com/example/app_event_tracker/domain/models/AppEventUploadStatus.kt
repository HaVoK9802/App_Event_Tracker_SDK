package com.example.app_event_tracker.domain.models


public data class AppEventUploadStatus(
    val uploadStatus: UploadStatus,
    val retryAttempt: Int = 0,
    val retryAt: Long = 0L
)
public enum class UploadStatus {
    QUEUED,
    PROCESSING,
    RETRYING,
    FAILED
}