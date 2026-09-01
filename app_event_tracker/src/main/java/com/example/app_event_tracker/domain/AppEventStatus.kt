package com.example.app_event_tracker.domain

public sealed class AppEventStatus{
    public data class Succeeded(val appEvent: AppEvent) : AppEventStatus()
    public data class InProgress(val appEvent: AppEvent) : AppEventStatus()
    public data class Failed(val error: Throwable, val appEvent: AppEvent) : AppEventStatus()
    public data class Retrying(val retryCount: Int, val appEvent: AppEvent) : AppEventStatus()
}

public enum class AppEventStatusType {
    SUCCEEDED,
    IN_PROGRESS,
    FAILED,
    RETRYING
}