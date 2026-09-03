package com.example.app_event_tracker.domain.usecase

import com.example.app_event_tracker.JsonHelper
import com.example.app_event_tracker.data.MissingAppEventType
import com.example.app_event_tracker.domain.models.AppEvent
import com.example.app_event_tracker.domain.models.AppEventType
import com.example.app_event_tracker.domain.repository.AppEventsRepo
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.jsonPrimitive
import java.util.UUID

internal class TrackEvent(
    val appEventsRepo: AppEventsRepo
) {
    suspend operator fun invoke(
        json: String,
        sessionId: String,
        timestamp: Long
    ) {

        val appEventType = getAppEventTypeFromJson(json)
        appEventsRepo.trackEvent(
            appEvent = AppEvent(
                id = UUID.randomUUID().toString(),
                appEventType = appEventType.await(),
                sessionId = sessionId,
                data = json,
                timestamp = timestamp
            )
        )
    }

    private suspend fun getAppEventTypeFromJson(json: String): Deferred<AppEventType> =
        withContext(Dispatchers.Default) {
            async {
                val eventType = JsonHelper.getJsonObject(json)["event_type"] ?: run {
                    throw MissingAppEventType()
                }
                when (eventType.jsonPrimitive.content) {
                    AppEventType.ADD_TO_CART -> AppEventType.MultipleEvent.AddToCart
                    AppEventType.INSTALL -> AppEventType.StrictlyOnceEvent.Install
                    AppEventType.SCREEN_VISIT -> AppEventType.OncePerSessionEvent.ScreenVisit
                    AppEventType.VISIT -> AppEventType.OncePerSessionEvent.Visit
                    AppEventType.PURCHASE -> AppEventType.MultipleEvent.Purchase
                    else -> AppEventType.Unknown
                }
            }
        }
}