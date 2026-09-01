package com.example.app_event_tracker.data.local

import android.content.Context
import androidx.room3.Room
import kotlin.jvm.java

internal object LocalAppEventsDatabaseClient {

    private var INSTANCE: LocalAppEventsDatabase? = null

    fun getDatabase(context: Context): LocalAppEventsDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                LocalAppEventsDatabase::class.java,
                "local_app_events_queue_database"
            ).build()

            INSTANCE = instance
            instance
        }
    }

    fun initDatabase(context: Context) {
        getDatabase(context)
    }
}