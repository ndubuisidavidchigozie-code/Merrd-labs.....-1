package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "research_notes")
data class ResearchNote(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val content: String,
  val author: String, // "David" or "Mercy"
  val category: String, // e.g. "Physics", "Chemistry", "JAMB"
  val tags: String, // e.g. "#Physics #Mechanics"
  val timestamp: Long = System.currentTimeMillis(),
  val likes: Int = 0
)

enum class MistakeCategory(val label: String, val description: String) {
  CONCEPTUAL_GAP("Conceptual Gap", "Misunderstanding of core scientific principles"),
  CALCULATION_ERROR("Calculation Error", "Arithmetic or unit conversion error"),
  MISREAD_QUESTION("Misread Question", "Overlooked NOT, EXCEPT, or key variable"),
  TIME_PRESSURE("Time Pressure", "Rushed decision or solved in >120 seconds")
}

@Entity(tableName = "cbt_mistakes")
data class MistakeItem(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val questionId: Int,
  val subject: String,
  val topic: String,
  val questionText: String,
  val optionA: String,
  val optionB: String,
  val optionC: String,
  val optionD: String,
  val correctOption: Char,
  val userSelectedOption: Char,
  val explanation: String,
  val category: String, // matches MistakeCategory.name
  val consecutiveCorrect: Int = 0, // Must reach 2 consecutive correct to clear!
  val cleared: Boolean = false,
  val timeSpentSeconds: Long = 0,
  val recordedAt: Long = System.currentTimeMillis()
)

data class CbtQuestion(
  val id: Int,
  val subject: String,
  val topic: String,
  val examYear: String = "JAMB 2024",
  val questionText: String,
  val optionA: String,
  val optionB: String,
  val optionC: String,
  val optionD: String,
  val correctOption: Char,
  val explanation: String,
  val formula: String? = null
)

data class TopicMastery(
  val subject: String,
  val topic: String,
  val correctCount: Int,
  val totalCount: Int
) {
  val percentage: Float
    get() = if (totalCount > 0) (correctCount.toFloat() / totalCount) * 100f else 0f

  val statusColor: String
    get() = when {
      percentage >= 75f -> "GREEN"
      percentage >= 50f -> "YELLOW"
      else -> "RED"
    }
}

data class NigerianState(
  val name: String,
  val capital: String,
  val lgaCount: Int,
  val majorHubs: String,
  val zone: String, // "Guinea Savanna", "Rainforest", etc.
  val relX: Float, // 0.0 - 1.0 on Nigeria Canvas
  val relY: Float
)

data class GisFeature(
  val id: String,
  val name: String,
  val layer: String, // "RIVERS", "RELIEF", "MINERALS", "AFRICA"
  val formationType: String,
  val economicSignificance: String,
  val pastJambQuestions: List<String>,
  val posX: Float, // 0f..1f
  val posY: Float
)
