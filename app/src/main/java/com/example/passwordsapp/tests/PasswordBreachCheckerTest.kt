package com.example.passwordsapp.tests

import com.example.passwordsapp.feature_pass.domain.util.checkPasswordBreach
import com.nulabinc.zxcvbn.Zxcvbn
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class PasswordBreachCheckerTest {

    private val zxcvbn = Zxcvbn()

    @Test
    fun testLeakedPass() = runBlocking {
        val leakedPassword = "password123" // Example password
        val breachCount: Int = checkPasswordBreach(leakedPassword)
        println("Breach count for password '$leakedPassword': $breachCount")
        assertTrue("Password should have been breached", breachCount > 0)
    }

    @Test
    fun testStrongPass() = runBlocking {
        val strongPassword = "2\"7Ei;U?Py6=Gs0ly!Z"
        val breachCount: Int = checkPasswordBreach(strongPassword)
        println("Breach count for password '$strongPassword': $breachCount")
        assertTrue("Password should not have been breached", breachCount == 0)
    }

    @Test
    fun testEstimatorMeasure() = runBlocking {
        val passwordExample = "kasim2004"
        val analysis = zxcvbn.measure(passwordExample)
        println("Analysis for $passwordExample\n" +
                "Score: ${analysis.score}\n" +
                "Guesses: ${analysis.guesses}\n" +
                "Calculation time: ${analysis.calcTime}\n" +
                "Crack time (in seconds): ${analysis.crackTimeSeconds.onlineThrottling100perHour}\n" +
                "Warning: ${analysis.feedback.warning}\n" +
                "Suggestions: ${analysis.feedback.suggestions}")
        assertTrue("Password should have a score of 2", analysis.score == 2)
    }
}