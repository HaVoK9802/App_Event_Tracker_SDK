package com.example.app_event_tracker.data.remote_mock.api.impl

import com.example.app_event_tracker.AppEventTracker
import com.example.app_event_tracker.data.local.entity.UnprocessedAppEvent
import com.example.app_event_tracker.data.remote_mock.AppEventsDatabase
import com.example.app_event_tracker.data.remote_mock.AppEventsDatabaseClient
import com.example.app_event_tracker.data.remote_mock.api.ProcessedAppEventsDao
import com.example.app_event_tracker.data.remote_mock.api.RemoteDataSource
import com.example.app_event_tracker.data.remote_mock.mappers.toProcessedAppEvent
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

internal class RemoteDataSourceImpl(
    val serverMock: AppEventsDatabase = AppEventsDatabaseClient.getDatabase(
        AppEventTracker.getInstance().applicationContext
    )
): RemoteDataSource {


    override suspend fun uploadEvent(unprocessedEvent: UnprocessedAppEvent): Boolean {
        return if (eventProcessingSimulation()){
            serverMock.appEventsDao().eventIngestion(unprocessedEvent.toProcessedAppEvent())
            true
        } else {
             false
        }
    }

    override fun getProcessedAppEventsDataInterface(): ProcessedAppEventsDao {
        return serverMock.appEventsDao()
    }

    override suspend fun eventProcessingSimulation(): Boolean {
        val randomDelay = (1000..5000).random().toLong()
        delay(randomDelay.milliseconds)
        return Random.nextInt(1,101) <= 80
    }
}