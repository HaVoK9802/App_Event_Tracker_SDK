package com.example.app_event_tracker

import android.content.Context
import com.example.app_event_tracker.data.SessionManager
import com.example.app_event_tracker.data.repository.AppEventsRepoImpl
import com.example.app_event_tracker.data.worker.EventUploadScheduler
import com.example.app_event_tracker.domain.models.AppEvent
import com.example.app_event_tracker.domain.models.AppEventWithStatus
import com.example.app_event_tracker.domain.repository.AppEventsRepo
import com.example.app_event_tracker.domain.usecase.GetProcessedEvents
import com.example.app_event_tracker.domain.usecase.GetUnprocessedEvents
import com.example.app_event_tracker.domain.usecase.TrackEvent
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

public class AppEventTracker private constructor(
    internal val applicationContext: Context,
    private var trackEvent: TrackEvent? = null,
    private var getProcessedEvents: GetProcessedEvents? = null,
    private var getUnprocessedEvents: GetUnprocessedEvents? = null
){

    internal var sessionManager: SessionManager? = null
    private var coroutineScope: CoroutineScope? = null

    private var appEventsRepo: AppEventsRepo? = null

    internal var eventUploadScheduler: EventUploadScheduler? = null

    public companion object{
        @Volatile
        private var instance: AppEventTracker? = null

        public fun initializeTracker(context: Context): AppEventTracker {
            return instance ?: synchronized(this) {
                instance ?: AppEventTracker(context.applicationContext).also {
                    it.sessionManager = SessionManager()
                    it.sessionManager?.startSession()
                    it.coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO + CoroutineName("AppEventTrackerScope"))
                    it.appEventsRepo = AppEventsRepoImpl()
                    it.eventUploadScheduler = EventUploadScheduler(it.applicationContext)
                    it.trackEvent = TrackEvent(it.appEventsRepo!!)
                    it.getProcessedEvents = GetProcessedEvents(it.appEventsRepo!!)
                    it.getUnprocessedEvents = GetUnprocessedEvents(it.appEventsRepo!!)
                    instance = it
                }
            }
        }

        public fun getInstance(): AppEventTracker {
            return instance ?: throw IllegalStateException("AppEventTracker is not initialized. Call initializeTracker(context) first.")
        }
    }

    public fun destroyTracker() {
        sessionManager?.endSession()
        sessionManager = null
        coroutineScope?.cancel()
        coroutineScope = null
        instance = null
    }

    public fun trackEvent(
        json: String
    ) {
        coroutineScope?.launch {
            trackEvent?.invoke(
                json = json,
                sessionId = sessionManager?.getSessionId()!!,
                timestamp = System.currentTimeMillis()
            )
        }
    }

    public suspend fun getProcessedEvents(): Flow<List<AppEvent>> {
        return getProcessedEvents?.invoke() ?: emptyFlow()
    }

    public suspend fun getUnprocessedEvents(): Flow<List<AppEventWithStatus>> {
        return getUnprocessedEvents?.invoke() ?: emptyFlow()
    }
}