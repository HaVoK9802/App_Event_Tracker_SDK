package com.example.app_event_tracker.data.repository

import com.example.app_event_tracker.domain.AppEvent
import com.example.app_event_tracker.domain.AppEventTriggerRepetitionType
import com.example.app_event_tracker.domain.AppEventType
import com.example.app_event_tracker.domain.repository.AppEventsRepo
import kotlinx.coroutines.flow.Flow

internal class AppEventsRepoImpl(

): AppEventsRepo {
    override suspend fun trackEvent(appEvent: AppEvent) {
        require(appEvent.appEventType != AppEventType.UNKNOWN){
            //TODO: ADD DELAY RANDOM(0-5) SECONDS
            //TODO: 20% chance For the event to FAIL

            when(appEvent.appEventType.repetition){
                AppEventTriggerRepetitionType.STRICTLY_ONCE -> {

                }
                AppEventTriggerRepetitionType.ONCE_PER_SESSION -> {

                }
                AppEventTriggerRepetitionType.MULTIPLE -> {

                }
            }
        }
    }

    override suspend fun getEventList(): Flow<List<AppEvent>> {
        TODO("Not yet implemented")
    }
}