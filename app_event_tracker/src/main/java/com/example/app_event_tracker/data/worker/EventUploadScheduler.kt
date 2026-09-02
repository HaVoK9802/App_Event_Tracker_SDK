package com.example.app_event_tracker.data.worker

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit

internal class EventUploadScheduler(
    context: Context
) {

    private val workManager = WorkManager.getInstance(context)

    fun schedule(eventId: String) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest =
            OneTimeWorkRequestBuilder<EventUploadWorker>()
                .setInputData(
                    workDataOf(
                        EventUploadWorker.EVENT_ID_KEY to eventId
                    )
                )
                .setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    WorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .addTag(EVENT_UPLOAD_TAG)
                .build()

        workManager.enqueueUniqueWork(
            uniqueWorkName(eventId),
            ExistingWorkPolicy.KEEP,
            workRequest
        )
    }

    private fun uniqueWorkName(eventId: String): String {
        return "$WORK_NAME_PREFIX$eventId"
    }

    private companion object {
        const val WORK_NAME_PREFIX = "app_event_upload_"
        const val EVENT_UPLOAD_TAG = "app_event_upload"
    }
}
