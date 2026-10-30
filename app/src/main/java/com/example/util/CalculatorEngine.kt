package com.example.util

import kotlin.math.*

object CalculatorEngine {

  fun evaluate(expression: String, isDegree: Boolean, ansValue: Double = 0.0): Double {
    if (expression.isBlank()) return 0.0

    // Clean and normalize expression
    var sanitized = expression
      .replace("×", "*")
      .replace("÷", "/")
      .replace("−", "-")
      .replace("π", Math.PI.toString())
      .replace("e", Math.E.toString())
      .replace("ANS", ansValue.toString())
      .replace("Ans", ansValue.toString())

    return ExpressionParser(sanitized, isDegree).parse()
  }

  private class ExpressionParser(val str: String, val isDegree: Boolean) {
    var pos = -1
    var ch = ' '

    fun nextChar() {
      pos++
      ch = if (pos < str.length) str[pos] else '\u0000'
    }

    fun eat(charToEat: Char): Boolean {
      while (ch == ' ') nextChar()
      if (ch == charToEat) {
        nextChar()
        return true
      }
      return false
    }

    fun parse(): Double {
      nextChar()
      val x = parseExpression()
      if (pos < str.length) throw RuntimeException("Unexpected: $ch")
      return x
    }

    fun parseExpression(): Double {
      var x = parseTerm()
      while (true) {
        when {
          eat('+') -> x += parseTerm()
          eat('-') -> x -= parseTerm()
          else -> return x
        }
      }
    }

    fun parseTerm(): Double {
      var x = parseFactor()
      while (true) {
        when {
          eat('*') -> x *= parseFactor()
          eat('/') -> {
            val div = parseFactor()
            if (div == 0.0) throw ArithmeticException("Math Error: Division by zero")
            x /= div
          }
          eat('%') -> x %= parseFactor()
          else -> return x
        }
      }
    }

    fun parseFactor(): Double {
      if (eat('+')) return +parseFactor()
      if (eat('-')) return -parseFactor()

      var x: Double
      val startPos = pos

      if (eat('(')) {
        x = parseExpression()
        eat(')')
      } else if ((ch in '0'..'9') || ch == '.') {
        while ((ch in '0'..'9') || ch == '.') nextChar()
        x = str.substring(startPos, pos).toDouble()
      } else if (ch in 'a'..'z' || ch in 'A'..'Z' || ch == '√' || ch == '∛') {
        if (ch == '√' || ch == '∛') {
          val isCbrt = ch == '∛'
          nextChar()
          val arg = parseFactor()
          return if (isCbrt) cbrt(arg) else sqrt(arg)
        }
        while (ch in 'a'..'z' || ch in 'A'..'Z' || (ch in '0'..'9') || ch == '_') nextChar()
        val func = str.substring(startPos, pos)
        val arg = parseFactor()
        x = when (func.lowercase()) {
          "sin" -> sin(if (isDegree) Math.toRadians(arg) else arg)
          "cos" -> cos(if (isDegree) Math.toRadians(arg) else arg)
          "tan" -> tan(if (isDegree) Math.toRadians(arg) else arg)
          "asin" -> if (isDegree) Math.toDegrees(asin(arg)) else asin(arg)
          "acos" -> if (isDegree) Math.toDegrees(acos(arg)) else acos(arg)
          "atan" -> if (isDegree) Math.toDegrees(atan(arg)) else atan(arg)
          "sinh" -> sinh(arg)
          "cosh" -> cosh(arg)
          "tanh" -> tanh(arg)
          "sqrt" -> sqrt(arg)
          "cbrt" -> cbrt(arg)
          "log", "log10" -> log10(arg)
          "ln" -> ln(arg)
          "abs" -> abs(arg)
          else -> throw RuntimeException("Unknown function: $func")
        }
      } else {
        throw RuntimeException("Unexpected character: $ch")
      }

      // Check for power '^' or factorial '!'
      while (true) {
        if (eat('^')) {
          x = x.pow(parseFactor())
        } else if (eat('!')) {
          x = factorial(x.toLong()).toDouble()
        } else {
          break
        }
      }

      return x
    }

    private fun factorial(n: Long): Long {
      if (n < 0) throw ArithmeticException("Factorial of negative number")
      var res = 1L
      for (i in 2..min(n, 20L)) {
        res *= i
      }
      return res
    }
  }

  data class StatisticsResult(
    val count: Int,
    val mean: Double,
    val median: Double,
    val mode: String,
    val range: Double,
    val stdDev: Double,
    val variance: Double
  )

  fun computeStatistics(numbers: List<Double>): StatisticsResult? {
    if (numbers.isEmpty()) return null

    val sorted = numbers.sorted()
    val count = sorted.size
    val mean = sorted.average()

    // Median
    val median = if (count % 2 == 1) {
      sorted[count / 2]
    } else {
      (sorted[count / 2 - 1] + sorted[count / 2]) / 2.0
    }

    // Mode calculation per prompt requirement:
    // [2,3,3,4] -> 3
    // [1,1,2,2,3] -> 1, 2
    // [1,2,3,4] -> None
    val frequencies = sorted.groupingBy { it }.eachCount()
    val maxFreq = frequencies.values.maxOrNull() ?: 1

    val modeStr = if (maxFreq == 1 || frequencies.values.all { it == maxFreq }) {
      "None"
    } else {
      val modes = frequencies.filter { it.value == maxFreq }.keys.sorted()
      modes.joinToString(", ") { if (it % 1.0 == 0.0) it.toLong().toString() else it.toString() }
    }

    // Range
    val range = sorted.last() - sorted.first()

    // Variance & Standard Deviation
    val variance = sorted.sumOf { (it - mean).pow(2) } / count
    val stdDev = sqrt(variance)

    return StatisticsResult(
      count = count,
      mean = mean,
      median = median,
      mode = modeStr,
      range = range,
      stdDev = stdDev,
      variance = variance
    )
  }
}
