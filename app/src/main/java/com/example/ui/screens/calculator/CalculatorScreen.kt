package com.example.ui.screens.calculator

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
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
import com.example.util.CalculatorEngine

@Composable
fun CalculatorScreen(
  display: String,
  subDisplay: String,
  isDegreeMode: Boolean,
  isShiftActive: Boolean,
  isAlphaActive: Boolean,
  calcHistory: List<String>,
  storageInfo: Pair<String, Float>,
  onToken: (String) -> Unit,
  onClear: () -> Unit,
  onDelete: () -> Unit,
  onEquals: () -> Unit,
  onToggleDegree: () -> Unit,
  onToggleShift: () -> Unit,
  onToggleAlpha: () -> Unit,
  onPurgeHistory: () -> Unit,
  onBack: () -> Unit
) {
  var selectedSubTab by remember { mutableIntStateOf(0) } // 0: SC-991EX Keypad, 1: Statistics Mode, 2: History & Storage
  var showPurgeConfirmDialog by remember { mutableStateOf(false) }
  var isMerddLoading by remember { mutableStateOf(true) }

  // Statistics input state
  var statsInput by remember { mutableStateOf("2, 3, 3, 4") }
  var statsResult by remember { mutableStateOf(CalculatorEngine.computeStatistics(listOf(2.0, 3.0, 3.0, 4.0))) }

  androidx.compose.runtime.LaunchedEffect(Unit) {
    kotlinx.coroutines.delay(1200)
    isMerddLoading = false
  }

  if (isMerddLoading) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(ObsidianBackground),
      contentAlignment = Alignment.Center
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        Box(
          modifier = Modifier
            .size(90.dp)
            .clip(CircleShape)
            .background(Color.Black)
            .border(2.dp, ElectricBlue, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Image(
            painter = painterResource(id = R.drawable.ic_merdd_md),
            contentDescription = "merdd",
            modifier = Modifier.size(72.dp)
          )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
          text = "merdd",
          color = TextPrimary,
          fontSize = 28.sp,
          fontWeight = FontWeight.ExtraBold,
          letterSpacing = 3.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = "INITIALIZING SC-991EX ENGINE",
          color = CyanAccent,
          fontSize = 11.sp,
          fontFamily = FontFamily.Monospace,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(18.dp))

        LinearProgressIndicator(
          modifier = Modifier
            .width(180.dp)
            .height(4.dp)
            .clip(RoundedCornerShape(2.dp)),
          color = ElectricBlue,
          trackColor = Color(0x333B82F6)
        )
      }
    }
  } else {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(ObsidianBackground)
  ) {
    // Top Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(ObsidianSurface)
        .padding(horizontal = 8.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = onBack) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
      }
      Text(
        text = "MERDD SC-991EX SCIENTIFIC",
        color = TextPrimary,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Monospace,
        letterSpacing = 1.sp
      )
    }

    // Sub Navigation Tabs
    TabRow(
      selectedTabIndex = selectedSubTab,
      containerColor = ObsidianSurface,
      contentColor = ElectricBlue,
      indicator = { tabPositions ->
        TabRowDefaults.SecondaryIndicator(
          modifier = Modifier.tabIndicatorOffset(tabPositions[selectedSubTab]),
          color = ElectricBlue
        )
      }
    ) {
      Tab(
        selected = selectedSubTab == 0,
        onClick = { selectedSubTab = 0 },
        text = { Text("Calculator", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
      )
      Tab(
        selected = selectedSubTab == 1,
        onClick = { selectedSubTab = 1 },
        text = { Text("Statistics", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
      )
      Tab(
        selected = selectedSubTab == 2,
        onClick = { selectedSubTab = 2 },
        text = { Text("Memory & Storage", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
      )
    }

    when (selectedSubTab) {
      0 -> {
        // Main SC-991EX Layout
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // Obsidian Display Box
          item {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF070B12))
                .border(1.5.dp, Color(0x333B82F6), RoundedCornerShape(12.dp))
                .padding(14.dp)
            ) {
              Column {
                // Indicators Row
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                      text = if (isDegreeMode) "DEG" else "RAD",
                      color = CyanAccent,
                      fontSize = 10.sp,
                      fontFamily = FontFamily.Monospace,
                      fontWeight = FontWeight.Bold
                    )
                    if (isShiftActive) {
                      Text(
                        text = "SHIFT",
                        color = NeonYellow,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                      )
                    }
                    if (isAlphaActive) {
                      Text(
                        text = "ALPHA",
                        color = NeonPurple,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                      )
                    }
                  }
                  Text(
                    text = "NATURAL V.P.A.M.",
                    color = TextMuted,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                  )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Sub Display
                Text(
                  text = subDisplay.ifBlank { " " },
                  color = TextMuted,
                  fontSize = 13.sp,
                  fontFamily = FontFamily.Monospace,
                  textAlign = TextAlign.End,
                  modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Main Math Result Display
                Text(
                  text = display,
                  color = TextPrimary,
                  fontSize = 26.sp,
                  fontWeight = FontWeight.Bold,
                  fontFamily = FontFamily.Monospace,
                  textAlign = TextAlign.End,
                  maxLines = 2,
                  modifier = Modifier.fillMaxWidth()
                )
              }
            }
          }

          // Header Row: [SHIFT][ALPHA] | Central D-Pad / MD Logo | [MODE][ON]
          item {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                CalcKey("SHIFT", color = if (isShiftActive) NeonYellow else Color(0xFFD97706), onClick = onToggleShift, width = 50.dp)
                CalcKey("ALPHA", color = if (isAlphaActive) NeonPurple else Color(0xFF7C3AED), onClick = onToggleAlpha, width = 50.dp)
              }

              // Central D-Pad with Interlocking MD Logo
              Box(
                modifier = Modifier
                  .size(46.dp)
                  .clip(CircleShape)
                  .background(Color(0xFF1E293B))
                  .border(1.5.dp, ElectricBlue, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Image(
                  painter = painterResource(id = R.drawable.ic_merdd_md),
                  contentDescription = "MD Core",
                  modifier = Modifier.size(36.dp)
                )
              }

              Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                CalcKey("MODE", color = TextSecondary, onClick = onToggleDegree, width = 50.dp)
                CalcKey("ON", color = NeonRed, onClick = onClear, width = 50.dp)
              }
            }
          }

          // Subheader: [CALC][∫][log₁₀][ln]
          item {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              CalcKey("CALC", modifier = Modifier.weight(1f), onClick = onEquals)
              CalcKey("∫", modifier = Modifier.weight(1f), onClick = { onToken("∫") })
              CalcKey("log₁₀", modifier = Modifier.weight(1f), onClick = { onToken("log(") })
              CalcKey("ln", modifier = Modifier.weight(1f), onClick = { onToken("ln(") })
            }
          }

          // 6-Column Row 1: [x²][x³][xʸ][√][∛][^]
          item {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
              CalcKey("x²", modifier = Modifier.weight(1f), onClick = { onToken("^2") })
              CalcKey("x³", modifier = Modifier.weight(1f), onClick = { onToken("^3") })
              CalcKey("xʸ", modifier = Modifier.weight(1f), onClick = { onToken("^") })
              CalcKey("√", modifier = Modifier.weight(1f), onClick = { onToken("√(") })
              CalcKey("∛", modifier = Modifier.weight(1f), onClick = { onToken("∛(") })
              CalcKey("^", modifier = Modifier.weight(1f), onClick = { onToken("^") })
            }
          }

          // 6-Column Row 2: [sin][cos][tan][hyp][!][()]
          item {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
              CalcKey("sin", modifier = Modifier.weight(1f), onClick = { onToken("sin(") })
              CalcKey("cos", modifier = Modifier.weight(1f), onClick = { onToken("cos(") })
              CalcKey("tan", modifier = Modifier.weight(1f), onClick = { onToken("tan(") })
              CalcKey("hyp", modifier = Modifier.weight(1f), onClick = { onToken("sinh(") })
              CalcKey("!", modifier = Modifier.weight(1f), onClick = { onToken("!") })
              CalcKey("(", modifier = Modifier.weight(0.5f), onClick = { onToken("(") })
              CalcKey(")", modifier = Modifier.weight(0.5f), onClick = { onToken(")") })
            }
          }

          // 6-Column Row 3: [STO][RCL][M+][M-][ANS][DEL]
          item {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
              CalcKey("STO", modifier = Modifier.weight(1f), onClick = { /* Memory store */ })
              CalcKey("RCL", modifier = Modifier.weight(1f), onClick = { /* Memory recall */ })
              CalcKey("M+", modifier = Modifier.weight(1f), onClick = { onToken("+ANS") })
              CalcKey("M-", modifier = Modifier.weight(1f), onClick = { onToken("-ANS") })
              CalcKey("ANS", color = CyanAccent, modifier = Modifier.weight(1f), onClick = { onToken("ANS") })
              CalcKey("DEL", color = NeonRed, modifier = Modifier.weight(1f), onClick = onDelete)
            }
          }

          // 5-Column Keypad Rows:
          // Row 1: [7][8][9][÷][π]
          item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              NumKey("7", Modifier.weight(1f)) { onToken("7") }
              NumKey("8", Modifier.weight(1f)) { onToken("8") }
              NumKey("9", Modifier.weight(1f)) { onToken("9") }
              OpKey("÷", Modifier.weight(1f)) { onToken("÷") }
              FuncKey("π", Modifier.weight(1f)) { onToken("π") }
            }
          }

          // Row 2: [4][5][6][×][e]
          item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              NumKey("4", Modifier.weight(1f)) { onToken("4") }
              NumKey("5", Modifier.weight(1f)) { onToken("5") }
              NumKey("6", Modifier.weight(1f)) { onToken("6") }
              OpKey("×", Modifier.weight(1f)) { onToken("×") }
              FuncKey("e", Modifier.weight(1f)) { onToken("e") }
            }
          }

          // Row 3: [1][2][3][−][%]
          item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              NumKey("1", Modifier.weight(1f)) { onToken("1") }
              NumKey("2", Modifier.weight(1f)) { onToken("2") }
              NumKey("3", Modifier.weight(1f)) { onToken("3") }
              OpKey("−", Modifier.weight(1f)) { onToken("−") }
              FuncKey("%", Modifier.weight(1f)) { onToken("%") }
            }
          }

          // Row 4: [0][.][EXP][+][=]
          item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              NumKey("0", Modifier.weight(1f)) { onToken("0") }
              NumKey(".", Modifier.weight(1f)) { onToken(".") }
              FuncKey("EXP", Modifier.weight(1f)) { onToken("*10^") }
              OpKey("+", Modifier.weight(1f)) { onToken("+") }
              Box(
                modifier = Modifier
                  .weight(1f)
                  .height(48.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(ElectricBlue)
                  .clickable { onEquals() },
                contentAlignment = Alignment.Center
              ) {
                Text("=", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }

      1 -> {
        // Statistics Module
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          Text(
            text = "STATISTICS & DESCRIPTIVE ANALYSIS",
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )

          Text(
            text = "Enter comma-separated numbers (CSV). Instant calculation of Mean, Median, Mode, Range and Variance.",
            color = TextSecondary,
            fontSize = 12.sp
          )

          OutlinedTextField(
            value = statsInput,
            onValueChange = {
              statsInput = it
              val parsed = it.split(",", " ", ";")
                .mapNotNull { token -> token.trim().toDoubleOrNull() }
              statsResult = CalculatorEngine.computeStatistics(parsed)
            },
            placeholder = { Text("e.g. 2, 3, 3, 4", color = TextMuted) },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = ObsidianSurface,
              unfocusedContainerColor = ObsidianSurface,
              focusedBorderColor = ElectricBlue,
              unfocusedBorderColor = ObsidianCardBorder,
              focusedTextColor = TextPrimary,
              unfocusedTextColor = TextPrimary
            ),
            modifier = Modifier.fillMaxWidth()
          )

          // Presets testing prompt edge cases:
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            listOf("2, 3, 3, 4", "1, 1, 2, 2, 3", "1, 2, 3, 4").forEach { preset ->
              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(6.dp))
                  .background(Color(0xFF1E293B))
                  .clickable {
                    statsInput = preset
                    val parsed = preset.split(",", " ")
                      .mapNotNull { it.trim().toDoubleOrNull() }
                    statsResult = CalculatorEngine.computeStatistics(parsed)
                  }
                  .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "[$preset]",
                  color = CyanAccent,
                  fontSize = 10.sp,
                  fontFamily = FontFamily.Monospace
                )
              }
            }
          }

          if (statsResult != null) {
            val res = statsResult!!
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(ObsidianSurface)
                .border(1.dp, ObsidianCardBorder, RoundedCornerShape(12.dp))
                .padding(16.dp)
            ) {
              Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("DESCRIPTIVE SUMMARY (N = ${res.count})", color = ElectricBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                StatRow("Arithmetic Mean (x̄)", "%.4f".format(res.mean))
                StatRow("Median (Q2)", "%.4f".format(res.median))
                StatRow("Mode", res.mode)
                StatRow("Range (Max - Min)", "%.4f".format(res.range))
                StatRow("Standard Deviation (σ)", "%.4f".format(res.stdDev))
                StatRow("Variance (σ²)", "%.4f".format(res.variance))
              }
            }
          }
        }
      }

      2 -> {
        // Memory, History & Storage
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          // Storage Usage Tracker
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
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("LAB STORAGE QUOTA", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(storageInfo.first, color = CyanAccent, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
              }
              Spacer(modifier = Modifier.height(10.dp))
              LinearProgressIndicator(
                progress = { storageInfo.second },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(8.dp)
                  .clip(RoundedCornerShape(4.dp)),
                color = if (storageInfo.second > 0.8f) NeonRed else ElectricBlue,
                trackColor = Color(0xFF1E293B)
              )

              if (storageInfo.second > 0.8f) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = NeonRed, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("Storage warning: usage exceeds 80%", color = NeonRed, fontSize = 11.sp)
                }
              }

              Spacer(modifier = Modifier.height(14.dp))

              Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                  onClick = { showPurgeConfirmDialog = true },
                  colors = ButtonDefaults.buttonColors(containerColor = Color(0x33EF4444)),
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = NeonRed, modifier = Modifier.size(14.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("Purge Calculation History", color = NeonRed, fontSize = 11.sp)
                }
              }
            }
          }

          Text(
            text = "CALCULATION HISTORY",
            color = TextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )

          if (calcHistory.isEmpty()) {
            Text("No recent calculations recorded.", color = TextMuted, fontSize = 13.sp)
          } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
              items(calcHistory) { item ->
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(ObsidianSurface)
                    .padding(12.dp)
                ) {
                  Text(text = item, color = TextPrimary, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
                }
              }
            }
          }
        }
      }
    }
  }

  if (showPurgeConfirmDialog) {
    AlertDialog(
      onDismissRequest = { showPurgeConfirmDialog = false },
      title = { Text("Confirm Destructive Purge", color = TextPrimary) },
      text = { Text("Are you sure you want to permanently delete all stored calculation history logs? This action cannot be undone.", color = TextSecondary) },
      confirmButton = {
        Button(
          onClick = {
            onPurgeHistory()
            showPurgeConfirmDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = NeonRed)
        ) {
          Text("Purge", color = Color.White)
        }
      },
      dismissButton = {
        TextButton(onClick = { showPurgeConfirmDialog = false }) {
          Text("Cancel", color = TextMuted)
        }
      },
      containerColor = ObsidianSurface
    )
  }
  }
}

