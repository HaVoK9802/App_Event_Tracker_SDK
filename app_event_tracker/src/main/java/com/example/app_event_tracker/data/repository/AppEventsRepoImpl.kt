package com.example.app_event_tracker.data.repository

import com.example.app_event_tracker.AppEventTracker
import com.example.app_event_tracker.data.local.LocalAppEventsDatabase
import com.example.app_event_tracker.data.local.LocalAppEventsDatabaseClient
import com.example.app_event_tracker.data.local.mappers.toAppEventWithStatus
import com.example.app_event_tracker.data.remote_mock.AppEventsDatabase
import com.example.app_event_tracker.data.remote_mock.AppEventsDatabaseClient
import com.example.app_event_tracker.data.remote_mock.mappers.toAppEvent
import com.example.app_event_tracker.domain.models.AppEvent
import com.example.app_event_tracker.domain.models.AppEventTriggerRepetitionType
import com.example.app_event_tracker.domain.models.AppEventType
import com.example.app_event_tracker.domain.models.AppEventWithStatus
import com.example.app_event_tracker.domain.repository.AppEventsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class AppEventsRepoImpl(
    private val localDataSource: LocalAppEventsDatabase = LocalAppEventsDatabaseClient.getDatabase(
        AppEventTracker.getInstance().applicationContext
    ),
    private val remoteDataSource: AppEventsDatabase = AppEventsDatabaseClient.getDatabase(
        AppEventTracker.getInstance().applicationContext
    )
): AppEventsRepo {
    override suspend fun trackEvent(appEvent: AppEvent) {
        require(appEvent.appEventType != AppEventType.UNKNOWN){
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

    override suspend fun getProcessedEventList(): Flow<List<AppEvent>> {
        return remoteDataSource.appEventsDao().getAllProcessedEvents().map {
            it.map { entity ->
                entity.toAppEvent()
            }
        }
    }

    override suspend fun getUnprocessedEventList(): Flow<List<AppEventWithStatus>> {
        return localDataSource.localAppEventTrackerDao().getUnProcessedEvents().map {
            it.map { entity ->
                entity.toAppEventWithStatus()
            }
        }
    }
}