package com.example.app_event_tracker.data

import java.util.UUID
import java.util.concurrent.atomic.AtomicReference


internal class SessionManager {
    private val sessionId: AtomicReference<String?> = AtomicReference<String?>(null)

    fun getSessionId(): String? {
        return sessionId.get()
    }

    fun startSession() {
        sessionId.compareAndSet(null, UUID.randomUUID().toString())
    }

    fun endSession() {
        sessionId.set(null)
    }
}