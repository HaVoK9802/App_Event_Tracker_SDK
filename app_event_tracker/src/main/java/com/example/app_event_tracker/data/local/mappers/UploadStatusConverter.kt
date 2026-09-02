package com.example.app_event_tracker.data.local.mappers

import androidx.room3.ColumnTypeConverter
import com.example.app_event_tracker.domain.models.UploadStatus

internal class UploadStatusConverter {
    @ColumnTypeConverter
    fun fromState(state: UploadStatus): String = state.name

    @ColumnTypeConverter
    fun toState(value: String): UploadStatus = UploadStatus.valueOf(value)
}