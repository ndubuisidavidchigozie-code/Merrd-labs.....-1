package com.example.ui.screens.cbt

import android.content.Context
import android.content.Intent
import android.graphics.Color as AndroidColor
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.data.SeedData
import com.example.model.CbtQuestion
import com.example.model.MistakeCategory
import com.example.model.MistakeItem
import com.example.model.TopicMastery
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonRed
import com.example.ui.theme.NeonYellow
import com.example.ui.theme.ObsidianBackground
import com.example.ui.theme.ObsidianCardBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import java.io.File
import java.io.FileOutputStream

@Composable
fun CbtHubScreen(
  questions: List<CbtQuestion>,
  currentIndex: Int,
  selectedAnswers: Map<Int, Char>,
  questionTimes: Map<Int, Long>,
  isSubmitted: Boolean,
  mistakes: List<MistakeItem>,
  topicMasteries: List<TopicMastery>,
  onSelectAnswer: (questionId: Int, option: Char, timeSec: Long) -> Unit,
  onNavigateQuestion: (Int) -> Unit,
  onSubmitTest: () -> Unit,
  onResetTest: () -> Unit,
  onAnswerMistake: (item: MistakeItem, char: Char) -> Boolean,
  onQueueWeakTopics: () -> Unit,
  onBack: () -> Unit
) {
  val context = LocalContext.current
  var activeTab by remember { mutableIntStateOf(0) } // 0: Live Exam, 1: Mistake Bank, 2: Mastery Analytics, 3: Formula Sheet
  var selectedMistakeCategory by remember { mutableStateOf("All") }

  // Per-question real-time timer
  var questionSeconds by remember { mutableLongStateOf(0L) }
  LaunchedEffect(currentIndex, isSubmitted) {
    if (!isSubmitted) {
      questionSeconds = questionTimes[questions.getOrNull(currentIndex)?.id ?: 0] ?: 0L
      while (true) {
        delay(1000)
        questionSeconds++
      }
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(ObsidianBackground)
  ) {
    // Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(ObsidianSurface)
        .padding(horizontal = 8.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onBack) {
          Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
        }
        Text(
          text = "JAMB CBT & MISTAKE BANK",
          color = TextPrimary,
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
      }
    }

    // Tab Navigation
    TabRow(
      selectedTabIndex = activeTab,
      containerColor = ObsidianSurface,
      contentColor = ElectricBlue,
      indicator = { tabPositions ->
        TabRowDefaults.SecondaryIndicator(
          modifier = Modifier.tabIndicatorOffset(tabPositions[activeTab]),
          color = ElectricBlue
        )
      }
    ) {
      Tab(
        selected = activeTab == 0,
        onClick = { activeTab = 0 },
        text = { Text("Exam Test", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
      )
      Tab(
        selected = activeTab == 1,
        onClick = { activeTab = 1 },
        text = {
          val count = mistakes.count { !it.cleared }
          Text("Mistakes ($count)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      )
      Tab(
        selected = activeTab == 2,
        onClick = { activeTab = 2 },
        text = { Text("Mastery", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
      )
      Tab(
        selected = activeTab == 3,
        onClick = { activeTab = 3 },
        text = { Text("Formulas", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
      )
    }

    when (activeTab) {
      0 -> {
        // CBT Live Test View
        if (questions.isEmpty()) {
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No active questions in this test session.", color = TextMuted)
          }
        } else {
          val currentQ = questions.getOrElse(currentIndex) { questions.first() }
          val selectedOption = selectedAnswers[currentQ.id]
          val isTimeFlagged = questionSeconds > 120L

          LazyColumn(
            modifier = Modifier
              .fillMaxSize()
              .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            // Test Top Bar: Question Index & Live Question Timer with >120s flag!
            item {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(6.dp))
                      .background(Color(0xFF1E293B))
                      .padding(horizontal = 8.dp, vertical = 4.dp)
                  ) {
                    Text(
                      text = "${currentQ.subject.uppercase()} • ${currentQ.topic}",
                      color = CyanAccent,
                      fontSize = 11.sp,
                      fontFamily = FontFamily.Monospace,
                      fontWeight = FontWeight.Bold
                    )
                  }
                  Spacer(modifier = Modifier.width(6.dp))
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(6.dp))
                      .background(Color(0x333B82F6))
                      .border(1.dp, ElectricBlue, RoundedCornerShape(6.dp))
                      .padding(horizontal = 6.dp, vertical = 3.dp)
                  ) {
                    Text(
                      text = currentQ.examYear,
                      color = ElectricBlue,
                      fontSize = 10.sp,
                      fontFamily = FontFamily.Monospace,
                      fontWeight = FontWeight.ExtraBold
                    )
                  }
                }

                // Question Timer
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isTimeFlagged) Color(0x33EF4444) else Color(0xFF1E293B))
                    .border(1.dp, if (isTimeFlagged) NeonRed else ObsidianCardBorder, RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = null,
                    tint = if (isTimeFlagged) NeonRed else CyanAccent,
                    modifier = Modifier.size(14.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "${questionSeconds / 60}m ${questionSeconds % 60}s",
                    color = if (isTimeFlagged) NeonRed else TextPrimary,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                  )
                  if (isTimeFlagged) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("[>120s!]", color = NeonRed, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                  }
                }
              }
            }

            // 1-40 Question Grid Navigation
            item {
              LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                itemsIndexed(questions) { idx, q ->
                  val isCurr = idx == currentIndex
                  val isAnswered = selectedAnswers.containsKey(q.id)
                  val isCorrect = isSubmitted && selectedAnswers[q.id] == q.correctOption
                  val isWrong = isSubmitted && isAnswered && !isCorrect

                  val boxBg = when {
                    isCurr -> ElectricBlue
                    isSubmitted && isCorrect -> NeonGreen.copy(alpha = 0.3f)
                    isSubmitted && isWrong -> NeonRed.copy(alpha = 0.3f)
                    isAnswered -> CyanAccent.copy(alpha = 0.25f)
                    else -> ObsidianSurface
                  }

                  Box(
                    modifier = Modifier
                      .size(34.dp)
                      .clip(RoundedCornerShape(6.dp))
                      .background(boxBg)
                      .border(1.dp, if (isCurr) ElectricBlue else ObsidianCardBorder, RoundedCornerShape(6.dp))
                      .clickable { onNavigateQuestion(idx) },
                    contentAlignment = Alignment.Center
                  ) {
                    Text(
                      text = "${idx + 1}",
                      color = if (isCurr) Color.White else TextPrimary,
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Bold
                    )
                  }
                }
              }
            }

            // Question Statement Card
            item {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(12.dp))
                  .background(ObsidianSurface)
                  .border(1.dp, ObsidianCardBorder, RoundedCornerShape(12.dp))
                  .padding(16.dp)
              ) {
                Column {
                  Text(
                    text = "Question ${currentIndex + 1} of ${questions.size}",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                  )
                  Spacer(modifier = Modifier.height(8.dp))
                  Text(
                    text = currentQ.questionText,
                    color = TextPrimary,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Medium
                  )
                  if (currentQ.formula != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                      text = "Formula: ${currentQ.formula}",
                      color = CyanAccent,
                      fontSize = 12.sp,
                      fontFamily = FontFamily.Monospace
                    )
                  }
                }
              }
            }

            // Options A, B, C, D
            val options = listOf('A' to currentQ.optionA, 'B' to currentQ.optionB, 'C' to currentQ.optionC, 'D' to currentQ.optionD)
            items(options) { (optChar, optText) ->
              val isSelected = selectedOption == optChar
              val isCorrect = isSubmitted && optChar == currentQ.correctOption
              val isWrongSelected = isSubmitted && isSelected && !isCorrect

              val cardBorder = when {
                isCorrect -> NeonGreen
                isWrongSelected -> NeonRed
                isSelected -> ElectricBlue
                else -> ObsidianCardBorder
              }

              val cardBg = when {
                isCorrect -> NeonGreen.copy(alpha = 0.15f)
                isWrongSelected -> NeonRed.copy(alpha = 0.15f)
                isSelected -> ElectricBlue.copy(alpha = 0.15f)
                else -> ObsidianSurface
              }

              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(10.dp))
                  .background(cardBg)
                  .border(1.dp, cardBorder, RoundedCornerShape(10.dp))
                  .clickable(enabled = !isSubmitted) {
                    onSelectAnswer(currentQ.id, optChar, questionSeconds)
                  }
                  .padding(14.dp)
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Box(
                    modifier = Modifier
                      .size(28.dp)
                      .clip(CircleShape)
                      .background(if (isSelected) ElectricBlue else Color(0xFF1E293B)),
                    contentAlignment = Alignment.Center
                  ) {
                    Text(
                      text = "$optChar",
                      color = if (isSelected) Color.White else TextSecondary,
                      fontSize = 13.sp,
                      fontWeight = FontWeight.Bold
                    )
                  }
                  Spacer(modifier = Modifier.width(12.dp))
                  Text(
                    text = optText,
                    color = TextPrimary,
                    fontSize = 14.sp
                  )
                }
              }
            }

            // Worked Solution (Visible when submitted)
            if (isSubmitted) {
              item {
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF0F172A))
                    .border(1.dp, Color(0x333B82F6), RoundedCornerShape(10.dp))
                    .padding(14.dp)
                ) {
                  Column {
                    Text("WORKED EXPLANATION:", color = CyanAccent, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(currentQ.explanation, color = TextSecondary, fontSize = 13.sp, lineHeight = 18.sp)
                  }
                }
              }
            }

            // Bottom Action: Next, Prev, Submit
            item {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Button(
                  onClick = { onNavigateQuestion(currentIndex - 1) },
                  enabled = currentIndex > 0,
                  colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Text("Previous", color = TextPrimary, fontSize = 12.sp)
                }

                if (!isSubmitted) {
                  Button(
                    onClick = onSubmitTest,
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                    shape = RoundedCornerShape(8.dp)
                  ) {
                    Text("Submit Test", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                  }
                } else {
                  Button(
                    onClick = onResetTest,
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                    shape = RoundedCornerShape(8.dp)
                  ) {
                    Text("Retake Test", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                  }
                }

                Button(
                  onClick = { onNavigateQuestion(currentIndex + 1) },
                  enabled = currentIndex < questions.size - 1,
                  colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Text("Next", color = TextPrimary, fontSize = 12.sp)
                }
              }
            }
          }
        }
      }

      1 -> {
        // Mistake Bank Tab
        val categories = listOf("All") + MistakeCategory.values().map { it.label }
        val filteredMistakes = mistakes.filter { item ->
          !item.cleared && (selectedMistakeCategory == "All" || item.category.equals(selectedMistakeCategory, ignoreCase = true))
        }

        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          item {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(ObsidianSurface)
                .border(1.dp, NeonYellow.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                .padding(14.dp)
            ) {
              Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = NeonYellow, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("MISTAKE BANK (2-CONSECUTIVE CLEAR RULE)", color = NeonYellow, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "Mistakes stay in the bank until answered correctly TWO consecutive times. Re-test by category below.",
                  color = TextSecondary,
                  fontSize = 12.sp
                )
              }
            }
          }

          // Category Chips: Conceptual Gap, Calculation Error, Misread Question, Time Pressure
          item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              items(categories) { cat ->
                val isSelected = selectedMistakeCategory == cat
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) ElectricBlue else Color(0xFF1E293B))
                    .border(1.dp, if (isSelected) ElectricBlue else ObsidianCardBorder, RoundedCornerShape(8.dp))
                    .clickable { selectedMistakeCategory = cat }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                  Text(
                    text = cat,
                    color = if (isSelected) Color.White else TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }
          }

          if (filteredMistakes.isEmpty()) {
            item {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(top = 40.dp),
                contentAlignment = Alignment.Center
              ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(36.dp))
                  Spacer(modifier = Modifier.height(8.dp))
                  Text("No active mistakes in this category!", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                  Text("All errors cleared with 2 consecutive correct answers.", color = TextMuted, fontSize = 12.sp)
                }
              }
            }
          } else {
            items(filteredMistakes, key = { it.id }) { mistake ->
              MistakeCard(
                mistake = mistake,
                onAnswer = { char -> onAnswerMistake(mistake, char) }
              )
            }
          }
        }
      }

      2 -> {
        // Diagnostic Topic Mastery Analytics
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          item {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(ObsidianSurface)
                .border(1.dp, ObsidianCardBorder, RoundedCornerShape(12.dp))
                .padding(16.dp)
            ) {
              Column {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text("DIAGNOSTIC TOPIC MASTERY", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                  Button(
                    onClick = onQueueWeakTopics,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0x333B82F6)),
                    shape = RoundedCornerShape(6.dp)
                  ) {
                    Text("Queue Weak Topics", color = ElectricBlue, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                  }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                  text = "Indicators: Green >75% (Mastered), Yellow 50–74% (Review Needed), Red <50% (Weak Topics).",
                  color = TextMuted,
                  fontSize = 11.sp
                )
              }
            }
          }

          items(topicMasteries) { tm ->
            val color = when (tm.statusColor) {
              "GREEN" -> NeonGreen
              "YELLOW" -> NeonYellow
              else -> NeonRed
            }
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(ObsidianSurface)
                .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
                .padding(14.dp)
            ) {
              Column {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Column {
                    Text(tm.topic, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(tm.subject, color = TextMuted, fontSize = 11.sp)
                  }
                  Text(
                    text = "${"%.0f".format(tm.percentage)}%",
                    color = color,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace
                  )
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                  progress = { tm.percentage / 100f },
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                  color = color,
                  trackColor = Color(0xFF1E293B)
                )
              }
            }
          }
        }
      }

      3 -> {
        // Formula Sheet & PDF Export
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          item {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(ObsidianSurface)
                .border(1.dp, ObsidianCardBorder, RoundedCornerShape(12.dp))
                .padding(16.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text("2-COLUMN STEM FORMULA SHEET", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                  Text("Printable PDF summary for JAMB / UTME", color = TextMuted, fontSize = 11.sp)
                }

                Button(
                  onClick = { exportFormulaSheetPdf(context) },
                  colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Icon(imageVector = Icons.Default.Download, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("Export PDF", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
              }
            }
          }

          items(SeedData.formulaSheetData) { (title, formulas) ->
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(ObsidianSurface)
                .border(1.dp, ObsidianCardBorder, RoundedCornerShape(10.dp))
                .padding(14.dp)
            ) {
              Column {
                Text(title, color = CyanAccent, fontSize = 13.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                Spacer(modifier = Modifier.height(6.dp))
                Text(formulas, color = TextPrimary, fontSize = 12.sp, fontFamily = FontFamily.Monospace, lineHeight = 18.sp)
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun MistakeCard(
  mistake: MistakeItem,
  onAnswer: (Char) -> Boolean
) {
  var selectedChoice by remember { mutableStateOf<Char?>(null) }
  var feedbackCorrect by remember { mutableStateOf<Boolean?>(null) }

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(ObsidianSurface)
      .border(1.dp, ObsidianCardBorder, RoundedCornerShape(12.dp))
      .padding(16.dp)
  ) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0x33EF4444))
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = mistake.category,
            color = NeonRed,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
          )
        }

        // 2-Consecutive streak display!
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (mistake.consecutiveCorrect == 1) Color(0x3310B981) else Color(0xFF1E293B))
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = "Clear Streak: ${mistake.consecutiveCorrect}/2",
            color = if (mistake.consecutiveCorrect == 1) NeonGreen else TextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
          )
        }
      }

      Text(
        text = mistake.questionText,
        color = TextPrimary,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
      )

      // Re-Test Options A, B, C, D
      val choices = listOf('A' to mistake.optionA, 'B' to mistake.optionB, 'C' to mistake.optionC, 'D' to mistake.optionD)
      choices.forEach { (cChar, cText) ->
        val isChosen = selectedChoice == cChar
        val isCorrectAnswer = cChar == mistake.correctOption
        val border = when {
          isChosen && feedbackCorrect == true -> NeonGreen
          isChosen && feedbackCorrect == false -> NeonRed
          else -> Color(0x22FFFFFF)
        }

        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF0F172A))
            .border(1.dp, border, RoundedCornerShape(8.dp))
            .clickable {
              selectedChoice = cChar
              val res = onAnswer(cChar)
              feedbackCorrect = res
            }
            .padding(10.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text("$cChar.", color = CyanAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Text(cText, color = TextPrimary, fontSize = 13.sp)
          }
        }
      }

      if (feedbackCorrect != null) {
        Text(
          text = if (feedbackCorrect == true) "✓ Correct! Streak updated." else "✗ Incorrect. Streak reset to 0.",
          color = if (feedbackCorrect == true) NeonGreen else NeonRed,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}

private fun exportFormulaSheetPdf(context: Context) {
  try {
    val document = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4
    val page = document.startPage(pageInfo)
    val canvas = page.canvas

    val paint = Paint().apply {
      color = AndroidColor.BLACK
      textSize = 14f
      isFakeBoldText = true
    }

    canvas.drawText("MERDD LAB - 2-COLUMN STEM FORMULA SHEET", 40f, 50f, paint)

    paint.textSize = 9f
    paint.isFakeBoldText = false
    canvas.drawText("David & Mercy's Laboratory • JAMB / UTME Physics, Chemistry, Math & Biology", 40f, 68f, paint)

    var yPos = 100f
    SeedData.formulaSheetData.forEachIndexed { idx, (cat, content) ->
      paint.isFakeBoldText = true
      paint.color = AndroidColor.BLUE
      canvas.drawText(cat, 40f, yPos, paint)
      yPos += 14f

      paint.isFakeBoldText = false
      paint.color = AndroidColor.BLACK
      content.split("\n").forEach { line ->
        canvas.drawText(line, 40f, yPos, paint)
        yPos += 12f
      }
      yPos += 10f
    }

    document.finishPage(page)

    val file = File(context.cacheDir, "Merdd_STEM_Formula_Sheet.pdf")
    val fos = FileOutputStream(file)
    document.writeTo(fos)
    document.close()
    fos.close()

    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
      type = "application/pdf"
      putExtra(Intent.EXTRA_STREAM, uri)
      addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(sendIntent, "Share Merdd Formula Sheet"))
  } catch (e: Exception) {
    Toast.makeText(context, "Exported PDF to app cache.", Toast.LENGTH_SHORT).show()
  }
}
