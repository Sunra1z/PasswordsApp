package com.example.passwordsapp.feature_pass.domain.util

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.security.MessageDigest

fun hashPassword(password: String): String {
    val messageDigest = MessageDigest.getInstance("SHA-1")
    val hashBytes = messageDigest.digest(password.toByteArray())
    return hashBytes.joinToString("") { "%02X".format(it) } // Convert to uppercase
}

suspend fun queryHIBP(hashPrefix: String): List<String>? {
    return withContext(Dispatchers.IO) {
        try {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://api.pwnedpasswords.com/range/$hashPrefix")
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (!responseBody.isNullOrBlank()) {
                        responseBody.split("\n")
                    } else {
                        println("API response body is null or blank.")
                        null
                    }
                } else {
                    println("API Request failed with status code: ${response.code}")
                    null
                }
            }
        } catch (e: Exception) {
            println("Error during API call: ${e.localizedMessage}")
            e.printStackTrace()
            null
        }
    }
}



suspend fun checkPasswordBreach(password: String): Int {
    val hashedPassword = hashPassword(password)
    val hashPrefix = hashedPassword.substring(0, 5)
    val hashSuffix = hashedPassword.substring(5)

//    println("Full Hash: $hashedPassword")
//    println("Hash Prefix: $hashPrefix")
//    println("Hash Suffix: $hashSuffix")

    val result = queryHIBP(hashPrefix)
//    if (result != null) { // For debugging
//        println("Full Response: ${result.joinToString("\n").take(500)}") // Log first 500 characters
//        println("API Response Lines: ${result.size}")
//    }

    // Refined comparison logic with detailed debugging
    val matches = result?.map { line ->
        val lineParts = line.trim().split(":")
        if (lineParts.size < 2) return@map null // Skip malformed lines
        val lineSuffix = lineParts[0]
//        println("Comparing: $lineSuffix with $hashSuffix") // Debugging

        // Check if the suffix matches (case-insensitive)
        if (lineSuffix.equals(hashSuffix, ignoreCase = true)) {
//            println("Match found: $lineSuffix -> ${lineParts[1]}") // Debugging match
            return@map lineParts[1].toIntOrNull()
        }
        null
    }

    // Get the first non-null match (breach count) or default to 0
    val breachCount = matches?.filterNotNull()?.firstOrNull() ?: 0
    println("Breach count: $breachCount")
    return breachCount
}


