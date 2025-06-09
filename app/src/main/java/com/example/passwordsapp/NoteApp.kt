package com.example.passwordsapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.passwordsapp.feature_pass.domain.repository.PreferencesRepository
import com.example.passwordsapp.feature_pass.presentation.PasswordCheck.PasswordCheckWorker
import com.example.passwordsapp.feature_pass.presentation.PasswordCheck.PasswordHealthCheckWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class NoteApp : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory
    @Inject lateinit var preferencesRepository: PreferencesRepository

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("NoteApp", "Application onCreate called")
        createNotificationChannel()
        CoroutineScope(Dispatchers.IO).launch {
            val alertTimeInHours = fetchAlertTime()
            withContext(Dispatchers.Main) {
                setupPeriodicWork(alertTimeInHours)
                schedulePasswordHealthCheck()
            }
        }
    }

    private suspend fun fetchAlertTime(): Long {
        val alertTime = preferencesRepository.alertTime.first()
        return when (alertTime) {
            "1h" -> 1
            "6h" -> 6
            "12h" -> 12
            "24h" -> 24
            else -> 24 // Default to 24 hours if the value is not recognized
        }
    }

    private fun schedulePasswordHealthCheck(){
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<PasswordHealthCheckWorker>(1, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "PasswordHealthCheck",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }

    private fun setupPeriodicWork(alertTimeHours: Long) {
        Log.d("NoteApp", "Setting up periodic work")

        // Define constraints for periodic work
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // Requires internet connection
            .setRequiresBatteryNotLow(true) // Avoids running on low battery
            .build()

        // Create a periodic work request with a minimum interval of 1 day
        val workRequest = PeriodicWorkRequestBuilder<PasswordCheckWorker>(alertTimeHours, TimeUnit.HOURS)
            .setConstraints(constraints) // Attach constraints
            .build()

        // Enqueue the periodic work uniquely
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "PasswordCheckWork", // Unique name for the task
            ExistingPeriodicWorkPolicy.UPDATE, // Replace existing work if it exists
            workRequest
        )
        Log.d("NoteApp", "Periodic work setup complete")

        // Monitor the status of the work
        WorkManager.getInstance(this).getWorkInfosForUniqueWorkLiveData("PasswordCheckWork")
            .observeForever { workInfos ->
                workInfos?.forEach { workInfo ->
                    Log.d("NoteApp", "WorkInfo ID: ${workInfo.id}, State: ${workInfo.state}")
                    if (workInfo.state == WorkInfo.State.FAILED) {
                        Log.e("NoteApp", "Work failed. ID: ${workInfo.id}")
                    }
                }
            }
    }

    private fun setupTestWork() { // for Debugging Notification purposes
        Log.d("NoteApp", "Setting up test work")

        // Create a simple one-time work request
        val workRequest = OneTimeWorkRequestBuilder<PasswordCheckWorker>().build()

        // Enqueue the work
        WorkManager.getInstance(this).enqueueUniqueWork(
            "PasswordCheckTestWork", // Unique name for testing
            ExistingWorkPolicy.REPLACE, // Replace any existing test work
            workRequest
        )
        Log.d("NoteApp", "Test work enqueued")

        // Monitor the work's status
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(workRequest.id)
            .observeForever { workInfo ->
                workInfo?.let {
                    Log.d("NoteApp", "WorkInfo ID: ${it.id}, State: ${it.state}")
                    if (it.state == WorkInfo.State.SUCCEEDED) {
                        Log.d("NoteApp", "Work completed successfully.")
                    } else if (it.state == WorkInfo.State.FAILED) {
                        Log.e("NoteApp", "Work failed.")
                    }
                }
            }
    }


    private fun createNotificationChannel() {
        Log.d("NoteApp", "Creating notification channel")
        val name = "Password Check Notifications"
        val descriptionText = "Notifications for compromised or weak passwords"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("PASSWORD_CHECK_CHANNEL", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        Log.d("NoteApp", "Notification channel created")
    }
}