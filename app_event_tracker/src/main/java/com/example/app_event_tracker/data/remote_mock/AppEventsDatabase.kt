package com.example.app_event_tracker.data.remote_mock

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.app_event_tracker.data.remote_mock.dao.ProcessedAppEventsDao
import com.example.app_event_tracker.data.remote_mock.dto.ProcessedAppEvent


@Database(
    entities = [ProcessedAppEvent::class],
    version = 1,
    exportSchema = false
)
internal abstract class AppEventsDatabase : RoomDatabase() {

    abstract fun appEventsDao(): ProcessedAppEventsDao

}