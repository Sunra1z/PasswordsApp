package com.example.passwordsapp.feature_pass.presentation.PasswordCheck

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.passwordsapp.feature_pass.domain.repository.PasswordCheckRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PasswordHealthCheckWorker @Inject constructor(
    @ApplicationContext context: Context,
    workerParams: WorkerParameters,
    private val repository: PasswordCheckRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            repository.checkPasswordsHealth()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}