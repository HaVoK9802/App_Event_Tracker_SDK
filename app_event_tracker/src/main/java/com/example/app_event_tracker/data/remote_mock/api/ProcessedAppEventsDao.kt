package com.example.app_event_tracker.data.remote_mock.api

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import com.example.app_event_tracker.data.remote_mock.dto.ProcessedAppEvent
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ProcessedAppEventsDao {


    @Insert
    fun eventIngestion(event: ProcessedAppEvent)

    @Query("SELECT * FROM remote_mock_app_events ORDER BY timestamp ASC")
    fun getAllProcessedEvents(): Flow<List<ProcessedAppEvent>>

    @Query("SELECT * FROM remote_mock_app_events WHERE id = :id LIMIT 1")
    suspend fun getEventById(id: String): ProcessedAppEvent?
}