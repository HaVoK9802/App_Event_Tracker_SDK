package com.example.app_event_tracker.domain.usecase

import com.example.app_event_tracker.JsonHelper
import com.example.app_event_tracker.data.repository.AppEventsRepoImpl
import com.example.app_event_tracker.domain.AppEvent
import com.example.app_event_tracker.domain.AppEventType
import com.example.app_event_tracker.domain.repository.AppEventsRepo
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.jsonPrimitive

internal class TrackEvent(
    private val appEventsRepo: AppEventsRepo = AppEventsRepoImpl()
) {
    suspend operator fun invoke(
        json: String,
        sessionId: String,
        timestamp: Long
    ) {

        val appEventType = getAppEventTypeFromJson(json)
        appEventsRepo.trackEvent(
            appEvent = AppEvent(
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
                when (JsonHelper.getJsonObject(json)["event_type"]!!.jsonPrimitive.content) {
                    "add_to_cart" -> AppEventType.ADD_TO_CART
                    "install" -> AppEventType.INSTALL
                    "visit" -> AppEventType.VISIT
                    "purchase" -> AppEventType.PURCHASE
                    else -> AppEventType.UNKNOWN
                }
            }
        }
}