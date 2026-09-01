package com.example.app_event_tracker.data.local.mappers

import androidx.room.TypeConverter
import com.example.app_event_tracker.domain.models.UploadStatus

internal class UploadStatusConverter {
    @TypeConverter
    fun fromState(state: UploadStatus): String = state.name

    @TypeConverter
    fun toState(value: String): UploadStatus = UploadStatus.valueOf(value)
}