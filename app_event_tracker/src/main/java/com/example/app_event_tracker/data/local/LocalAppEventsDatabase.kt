package com.example.app_event_tracker.data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.app_event_tracker.data.local.dao.LocalAppEventTrackerDao
import com.example.app_event_tracker.data.local.entity.UnprocessedAppEvent


@Database(
    entities = [UnprocessedAppEvent::class],
    version = 1,
    exportSchema = false
)
internal abstract class LocalAppEventsDatabase : RoomDatabase() {

    abstract fun localAppEventTrackerDao(): LocalAppEventTrackerDao


}