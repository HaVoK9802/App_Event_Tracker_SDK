package com.example.app_event_tracker.data.local.entity

import androidx.room3.Embedded
import androidx.room3.ColumnTypeConverters
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.example.app_event_tracker.data.local.mappers.UploadStatusConverter
import com.example.app_event_tracker.domain.models.AppEventUploadStatus

@Entity(tableName = "unprocessed_app_events")
@ColumnTypeConverters(UploadStatusConverter::class)
internal data class UnprocessedAppEvent(
    @PrimaryKey val id: String,
    val appEventType: String,
    val sessionId: String,
    val data: String,
    val timestamp: Long,
    @Embedded(prefix = "current_") val status: AppEventUploadStatus
)