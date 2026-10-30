package com.example.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object TimeUtils {

  fun getLagosGreeting(): String {
    val lagosTimeZone = TimeZone.getTimeZone("Africa/Lagos")
    val calendar = Calendar.getInstance(lagosTimeZone)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)

    return when {
      hour >= 22 || hour < 5 -> "Hello night owl!"
      hour in 5..11 -> "Welcome back researchers"
      hour in 12..16 -> "Good afternoon researchers"
      else -> "Good evening researchers"
    }
  }

  fun getLagosFormattedTime(): String {
    val lagosTimeZone = TimeZone.getTimeZone("Africa/Lagos")
    val sdf = SimpleDateFormat("HH:mm:ss", Locale.US)
    sdf.timeZone = lagosTimeZone
    return "${sdf.format(Date())} WAT (GMT+1)"
  }

  fun formatRelativeTime(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
      seconds < 60 -> "Just now"
      minutes < 60 -> "${minutes}m ago"
      hours < 24 -> "${hours}h ago"
      days < 7 -> "${days}d ago"
      else -> SimpleDateFormat("MMM d, yyyy", Locale.US).format(Date(timestamp))
    }
  }
}
