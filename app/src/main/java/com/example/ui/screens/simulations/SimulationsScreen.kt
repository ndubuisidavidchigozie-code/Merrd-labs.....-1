package com.example.ui.screens.simulations

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

data class PhetSimulation(
  val id: Int,
  val title: String,
  val category: String,
  val sub: String
)

@Composable
fun SimulationsScreen(onBack: () -> Unit) {
  val phetSims = listOf(
    PhetSimulation(1, "Pendulum Lab", "Physics", "Energy bar graphs, period timer, damping & gravity"),
    PhetSimulation(2, "Projectile Motion", "Physics", "Cannon angle, drag force, apex marker, target landing"),
    PhetSimulation(3, "Gravity & Orbits", "Astrophysics", "Keplerian elliptical orbits, velocity vectors"),
    PhetSimulation(4, "Double Pendulum RK4", "Chaos Theory", "Symplectic integration, sensitive bifurcation trace"),
    PhetSimulation(5, "Wave Interference", "Optics & Waves", "2-slit coherent wave superpositions & nodal points"),
    PhetSimulation(6, "Charges & Fields", "Electrostatics", "E-field lines, sensor probes, equipotential contours"),
    PhetSimulation(7, "Gas Properties", "Thermodynamics", "Molecular collisions, pressure gauge, heat/cool fire"),
    PhetSimulation(8, "Acid-Base Titration", "Chemistry", "Burette stopcock, pH indicator curve, drop animation"),
    PhetSimulation(9, "Kinetics & Arrhenius", "Chemistry", "Activation energy barrier, molecular collision speed"),
    PhetSimulation(10, "Bohr Rutherford Atom", "Quantum Physics", "Photon emission/absorption orbital spectral jumps")
  )

  var selectedSimId by remember { mutableIntStateOf(1) }

  // PhET Interactive Controls State
  var isPlaying by remember { mutableStateOf(true) }
  var isSlowMotion by remember { mutableStateOf(false) }
  var showEnergyBars by remember { mutableStateOf(true) }
  var showVelocityVector by remember { mutableStateOf(true) }

  // Physical Parameters
  var massKg by remember { mutableFloatStateOf(1.0f) }
  var lengthM by remember { mutableFloatStateOf(1.0f) }
  var gravityName by remember { mutableStateOf("Earth") } // Earth (9.8), Moon (1.6), Jupiter (24.8)
  val gVal = when (gravityName) {
    "Moon" -> 1.62f
    "Jupiter" -> 24.79f
    else -> 9.81f
  }

  // Animation Loop
  val infiniteTransition = rememberInfiniteTransition(label = "phetAnim")
  val animTime by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 2 * PI.toFloat(),
    animationSpec = infiniteRepeatable(
      tween(
        durationMillis = if (isSlowMotion) 4500 else 2200,
        easing = LinearEasing
      )
    ),
    label = "phetLoop"
  )

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
        Column {
          Text(
            text = "PhET-STYLE SIMULATION LABS",
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 1.sp
          )
          Text(
            text = "Interactive STEM Physics & Chemistry Workbenches",
            color = CyanAccent,
            fontSize = 10.sp
          )
        }
      }
    }

    // Horizontal Sim Selector
    LazyRow(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      itemsIndexed(phetSims) { _, sim ->
        val isSel = selectedSimId == sim.id
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSel) ElectricBlue else ObsidianSurface)
            .border(1.dp, if (isSel) ElectricBlue else ObsidianCardBorder, RoundedCornerShape(8.dp))
            .clickable { selectedSimId = sim.id }
            .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Text(
            text = "${sim.id}. ${sim.title}",
            color = if (isSel) Color.White else TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 14.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      // 1. PhET Simulation Stage (Canvas)
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF070B12))
            .border(1.5.dp, Color(0x333B82F6), RoundedCornerShape(14.dp))
        ) {
          Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Laboratory measurement grid
            val gridStep = 25.dp.toPx()
            for (x in 0..w.toInt() step gridStep.toInt()) {
              drawLine(Color(0x0A38BDF8), Offset(x.toFloat(), 0f), Offset(x.toFloat(), h), 1f)
            }
            for (y in 0..h.toInt() step gridStep.toInt()) {
              drawLine(Color(0x0A38BDF8), Offset(0f, y.toFloat()), Offset(w, y.toFloat()), 1f)
            }

            when (selectedSimId) {
              1 -> {
                // Realistic PhET Pendulum Lab
                val pivot = Offset(w * 0.55f, 35f)
                val pendulumLenPx = (lengthM * 110f).coerceIn(60f, 200f)
                val maxAngleRad = 0.65f
                val theta = if (isPlaying) maxAngleRad * cos(animTime * (gVal / 9.8f)) else maxAngleRad * 0.5f

                val bobPos = Offset(
                  pivot.x + pendulumLenPx * sin(theta),
                  pivot.y + pendulumLenPx * cos(theta)
                )

                // Stand & Pivot Mount
                drawLine(Color(0xFF64748B), Offset(pivot.x - 30f, pivot.y), Offset(pivot.x + 30f, pivot.y), 6f)
                drawCircle(Color(0xFFCBD5E1), 6f, pivot)

                // Suspension String
                drawLine(Color(0xFFE2E8F0), pivot, bobPos, strokeWidth = 2.5f)

                // Velocity Vector Arrow if toggled
                if (showVelocityVector) {
                  val vMag = -1.2f * sin(animTime * (gVal / 9.8f)) * 40f
                  val vEnd = Offset(bobPos.x + vMag * cos(theta), bobPos.y - vMag * sin(theta))
                  drawLine(NeonGreen, bobPos, vEnd, strokeWidth = 3f, cap = StrokeCap.Round)
                  drawCircle(NeonGreen, 4f, vEnd)
                }

                // Weighted Bob (size scales with mass)
                val bobRadius = (14f + massKg * 5f).coerceIn(14f, 32f)
                drawCircle(ElectricBlue, radius = bobRadius, center = bobPos)
                drawCircle(Color(0x55FFFFFF), radius = bobRadius * 0.5f, center = Offset(bobPos.x - bobRadius * 0.3f, bobPos.y - bobRadius * 0.3f))

                // PhET Energy Bar Graph (KE, PE, Total E)
                if (showEnergyBars) {
                  val energyBoxLeft = 20f
                  val energyBoxTop = 20f
                  val barWidth = 14f
                  val maxBarH = 120f

                  val peFraction = ((cos(theta) - cos(maxAngleRad)) / (1.0 - cos(maxAngleRad))).toFloat().coerceIn(0f, 1f)
                  val keFraction = (1.0f - peFraction).coerceIn(0f, 1f)

                  // Draw Energy Chart Background Box
                  drawRoundRect(
                    color = Color(0xDD0F172A),
                    topLeft = Offset(energyBoxLeft - 6f, energyBoxTop - 6f),
                    size = Size(90f, maxBarH + 30f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
                  )

                  // KE Bar (Green)
                  val keH = maxBarH * keFraction
                  drawRect(NeonGreen, Offset(energyBoxLeft, energyBoxTop + maxBarH - keH), Size(barWidth, keH))

                  // PE Bar (Blue)
                  val peH = maxBarH * peFraction
                  drawRect(ElectricBlue, Offset(energyBoxLeft + 22f, energyBoxTop + maxBarH - peH), Size(barWidth, peH))

                  // Total E Bar (Yellow)
                  drawRect(NeonYellow, Offset(energyBoxLeft + 44f, energyBoxTop), Size(barWidth, maxBarH))
                }
              }

              2 -> {
                // PhET Projectile Motion (Cannon & Trajectory)
                val cannonBase = Offset(50f, h - 50f)
                val cannonAngleDeg = 45f
                val cannonRad = Math.toRadians(cannonAngleDeg.toDouble()).toFloat()
                val cannonLen = 45f

                // Cannon Barrel
                val barrelEnd = Offset(
                  cannonBase.x + cannonLen * cos(cannonRad),
                  cannonBase.y - cannonLen * sin(cannonRad)
                )
                drawLine(Color(0xFF94A3B8), cannonBase, barrelEnd, strokeWidth = 12f, cap = StrokeCap.Round)
                drawCircle(Color(0xFF475569), 16f, cannonBase)

                // Parabolic Trajectory
                val traj = Path().apply {
                  moveTo(barrelEnd.x, barrelEnd.y)
                  quadraticTo(w * 0.5f, 40f, w * 0.88f, h - 50f)
                }
                drawPath(traj, CyanAccent, style = Stroke(width = 2.5f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 6f), 0f)))

                // Target
                drawCircle(NeonRed, 16f, Offset(w * 0.88f, h - 50f), style = Stroke(3f))
                drawCircle(Color.White, 8f, Offset(w * 0.88f, h - 50f), style = Stroke(2f))
                drawCircle(NeonRed, 3f, Offset(w * 0.88f, h - 50f))

                // Flying projectile
                val projProgress = (animTime / (2 * PI.toFloat())).coerceIn(0f, 1f)
                val px = barrelEnd.x + (w * 0.88f - barrelEnd.x) * projProgress
                val py = barrelEnd.y + (4 * (40f - barrelEnd.y) * projProgress * (1 - projProgress)) + (h - 50f - barrelEnd.y) * projProgress * projProgress
                drawCircle(NeonYellow, 7f, Offset(px, py))
              }

              8 -> {
                // PhET Acid-Base Titration Lab
                val buretteX = w * 0.5f
                // Burette Tube
                drawRect(Color(0x3338BDF8), Offset(buretteX - 12f, 20f), Size(24f, 160f))
                drawRect(Color(0xFF38BDF8), Offset(buretteX - 12f, 20f), Size(24f, 160f), style = Stroke(1.5f))

                // Conical Flask below
                val flaskPath = Path().apply {
                  moveTo(buretteX - 16f, 210f)
                  lineTo(buretteX - 50f, h - 30f)
                  lineTo(buretteX + 50f, h - 30f)
                  lineTo(buretteX + 16f, 210f)
                  close()
                }
                // Solution inside flask (color shifts from clear to bright pink phenolphthalein)
                val pinkAlpha = (animTime / (2 * PI.toFloat())).coerceIn(0f, 1f)
                drawPath(flaskPath, Color(0xFFEC4899).copy(alpha = pinkAlpha * 0.7f))
                drawPath(flaskPath, Color(0xFF64748B), style = Stroke(2f))

                // Falling drop
                val dropY = 180f + ((animTime * 60f) % 30f)
                drawCircle(Color(0xFF38BDF8), 3.5f, Offset(buretteX, dropY))
              }

              else -> {
                // Interactive Harmonic Wave & Field
                val center = Offset(w / 2, h / 2)
                drawCircle(ElectricBlue, 40f * massKg, center, style = Stroke(3f))
                drawCircle(CyanAccent, 80f * lengthM, center, style = Stroke(2f))
                drawCircle(NeonPurple, 120f, center, style = Stroke(1.5f))
              }
            }
          }

          // Top Right PhET Telemetry Overlay
          Box(
            modifier = Modifier
              .align(Alignment.TopEnd)
              .padding(10.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xEE0F172A))
              .border(1.dp, Color(0x333B82F6), RoundedCornerShape(8.dp))
              .padding(horizontal = 10.dp, vertical = 6.dp)
          ) {
            Column {
              Text(
                text = "g = ${gVal} m/s² ($gravityName)",
                color = CyanAccent,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "L = ${"%.2f".format(lengthM)}m • m = ${"%.1f".format(massKg)}kg",
                color = TextSecondary,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
              )
            }
          }
        }
      }

      // 2. PhET Control Panel Bar (Play/Pause, Step, Slow-Mo)
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ObsidianSurface)
            .border(1.dp, ObsidianCardBorder, RoundedCornerShape(12.dp))
            .padding(12.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
              // Play / Pause
              Box(
                modifier = Modifier
                  .size(38.dp)
                  .clip(CircleShape)
                  .background(if (isPlaying) Color(0xFF1E293B) else ElectricBlue)
                  .clickable { isPlaying = !isPlaying },
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                  contentDescription = "Play/Pause",
                  tint = Color.White,
                  modifier = Modifier.size(20.dp)
                )
              }

              // Step Frame
              Box(
                modifier = Modifier
                  .size(38.dp)
                  .clip(CircleShape)
                  .background(Color(0xFF1E293B))
                  .clickable { isPlaying = false },
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.SkipNext,
                  contentDescription = "Step",
                  tint = TextPrimary,
                  modifier = Modifier.size(20.dp)
                )
              }

              // Slow Motion Toggle
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(if (isSlowMotion) CyanAccent.copy(alpha = 0.2f) else Color(0xFF1E293B))
                  .border(1.dp, if (isSlowMotion) CyanAccent else Color.Transparent, RoundedCornerShape(8.dp))
                  .clickable { isSlowMotion = !isSlowMotion }
                  .padding(horizontal = 8.dp, vertical = 6.dp)
              ) {
                Text(
                  text = if (isSlowMotion) "0.5x Slow" else "1.0x Normal",
                  color = if (isSlowMotion) CyanAccent else TextSecondary,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            // Gravity Presets (Earth, Moon, Jupiter)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              listOf("Earth", "Moon", "Jupiter").forEach { gName ->
                val isSel = gravityName == gName
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isSel) ElectricBlue else Color(0xFF1E293B))
                    .clickable { gravityName = gName }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  Text(gName, color = if (isSel) Color.White else TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }
      }

      // 3. Physical Adjusters (Length & Mass Sliders)
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ObsidianSurface)
            .border(1.dp, ObsidianCardBorder, RoundedCornerShape(12.dp))
            .padding(14.dp)
        ) {
          Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Length Slider
            Column {
              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("String Length / Scale (L)", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text("${"%.2f".format(lengthM)} m", color = CyanAccent, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
              }
              Slider(
                value = lengthM,
                onValueChange = { lengthM = it },
                valueRange = 0.4f..2.0f,
                colors = SliderDefaults.colors(thumbColor = CyanAccent, activeTrackColor = CyanAccent)
              )
            }

            // Mass Slider
            Column {
              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Bob Mass (m)", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text("${"%.2f".format(massKg)} kg", color = ElectricBlue, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
              }
              Slider(
                value = massKg,
                onValueChange = { massKg = it },
                valueRange = 0.2f..3.0f,
                colors = SliderDefaults.colors(thumbColor = ElectricBlue, activeTrackColor = ElectricBlue)
              )
            }

            // Vector & Energy Toggles
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(if (showEnergyBars) NeonGreen.copy(alpha = 0.2f) else Color(0xFF1E293B))
                  .border(1.dp, if (showEnergyBars) NeonGreen else Color.Transparent, RoundedCornerShape(6.dp))
                  .clickable { showEnergyBars = !showEnergyBars }
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Text("Energy Histogram", color = if (showEnergyBars) NeonGreen else TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
              }

              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(if (showVelocityVector) NeonYellow.copy(alpha = 0.2f) else Color(0xFF1E293B))
                  .border(1.dp, if (showVelocityVector) NeonYellow else Color.Transparent, RoundedCornerShape(6.dp))
                  .clickable { showVelocityVector = !showVelocityVector }
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Text("Velocity Vector", color = if (showVelocityVector) NeonYellow else TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }
}
