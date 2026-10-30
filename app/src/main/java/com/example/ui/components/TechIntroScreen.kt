package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ObsidianBackground
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import kotlinx.coroutines.delay

@Composable
fun TechIntroScreen(onComplete: () -> Unit) {
  var elapsedMs by remember { mutableFloatStateOf(0f) }
  var statusText by remember { mutableStateOf("INITIALIZING...") }
  var progressPercent by remember { mutableFloatStateOf(0.25f) }

  val infiniteTransition = rememberInfiniteTransition(label = "rings")
  val ringAngle1 by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(tween(4000, easing = LinearEasing), RepeatMode.Restart),
    label = "ring1"
  )
  val ringAngle2 by infiniteTransition.animateFloat(
    initialValue = 360f,
    targetValue = 0f,
    animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Restart),
    label = "ring2"
  )

  LaunchedEffect(Unit) {
    val startTime = System.currentTimeMillis()
    while (elapsedMs < 4000f) {
      delay(50)
      elapsedMs = (System.currentTimeMillis() - startTime).toFloat()
      when {
        elapsedMs < 1000f -> {
          statusText = "INITIALIZING SYSTEM CORE"
          progressPercent = 0.25f
        }
        elapsedMs < 2200f -> {
          statusText = "QUANTUM_CORE_ACTIVE"
          progressPercent = 0.55f
        }
        elapsedMs < 3200f -> {
          statusText = "BIOMETRIC_CHECK_PASS"
          progressPercent = 0.85f
        }
        else -> {
          statusText = "MERDD_LAB_UNLOCKED"
          progressPercent = 1.0f
        }
      }
    }
    delay(200)
    onComplete()
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(ObsidianBackground)
      .padding(24.dp),
    contentAlignment = Alignment.Center
  ) {
    // Cyber HUD Overlay
    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      // Top HUD telemetry
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text("[SYS.VER: 2.0.26]", color = CyanAccent, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
          Text("[OPERATORS: DAVID & MERCY]", color = ElectricBlue, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
        }
        Column(horizontalAlignment = Alignment.End) {
          Text("[COORD: 0x991EX]", color = CyanAccent, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
          Text("STREAM: 10100110", color = TextMuted, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
        }
      }

      // Center Core with Logo & Rotating Rings
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(300.dp),
        contentAlignment = Alignment.Center
      ) {
        // Rotating Ring 1
        Canvas(modifier = Modifier.size(240.dp).rotate(ringAngle1)) {
          drawCircle(
            color = ElectricBlue,
            radius = size.minDimension / 2,
            style = Stroke(
              width = 2.dp.toPx(),
              pathEffect = PathEffect.dashPathEffect(floatArrayOf(30f, 20f, 10f, 20f), 0f)
            )
          )
        }

        // Rotating Ring 2 (counter)
        Canvas(modifier = Modifier.size(200.dp).rotate(ringAngle2)) {
          drawCircle(
            color = CyanAccent,
            radius = size.minDimension / 2,
            style = Stroke(
              width = 1.5.dp.toPx(),
              pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 15f), 0f)
            )
          )
        }

        // Interlocking MD Logo
        Box(
          modifier = Modifier
            .size(130.dp)
            .clip(CircleShape)
            .background(Color.Black)
            .border(2.dp, ElectricBlue, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Image(
            painter = painterResource(id = R.drawable.ic_merdd_md),
            contentDescription = "Merdd MD Logo",
            modifier = Modifier.size(110.dp)
          )
        }
      }

      // Bottom Status & Telemetry
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = statusText,
          color = CyanAccent,
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace,
          letterSpacing = 1.5.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        LinearProgressIndicator(
          progress = { progressPercent },
          modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp)),
          color = ElectricBlue,
          trackColor = Color(0x333B82F6),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "PARSING: ${(progressPercent * 100).toInt()}%",
            color = TextMuted,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
          )
          TextButton(onClick = onComplete) {
            Text("SKIP >>", color = CyanAccent, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
          }
        }
      }
    }
  }
}
