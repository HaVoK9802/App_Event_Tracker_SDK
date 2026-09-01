package com.example.app_event_tracker.domain.usecase

import com.example.app_event_tracker.domain.models.AppEventWithStatus
import com.example.app_event_tracker.domain.repository.AppEventsRepo
import kotlinx.coroutines.flow.Flow

internal class GetUnprocessedEvents(
    val appEventsRepo: AppEventsRepo
){
    suspend operator fun invoke(): Flow<List<AppEventWithStatus>> {
        return appEventsRepo.getUnprocessedEventList()
    }
}