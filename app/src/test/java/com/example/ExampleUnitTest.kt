package com.example

import com.example.util.CalculatorEngine
import com.example.util.TimeUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
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
    // Single mode: [2,3,3,4] -> 3
    val stats1 = CalculatorEngine.computeStatistics(listOf(2.0, 3.0, 3.0, 4.0))
    assertNotNull(stats1)
    assertEquals("3", stats1?.mode)
    assertEquals(3.0, stats1?.mean ?: 0.0, 0.001)
    assertEquals(3.0, stats1?.median ?: 0.0, 0.001)
    assertEquals(2.0, stats1?.range ?: 0.0, 0.001)

    // Bimodal: [1,1,2,2,3] -> 1, 2
    val stats2 = CalculatorEngine.computeStatistics(listOf(1.0, 1.0, 2.0, 2.0, 3.0))
    assertNotNull(stats2)
    assertEquals("1, 2", stats2?.mode)

    // No mode: [1,2,3,4] -> None
    val stats3 = CalculatorEngine.computeStatistics(listOf(1.0, 2.0, 3.0, 4.0))
    assertNotNull(stats3)
    assertEquals("None", stats3?.mode)
  }

  @Test
  fun testTimeUtils() {
    val greeting = TimeUtils.getLagosGreeting()
    assertTrue(greeting.isNotBlank())

    val formatted = TimeUtils.getLagosFormattedTime()
    assertTrue(formatted.contains("WAT"))
    val timeOnly = formatted.substringBefore(" WAT")
    assertTrue(timeOnly.matches(Regex("\\d{2}:\\d{2}:\\d{2}")))
  }
}

