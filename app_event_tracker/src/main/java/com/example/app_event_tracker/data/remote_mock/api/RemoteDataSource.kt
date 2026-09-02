package com.example.app_event_tracker.data.remote_mock.api

import androidx.work.WorkRequest
import com.example.app_event_tracker.data.local.entity.UnprocessedAppEvent
import com.example.app_event_tracker.domain.usecase.GetUnprocessedEvents

internal interface RemoteDataSource {


    suspend fun uploadEvent(unprocessedEvent: UnprocessedAppEvent): Boolean



    fun getProcessedAppEventsDataInterface(): ProcessedAppEventsDao

    suspend fun eventProcessingSimulation(): Boolean


}