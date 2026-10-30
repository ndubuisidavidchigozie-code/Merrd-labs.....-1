package com.example.sync

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

enum class SyncStatus(val label: String, val colorHex: Long) {
  SYNCED_LIVE("Synced Live", 0xFF10B981), // Green
  RECONNECTING("Reconnecting to Lab...", 0xFFF59E0B), // Yellow
  OFFLINE_LOCAL("Offline (Saving Locally)", 0xFF94A3B8) // Gray
}

data class SyncPayload(
  val id: String = UUID.randomUUID().toString(),
  val actionType: String,
  val entityId: String,
  val dataJson: String,
  val timestamp: Long = System.currentTimeMillis()
)

data class LeaderboardEntry(
  val id: String,
  val playerName: String,
  val quizType: String,
  val score: Int,
  val totalQuestions: Int,
  val streak: Int,
  val completionTimeSeconds: Long,
  val timestamp: Long = System.currentTimeMillis()
)

class SyncManager(context: Context) {
  private val scope = CoroutineScope(Dispatchers.IO)
  private val prefs = context.getSharedPreferences("merdd_sync_prefs", Context.MODE_PRIVATE)

  private val _syncStatus = MutableStateFlow(SyncStatus.SYNCED_LIVE)
  val syncStatus: StateFlow<SyncStatus> = _syncStatus.asStateFlow()

  private val _offlineQueue = MutableStateFlow<List<SyncPayload>>(emptyList())
  val offlineQueue: StateFlow<List<SyncPayload>> = _offlineQueue.asStateFlow()

  private val _leaderboard = MutableStateFlow<List<LeaderboardEntry>>(
    listOf(
      LeaderboardEntry("1", "David", "Physics Mechanics", 10, 10, 5, 210),
      LeaderboardEntry("2", "Mercy", "Chemistry Titration", 10, 10, 7, 195),
      LeaderboardEntry("3", "David", "Maths Calculus", 9, 10, 4, 240),
      LeaderboardEntry("4", "Mercy", "Biology Genetics", 10, 10, 6, 180)
    )
  )
  val leaderboard: StateFlow<List<LeaderboardEntry>> = _leaderboard.asStateFlow()

  private val _currentUserName = MutableStateFlow(prefs.getString("merdd_user_name", "David") ?: "David")
  val currentUserName: StateFlow<String> = _currentUserName.asStateFlow()

  init {
    // Start background sync heartbeat loop
    scope.launch {
      while (true) {
        delay(12000)
        if (_offlineQueue.value.isNotEmpty() && _syncStatus.value == SyncStatus.SYNCED_LIVE) {
          flushQueue()
        }
      }
    }
  }

  fun setUserName(name: String) {
    _currentUserName.value = name
    prefs.edit().putString("merdd_user_name", name).apply()
  }

  fun setNetworkSimulation(status: SyncStatus) {
    _syncStatus.value = status
    if (status == SyncStatus.SYNCED_LIVE && _offlineQueue.value.isNotEmpty()) {
      scope.launch { flushQueue() }
    }
  }

  fun queueSync(actionType: String, entityId: String, dataJson: String) {
    val payload = SyncPayload(
      actionType = actionType,
      entityId = entityId,
      dataJson = dataJson
    )
    if (_syncStatus.value == SyncStatus.SYNCED_LIVE) {
      // Optimistic Instant Sync - immediately recorded
      // simulate instant socket ack
    } else {
      // Queue offline
      val current = _offlineQueue.value.toMutableList()
      // Deduplicate if same entityId & actionType
      current.removeAll { it.entityId == entityId && it.actionType == actionType }
      current.add(payload)
      _offlineQueue.value = current
    }
  }

  private suspend fun flushQueue() {
    _syncStatus.value = SyncStatus.RECONNECTING
    delay(1000) // Ordered reconnect flush simulation
    _offlineQueue.value = emptyList()
    _syncStatus.value = SyncStatus.SYNCED_LIVE
  }

  fun broadcastQuizResult(quizType: String, score: Int, total: Int, streak: Int, timeSec: Long) {
    val entry = LeaderboardEntry(
      id = UUID.randomUUID().toString(),
      playerName = _currentUserName.value,
      quizType = quizType,
      score = score,
      totalQuestions = total,
      streak = streak,
      completionTimeSeconds = timeSec
    )
    val current = _leaderboard.value.toMutableList()
    current.add(0, entry)
    _leaderboard.value = current.take(20)
    queueSync("BROADCAST_QUIZ", entry.id, "${entry.playerName}:$score/$total")
  }

  companion object {
    @Volatile
    private var INSTANCE: SyncManager? = null

    fun getInstance(context: Context): SyncManager {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: SyncManager(context.applicationContext).also { INSTANCE = it }
      }
    }
  }
}
