package com.example.app_event_tracker.domain.repository

import com.example.app_event_tracker.domain.models.AppEvent
import com.example.app_event_tracker.domain.models.AppEventWithStatus
import kotlinx.coroutines.flow.Flow

internal interface AppEventsRepo {

    suspend fun trackEvent(appEvent: AppEvent)

    suspend fun getProcessedEventList(): Flow<List<AppEvent>>

    suspend fun getUnprocessedEventList(): Flow<List<AppEventWithStatus>>
}