package com.example.app_event_tracker.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import com.example.app_event_tracker.data.local.LocalAppEventsDatabase
import com.example.app_event_tracker.data.local.LocalAppEventsDatabaseClient
import com.example.app_event_tracker.data.local.dao.LocalAppEventTrackerDao
import com.example.app_event_tracker.data.local.entity.UnprocessedAppEvent
import com.example.app_event_tracker.data.remote_mock.AppEventsDatabaseClient
import com.example.app_event_tracker.data.remote_mock.api.RemoteDataSource
import com.example.app_event_tracker.data.remote_mock.api.impl.RemoteDataSourceImpl
import com.example.app_event_tracker.domain.models.AppEventUploadStatus
import com.example.app_event_tracker.domain.models.UploadStatus
import kotlinx.coroutines.CancellationException

public class EventUploadWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val localDataSource: LocalAppEventsDatabase =
        LocalAppEventsDatabaseClient.getDatabase(applicationContext)

    private val remoteDataSource: RemoteDataSource =
        RemoteDataSourceImpl(applicationContext)

    override suspend fun doWork(): Result {
        val eventId = inputData.getString(EVENT_ID_KEY)
            ?: return Result.failure()

        val dao = localDataSource.localAppEventTrackerDao()

        var event = dao.getEventById(eventId)
            ?: return Result.success()

        if (runAttemptCount > 0) {
            event = event.copy(
                status = event.status.copy(
                    uploadStatus = UploadStatus.RETRYING
                )
            )
            dao.updateEvent(event)
        }

        event = event.copy(
            status = event.status.copy(
                uploadStatus = UploadStatus.PROCESSING
            )
        )
        dao.updateEvent(event)

        return try {
            val uploadSucceeded = remoteDataSource.uploadEvent(event)

            if (uploadSucceeded) {
                dao.markEventAsProcessed(event)
                Result.success()
            } else {
                handleUploadFailure(
                    dao = dao,
                    event = event
                )
                Result.retry()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleUploadFailure(
                dao = dao,
                event = event
            )
            Result.retry()
        }
    }

    private suspend fun handleUploadFailure(
        dao: LocalAppEventTrackerDao,
        event: UnprocessedAppEvent
    ) {
        val nextRetryAttempt = runAttemptCount + 1

        val retryingInterval = (
                    WorkRequest.MIN_BACKOFF_MILLIS *
                            nextRetryAttempt.toLong()
                    ).coerceAtMost(WorkRequest.MAX_BACKOFF_MILLIS)

        val retryAt = System.currentTimeMillis() + retryingInterval
        val failedEvent = event.copy(
            status = AppEventUploadStatus(
                uploadStatus = UploadStatus.FAILED,
                retryAttempt = nextRetryAttempt,
                retryAt = retryAt
            )
        )
        dao.updateEvent(failedEvent)
    }

    public companion object {
        public const val EVENT_ID_KEY: String = "event_id"
    }
}
