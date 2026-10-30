package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.MistakeCategory
import com.example.model.MistakeItem
import com.example.model.ResearchNote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MerddRepository(context: Context) {
  private val db = AppDatabase.getDatabase(context)
  private val researchDao = db.researchDao()
  private val mistakeDao = db.mistakeDao()
  private val prefs: SharedPreferences = context.getSharedPreferences("merdd_lab_prefs", Context.MODE_PRIVATE)

  init {
    // Seed database if empty
    CoroutineScope(Dispatchers.IO).launch {
      val existingNotes = researchDao.getAllNotes().first()
      if (existingNotes.isEmpty()) {
        SeedData.initialResearchNotes.forEach { researchDao.insertNote(it) }
      }
      val existingMistakes = mistakeDao.getAllMistakes().first()
      if (existingMistakes.isEmpty()) {
        SeedData.initialMistakes.forEach { mistakeDao.insertMistake(it) }
      }
    }
  }

  // Passcode Auth
  fun isLabUnlocked(): Boolean {
    return prefs.getBoolean("lab_unlocked", false)
  }

  fun setLabUnlocked(unlocked: Boolean) {
    prefs.edit().putBoolean("lab_unlocked", unlocked).apply()
  }

  fun verifyPasscode(input: String): Boolean {
    val isValid = input.trim().equals("solana", ignoreCase = true)
    if (isValid) {
      setLabUnlocked(true)
    }
    return isValid
  }

  // Research Notes
  val allNotes: Flow<List<ResearchNote>> = researchDao.getAllNotes()

  suspend fun insertNote(note: ResearchNote): Long {
    return researchDao.insertNote(note)
  }

  suspend fun likeNote(id: Long) {
    researchDao.likeNote(id)
  }

  suspend fun deleteNote(id: Long) {
    researchDao.deleteNote(id)
  }

  // Mistake Bank
  val activeMistakes: Flow<List<MistakeItem>> = mistakeDao.getActiveMistakes()

  fun getMistakesByCategory(category: String): Flow<List<MistakeItem>> {
    return mistakeDao.getMistakesByCategory(category)
  }

  suspend fun insertMistake(item: MistakeItem): Long {
    return mistakeDao.insertMistake(item)
  }

  suspend fun recordMistakeAnswer(mistakeId: Long, isCorrect: Boolean) {
    if (isCorrect) {
      mistakeDao.recordMistakeCorrect(mistakeId)
    } else {
      mistakeDao.recordMistakeWrong(mistakeId)
    }
  }

  suspend fun purgeClearedMistakes() {
    mistakeDao.purgeClearedMistakes()
  }

  // Calculator History & Storage
  fun getCalcHistory(): List<String> {
    val raw = prefs.getString("calc_history", "") ?: ""
    return if (raw.isBlank()) emptyList() else raw.split(";;;").filter { it.isNotBlank() }
  }

  fun saveCalcExpression(expr: String, result: String) {
    val current = getCalcHistory().toMutableList()
    current.add(0, "$expr = $result")
    if (current.size > 200) current.removeAt(current.size - 1)
    prefs.edit().putString("calc_history", current.joinToString(";;;")).apply()
  }

  fun clearCalcHistory() {
    prefs.edit().remove("calc_history").apply()
  }

  fun getStorageUsageBytes(): Long {
    // Calculate approximate size of shared preferences and database
    val historyLen = (prefs.getString("calc_history", "") ?: "").length.toLong()
    return 210_000L + historyLen * 2L // baseline ~0.21 MB
  }
}
