package com.example.app_event_tracker.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.app_event_tracker.data.local.entity.UnprocessedAppEvent
import kotlinx.coroutines.flow.Flow

@Dao
internal interface LocalAppEventTrackerDao {

    @Insert
    suspend fun logEvent(unprocessedAppEvent: UnprocessedAppEvent)

    @Update
    suspend fun updateEvent(unprocessedAppEvent: UnprocessedAppEvent)

    @Query("SELECT * FROM unprocessed_app_events WHERE current_uploadStatus = 'FAILED' ORDER BY timestamp ASC")
    suspend fun getFailedEvents(): List<UnprocessedAppEvent>

    @Query("SELECT * FROM unprocessed_app_events WHERE current_uploadStatus != 'PROCESSED' ORDER BY timestamp ASC")
    suspend fun getUnProcessedEvents(): Flow<List<UnprocessedAppEvent>>

}