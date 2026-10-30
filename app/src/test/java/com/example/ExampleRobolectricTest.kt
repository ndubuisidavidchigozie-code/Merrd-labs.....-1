package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.MerddRepository
import com.example.util.CalculatorEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun testAppNameIsMerddLab() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Merdd Lab", appName)
  }

  @Test
  fun testPasscodeVerification() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = MerddRepository(context)
    assertTrue(repository.verifyPasscode("solana"))
    assertTrue(repository.verifyPasscode("SOLANA"))
    assertFalse(repository.verifyPasscode("wrong_password"))
  }

  @Test
  fun testCalculatorEvaluation() {
    val result1 = CalculatorEngine.evaluate("2 + 3 * 4", isDegree = true)
    assertEquals(14.0, result1, 0.0001)

    val result2 = CalculatorEngine.evaluate("sin(30)", isDegree = true)
    assertEquals(0.5, result2, 0.0001)

    val result3 = CalculatorEngine.evaluate("√(16) + 3^2", isDegree = true)
    assertEquals(13.0, result3, 0.0001)
  }

  @Test
  fun testStatisticsModeCalculation() {
    // [2,3,3,4] -> 3
    val stats1 = CalculatorEngine.computeStatistics(listOf(2.0, 3.0, 3.0, 4.0))
    assertEquals("3", stats1?.mode)
    assertEquals(3.0, stats1?.mean ?: 0.0, 0.001)
    assertEquals(3.0, stats1?.median ?: 0.0, 0.001)
    assertEquals(2.0, stats1?.range ?: 0.0, 0.001)

    // [1,1,2,2,3] -> 1, 2
    val stats2 = CalculatorEngine.computeStatistics(listOf(1.0, 1.0, 2.0, 2.0, 3.0))
    assertEquals("1, 2", stats2?.mode)

    // [1,2,3,4] -> None
    val stats3 = CalculatorEngine.computeStatistics(listOf(1.0, 2.0, 3.0, 4.0))
    assertEquals("None", stats3?.mode)
  }

  @Test
  fun testTimeUtils() {
    val greeting = com.example.util.TimeUtils.getLagosGreeting()
    assertTrue(greeting.isNotBlank())

    val formatted = com.example.util.TimeUtils.getLagosFormattedTime()
    assertTrue(formatted.contains("WAT"))
    val timeOnly = formatted.substringBefore(" WAT")
    assertTrue(timeOnly.matches(Regex("\\d{2}:\\d{2}:\\d{2}")))
  }
}
