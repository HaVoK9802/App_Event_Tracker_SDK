package com.example.app_event_tracker.data.remote_mock.dao

import androidx.room3.Dao
import androidx.room3.Query
import com.example.app_event_tracker.data.remote_mock.dto.ProcessedAppEvent
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ProcessedAppEventsDao {

    @Query("SELECT * FROM remote_mock_app_events ORDER BY timestamp ASC")
    suspend fun getAllProcessedEvents(): Flow<List<ProcessedAppEvent>>

}