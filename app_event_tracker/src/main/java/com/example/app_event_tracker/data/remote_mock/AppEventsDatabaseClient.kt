package com.example.app_event_tracker.data.remote_mock

import android.content.Context
import androidx.room3.Room
import kotlin.jvm.java

internal object AppEventsDatabaseClient {

    private var INSTANCE: AppEventsDatabase? = null

    fun getDatabase(context: Context): AppEventsDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppEventsDatabase::class.java,
                "mock_server_successful_app_events"
            ).build()

            INSTANCE = instance
            instance
        }
    }

    fun initDatabase(context: Context) {
        getDatabase(context)
    }
}