@Composable
private fun StatRow(label: String, value: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(label, color = TextSecondary, fontSize = 13.sp)
    Text(value, color = TextPrimary, fontSize = 13.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
  }
}

@Composable
private fun CalcKey(
  text: String,
  modifier: Modifier = Modifier,
  width: androidx.compose.ui.unit.Dp? = null,
  color: Color = TextPrimary,
  onClick: () -> Unit
) {
  val baseMod = if (width != null) modifier.width(width) else modifier
  Box(
    modifier = baseMod
      .height(38.dp)
      .clip(RoundedCornerShape(6.dp))
      .background(Color(0xFF1E293B))
      .clickable { onClick() },
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = text,
      color = color,
      fontSize = 12.sp,
      fontWeight = FontWeight.Bold,
      fontFamily = FontFamily.Monospace
    )
  }
}

@Composable
private fun NumKey(text: String, modifier: Modifier, onClick: () -> Unit) {
  Box(
    modifier = modifier
      .height(48.dp)
      .clip(RoundedCornerShape(8.dp))
      .background(Color(0xFF111827))
      .border(1.dp, Color(0x22FFFFFF), RoundedCornerShape(8.dp))
      .clickable { onClick() },
    contentAlignment = Alignment.Center
  ) {
    Text(text = text, color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
  }
}

@Composable
private fun OpKey(text: String, modifier: Modifier, onClick: () -> Unit) {
  Box(
    modifier = modifier
      .height(48.dp)
      .clip(RoundedCornerShape(8.dp))
      .background(Color(0xFF1E293B))
      .clickable { onClick() },
    contentAlignment = Alignment.Center
  ) {
    Text(text = text, color = ElectricBlue, fontSize = 18.sp, fontWeight = FontWeight.Bold)
  }
}

@Composable
private fun FuncKey(text: String, modifier: Modifier, onClick: () -> Unit) {
  Box(
    modifier = modifier
      .height(48.dp)
      .clip(RoundedCornerShape(8.dp))
      .background(Color(0xFF0F172A))
      .border(1.dp, Color(0x333B82F6), RoundedCornerShape(8.dp))
      .clickable { onClick() },
    contentAlignment = Alignment.Center
  ) {
    Text(text = text, color = CyanAccent, fontSize = 14.sp, fontWeight = FontWeight.Medium)
  }
}
