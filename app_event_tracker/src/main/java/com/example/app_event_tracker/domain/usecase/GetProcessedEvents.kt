package com.example.app_event_tracker.domain.usecase

import com.example.app_event_tracker.domain.models.AppEvent
import com.example.app_event_tracker.domain.repository.AppEventsRepo
import kotlinx.coroutines.flow.Flow

internal class GetProcessedEvents(
    val appEventsRepo: AppEventsRepo
){
    suspend operator fun invoke(): Flow<List<AppEvent>> {
        return appEventsRepo.getProcessedEventList()
    }
}