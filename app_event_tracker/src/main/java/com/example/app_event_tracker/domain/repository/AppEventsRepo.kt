package com.example.app_event_tracker.domain.repository

import com.example.app_event_tracker.domain.AppEvent
import kotlinx.coroutines.flow.Flow

internal interface AppEventsRepo {

    suspend fun trackEvent(appEvent: AppEvent)

    suspend fun getEventList(): Flow<List<AppEvent>>
}