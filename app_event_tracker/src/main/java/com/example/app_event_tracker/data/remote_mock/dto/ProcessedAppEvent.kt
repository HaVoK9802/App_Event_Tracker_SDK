package com.example.app_event_tracker.data.remote_mock.dto

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "remote_mock_app_events")
internal data class ProcessedAppEvent(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val appEventType: String,
    val sessionId: String,
    val data: String?,
    val timestamp: Long
)


