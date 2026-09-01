package com.example.app_event_tracker

import android.content.Context
import com.example.app_event_tracker.data.SessionManager
import com.example.app_event_tracker.domain.usecase.TrackEvent
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

public class AppEventTracker private constructor(
    internal val applicationContext: Context,
    private val trackEvent: TrackEvent = TrackEvent()
){

    private var sessionManager: SessionManager? = null
    private var coroutineScope: CoroutineScope? = null

    public companion object{
        @Volatile
        private var instance: AppEventTracker? = null

        public fun initializeTracker(context: Context): AppEventTracker {
            return instance ?: synchronized(this) {
                instance ?: AppEventTracker(context.applicationContext).also {
                    it.sessionManager = SessionManager()
                    it.coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO + CoroutineName("AppEventTrackerScope"))
                    instance = it
                }
            }
        }

        public fun getInstance(): AppEventTracker {
            return instance ?: throw IllegalStateException("AppEventTracker is not initialized. Call initializeTracker(context) first.")
        }
    }

    public fun destroyTracker() {
        sessionManager = null
        coroutineScope?.cancel()
        coroutineScope = null
        instance = null
    }

    public fun trackEvent(
        json: String
    ) {
        coroutineScope?.launch {
              trackEvent(
                  json = json,
                  sessionId = sessionManager?.getSessionId()!!,
                  timestamp = System.currentTimeMillis()
              )
        }
    }
}