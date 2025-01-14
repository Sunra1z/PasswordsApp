package com.example.passwordsapp.feature_pass.presentation.PasswordCheck

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.passwordsapp.MainActivity
import com.example.passwordsapp.R
import com.example.passwordsapp.feature_pass.domain.model.PasswordWarning
import com.example.passwordsapp.feature_pass.domain.repository.PasswordCheckRepository
import com.example.passwordsapp.feature_pass.domain.repository.PreferencesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject


@HiltWorker
class PasswordCheckWorker @AssistedInject constructor( //
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val passwordCheckRepository: PasswordCheckRepository,
    private val preferencesRepository: PreferencesRepository
) : CoroutineWorker(context, workerParams) {


    override suspend fun doWork(): Result {

        val notificationsEnabled = preferencesRepository.notificationEnabled.first()

        // Cancel the worker if notifications are disabled
        if (!notificationsEnabled){
            return Result.success() // exit via Result success
        }
        // Notifications work
        return try {
            Log.d("PasswordCheckWorker", "doWork started")
            val breaches = passwordCheckRepository.checkPasswordForBreaches()
            Log.d("PasswordCheckWorker", "Warnings and breaches retrieved")
            sendNotifications(breaches)
            Log.d("PasswordCheckWorker", "sendNotifications called")
            Result.success()
        } catch (e: Exception) {
            Log.e("PasswordCheckWorker", "Exception in doWork: ${e.message}")
            Result.failure()
        }
    }

    private fun sendNotifications(warnings: List<PasswordWarning>) {
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        Log.d("PasswordCheckWorker", "sendNotifications called with ${warnings.size} warnings")

        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            warnings.forEach { warning ->
                Log.d("PasswordCheckWorker", "Processing warning: ${warning.title}")

                val intent = Intent(applicationContext, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                val pendingIntent: PendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

                val notification = NotificationCompat.Builder(applicationContext, "PASSWORD_CHECK_CHANNEL")
                    .setSmallIcon(R.drawable.baseline_warning_24)
                    .setContentTitle("Password Warning")
                    .setContentText("${warning.title}: ${warning.warning}")
                    .setStyle(NotificationCompat.BigTextStyle().bigText("${warning.title}: ${warning.warning}\n ${warning.suggestions.joinToString(", ")}"))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build()

                try {
                    notificationManager.notify(warning.noteId ?: 0, notification)
                    Log.d("PasswordCheckWorker", "Notification sent for warning: ${warning.title}")
                } catch (e: SecurityException) {
                    Log.e("PasswordCheckWorker", "SecurityException while sending notification: ${e.message}")
                }
            }
        } else {
            Log.w("PasswordCheckWorker", "POST_NOTIFICATIONS permission not granted")
        }
    }

}