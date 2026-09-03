package com.example.app_event_tracker.data.repository

import android.content.Context
import android.util.Log
import com.example.app_event_tracker.AppEventTracker
import com.example.app_event_tracker.JsonHelper
import com.example.app_event_tracker.data.MissingData
import com.example.app_event_tracker.data.UnknownAppEvent
import com.example.app_event_tracker.data.local.LocalAppEventsDatabase
import com.example.app_event_tracker.data.local.LocalAppEventsDatabaseClient
import com.example.app_event_tracker.data.local.mappers.toAppEventWithStatus
import com.example.app_event_tracker.data.local.mappers.toUnprocessedAppEvent
import com.example.app_event_tracker.data.remote_mock.api.RemoteDataSource
import com.example.app_event_tracker.data.remote_mock.api.impl.RemoteDataSourceImpl
import com.example.app_event_tracker.data.remote_mock.mappers.toAppEvent
import com.example.app_event_tracker.domain.models.AppEvent
import com.example.app_event_tracker.domain.models.AppEventType
import com.example.app_event_tracker.domain.models.AppEventUploadStatus
import com.example.app_event_tracker.domain.models.AppEventWithStatus
import com.example.app_event_tracker.domain.models.UploadStatus
import com.example.app_event_tracker.domain.repository.AppEventsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.jsonPrimitive

internal class AppEventsRepoImpl(
    context: Context
) : AppEventsRepo {

    private val localDataSource: LocalAppEventsDatabase = LocalAppEventsDatabaseClient.getDatabase(context.applicationContext)
    private val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl(context)

    override suspend fun trackEvent(appEvent: AppEvent) {
        try {
            var canQueueEvent = false

            when (appEvent.appEventType) {
                is AppEventType.StrictlyOnceEvent -> {
                    val unprocessedEvents =
                        localDataSource.localAppEventTrackerDao().getUnprocessedEvents()
                            .first().map { it.toAppEventWithStatus().appEvent }
                    val processedEvents =
                        remoteDataSource.getProcessedAppEventsDataInterface()
                            .getAllProcessedEvents().first()
                            .map { it.toAppEvent() }
                    val allEvents = unprocessedEvents + processedEvents

                    when (appEvent.appEventType) {
                        is AppEventType.StrictlyOnceEvent.Install -> {
                            val installEvent = allEvents.find {
                                it.appEventType is AppEventType.StrictlyOnceEvent.Install
                            }
                            if (installEvent == null) {
                                canQueueEvent = true
                            } else {
                                Log.w(
                                    "AppEvent",
                                    "Duplicate app install event, should be strictly once"
                                )
                            }
                        }
                    }
                }

                is AppEventType.OncePerSessionEvent -> {
                    var isDuplicateEvent = false
                    val unprocessedEvents =
                        localDataSource.localAppEventTrackerDao().getUnprocessedEvents()
                            .first().map { it.toAppEventWithStatus().appEvent }
                    val processedEvents =
                        remoteDataSource.getProcessedAppEventsDataInterface()
                            .getAllProcessedEvents().first()
                            .map { it.toAppEvent() }
                    val allEvents = unprocessedEvents + processedEvents
                    when (appEvent.appEventType) {
                        is AppEventType.OncePerSessionEvent.Visit -> {
                            val sessionId =
                                AppEventTracker.getInstance().sessionManager?.getSessionId()
                            val screenName =
                                JsonHelper.getJsonObject(appEvent.data)["screen_name"]?.jsonPrimitive?.content
                                    ?: run {
                                        throw MissingData("screen_name", appEvent.appEventType)
                                    }
                            allEvents.filter {
                                it.appEventType is AppEventType.OncePerSessionEvent.Visit
                            }.forEach {
                                val visitedScreenName =
                                    JsonHelper.getJsonObject(it.data)["screen_name"]!!.jsonPrimitive.content
                                if (it.sessionId == sessionId && screenName == visitedScreenName) {
                                    isDuplicateEvent = true
                                }
                            }
                        }
                    }
                    if (!isDuplicateEvent) {
                        canQueueEvent = true
                    } else {
                        Log.w("AppEvent", "Duplicate app event, should be once per session")
                    }
                }

                is AppEventType.MultipleEvent -> {
                    canQueueEvent = true
                }

                is AppEventType.Unknown -> {
                    return
                }
            }
            if(canQueueEvent) {
                localDataSource.localAppEventTrackerDao().queueEvent(
                    AppEventWithStatus(
                        appEvent = appEvent,
                        uploadStatus = AppEventUploadStatus(
                            UploadStatus.QUEUED
                        )
                    ).toUnprocessedAppEvent()
                )
                AppEventTracker.getInstance().eventUploadScheduler!!.schedule(appEvent.id)
            }
        } catch (_: IllegalArgumentException) {
            throw UnknownAppEvent()
        }

    }

    override suspend fun getProcessedEventList(): Flow<List<AppEvent>> {
        return  remoteDataSource.getProcessedAppEventsDataInterface().getAllProcessedEvents().map {
            it.map { entity ->
                entity.toAppEvent()
            }
        }
    }

    override suspend fun getUnprocessedEventList(): Flow<List<AppEventWithStatus>> {
        return localDataSource.localAppEventTrackerDao().getUnprocessedEvents().map {
            it.map { entity ->
                entity.toAppEventWithStatus()
            }
        }
    }
}