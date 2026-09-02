package com.example.app_event_tracker.data.repository

import android.util.Log
import com.example.app_event_tracker.AppEventTracker
import com.example.app_event_tracker.JsonHelper
import com.example.app_event_tracker.data.MissingData
import com.example.app_event_tracker.data.UnknownAppEvent
import com.example.app_event_tracker.data.local.LocalAppEventsDatabase
import com.example.app_event_tracker.data.local.LocalAppEventsDatabaseClient
import com.example.app_event_tracker.data.local.mappers.toAppEventWithStatus
import com.example.app_event_tracker.data.remote_mock.AppEventsDatabase
import com.example.app_event_tracker.data.remote_mock.AppEventsDatabaseClient
import com.example.app_event_tracker.data.remote_mock.mappers.toAppEvent
import com.example.app_event_tracker.domain.models.AppEvent
import com.example.app_event_tracker.domain.models.AppEventType
import com.example.app_event_tracker.domain.models.AppEventWithStatus
import com.example.app_event_tracker.domain.repository.AppEventsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.jsonPrimitive

internal class AppEventsRepoImpl(
    private val localDataSource: LocalAppEventsDatabase = LocalAppEventsDatabaseClient.getDatabase(
        AppEventTracker.getInstance().applicationContext
    ),
    private val remoteDataSource: AppEventsDatabase = AppEventsDatabaseClient.getDatabase(
        AppEventTracker.getInstance().applicationContext
    )
) : AppEventsRepo {

    override suspend fun trackEvent(appEvent: AppEvent) {
        try {
            require(appEvent.appEventType !is AppEventType.Unknown) {
                when (appEvent.appEventType) {
                    is AppEventType.StrictlyOnceEvent -> {

                        val unprocessedEvents =
                            localDataSource.localAppEventTrackerDao().getUnprocessedEvents()
                                .first().map { it.toAppEventWithStatus().appEvent }
                        val processedEvents =
                            remoteDataSource.appEventsDao().getAllProcessedEvents().first()
                                .map { it.toAppEvent() }
                        val allEvents = unprocessedEvents + processedEvents

                        when(appEvent.appEventType){
                            is AppEventType.StrictlyOnceEvent.Install -> {
                                val installEvent = allEvents.find {
                                    JsonHelper.getJsonObject(appEvent.data)["event_type"]!!.jsonPrimitive.content == appEvent.appEventType.name
                                }
                                if(installEvent==null){

                                } else {
                                    Log.w("AppEvent", "Duplicate app install event, should be strictly once")
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
                            remoteDataSource.appEventsDao().getAllProcessedEvents().first()
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
                                allEvents.forEach {
                                    val visitedScreenName =
                                        JsonHelper.getJsonObject(it.data)["screen_name"]!!.jsonPrimitive.content
                                    if (it.sessionId == sessionId && screenName == visitedScreenName) {
                                        isDuplicateEvent = true
                                    }
                                }
                            }
                        }
                        if (!isDuplicateEvent) {


                        } else {
                            Log.w("AppEvent", "Duplicate app event, should be once per session")
                        }
                    }

                    is AppEventType.MultipleEvent -> {
                        when (appEvent.appEventType) {
                            is AppEventType.MultipleEvent.AddToCart, AppEventType.MultipleEvent.Purchase -> {
                                val itemId =
                                    JsonHelper.getJsonObject(appEvent.data)["item_id"]?.jsonPrimitive?.content
                                        ?: run {
                                            throw MissingData("item_id", appEvent.appEventType)
                                        }
                                val processedEvent =
                                    localDataSource.localAppEventTrackerDao().getEventById(itemId)
                                val unprocessedEvent =
                                    remoteDataSource.appEventsDao().getEventById(itemId)
                                if (processedEvent == null && unprocessedEvent == null) {

                                } else {
                                    Log.w("AppEvent", "Duplicate app event, has same event id")
                                }
                            }
                        }
                    }

                    is AppEventType.Unknown -> return
                }
            }
        } catch (_: IllegalArgumentException) {
            throw UnknownAppEvent()
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
        return localDataSource.localAppEventTrackerDao().getUnprocessedEvents().map {
            it.map { entity ->
                entity.toAppEventWithStatus()
            }
        }
    }
}