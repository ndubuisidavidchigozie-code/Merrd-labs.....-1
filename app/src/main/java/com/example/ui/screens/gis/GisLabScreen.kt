package com.example.ui.screens.gis

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SeedData
import com.example.model.GisFeature
import com.example.model.NigerianState
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
import kotlin.math.abs
import kotlin.math.hypot

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GisLabScreen(onBack: () -> Unit) {
  var preset by remember { mutableStateOf("Nigeria") } // "Nigeria", "West Africa", "Full Africa"
  var mapType by remember { mutableStateOf("Vector") } // "Vector", "Satellite", "Terrain"
  var searchQuery by remember { mutableStateOf("") }

  var showRivers by remember { mutableStateOf(true) }
  var showRelief by remember { mutableStateOf(true) }
  var showMinerals by remember { mutableStateOf(true) }
  var showVegetation by remember { mutableStateOf(true) }
  var showStateLines by remember { mutableStateOf(true) }

  // Hotspot drawer selection
  var selectedFeature by remember { mutableStateOf<GisFeature?>(null) }
  var selectedState by remember { mutableStateOf<NigerianState?>(SeedData.nigerianStates.find { it.name == "FCT (Abuja)" }) }
  var showToolModal by remember { mutableStateOf<String?>(null) } // "TIME_SOLVER", "SCALE_MEASURE"

  // Google Maps Style Pan & Zoom
  var scale by remember { mutableFloatStateOf(1f) }
  var offset by remember { mutableStateOf(Offset.Zero) }
  val transformableState = rememberTransformableState { zoomChange, offsetChange, _ ->
    scale = (scale * zoomChange).coerceIn(0.8f, 5f)
    offset += offsetChange
  }

  // Distance Measurement Tap Points
  var tapPointA by remember { mutableStateOf<Offset?>(null) }
  var tapPointB by remember { mutableStateOf<Offset?>(null) }

  val filteredStates = SeedData.nigerianStates.filter {
    searchQuery.isBlank() || it.name.contains(searchQuery, ignoreCase = true) || it.capital.contains(searchQuery, ignoreCase = true)
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFF0F172A))
  ) {
    // 1. Google Maps Styled Interactive Canvas
    Box(
      modifier = Modifier
        .fillMaxSize()
        .transformable(state = transformableState)
        .pointerInput(Unit) {
          detectTapGestures { tapOffset ->
            if (tapPointA == null) {
              tapPointA = tapOffset
            } else if (tapPointB == null) {
              tapPointB = tapOffset
            } else {
              tapPointA = tapOffset
              tapPointB = null
            }
          }
        }
    ) {
      Canvas(
        modifier = Modifier
          .fillMaxSize()
          .graphicsLayer(
            scaleX = scale,
            scaleY = scale,
            translationX = offset.x,
            translationY = offset.y
          )
      ) {
        val w = size.width
        val h = size.height

        // Canvas Background based on Map Type (Satellite vs Vector vs Terrain)
        val bgColor = when (mapType) {
          "Satellite" -> Color(0xFF050811)
          "Terrain" -> Color(0xFF0B132B)
          else -> Color(0xFF0A0E17)
        }
        drawRect(bgColor)

        // Subtle Modern Grid Lines
        val step = 48.dp.toPx()
        for (x in 0..w.toInt() step step.toInt()) {
          drawLine(Color(0x0E38BDF8), Offset(x.toFloat(), 0f), Offset(x.toFloat(), h), 1f)
        }
        for (y in 0..h.toInt() step step.toInt()) {
          drawLine(Color(0x0E38BDF8), Offset(0f, y.toFloat()), Offset(w, y.toFloat()), 1f)
        }

        if (preset == "Full Africa") {
          // Africa Continental Vector
          val africaPath = Path().apply {
            moveTo(w * 0.15f, h * 0.25f)
            lineTo(w * 0.40f, h * 0.15f)
            lineTo(w * 0.65f, h * 0.18f)
            lineTo(w * 0.85f, h * 0.38f)
            lineTo(w * 0.75f, h * 0.65f)
            lineTo(w * 0.55f, h * 0.88f)
            lineTo(w * 0.38f, h * 0.70f)
            lineTo(w * 0.35f, h * 0.50f)
            lineTo(w * 0.12f, h * 0.45f)
            close()
          }
          drawPath(africaPath, Color(0x1F38BDF8))
          drawPath(africaPath, CyanAccent, style = Stroke(width = 2.5.dp.toPx()))
        } else {
          // High-Detail Nigeria Boundary
          val nigeriaPath = Path().apply {
            moveTo(w * 0.28f, h * 0.22f)
            lineTo(w * 0.65f, h * 0.19f)
            lineTo(w * 0.90f, h * 0.24f)
            lineTo(w * 0.82f, h * 0.50f)
            lineTo(w * 0.70f, h * 0.74f)
            lineTo(w * 0.50f, h * 0.85f)
            lineTo(w * 0.25f, h * 0.73f)
            lineTo(w * 0.22f, h * 0.55f)
            close()
          }

          if (showVegetation) {
            // Mangrove Tidal Swamp Zone (Southern coastal ribbon)
            drawRect(
              color = Color(0x2810B981),
              topLeft = Offset(w * 0.25f, h * 0.73f),
              size = androidx.compose.ui.geometry.Size(w * 0.45f, h * 0.12f)
            )
            // Guinea & Sudan Savanna (Middle & North)
            drawRect(
              color = Color(0x20F59E0B),
              topLeft = Offset(w * 0.25f, h * 0.22f),
              size = androidx.compose.ui.geometry.Size(w * 0.65f, h * 0.35f)
            )
          }

          drawPath(nigeriaPath, Color(0x261E3A8A))
          drawPath(nigeriaPath, Color(0xFF60A5FA), style = Stroke(width = 2.5.dp.toPx()))

          // State Lines & Pins
          if (showStateLines) {
            SeedData.nigerianStates.forEach { st ->
              val px = st.relX * w
              val py = st.relY * h
              val isCurrent = selectedState?.name == st.name

              // Google Maps Style Pulsing Pin
              if (isCurrent) {
                drawCircle(
                  color = ElectricBlue.copy(alpha = 0.3f),
                  radius = 16.dp.toPx(),
                  center = Offset(px, py)
                )
                drawCircle(
                  color = ElectricBlue,
                  radius = 8.dp.toPx(),
                  center = Offset(px, py)
                )
                drawCircle(
                  color = Color.White,
                  radius = 3.dp.toPx(),
                  center = Offset(px, py)
                )
              } else {
                drawCircle(
                  color = Color(0x9960A5FA),
                  radius = 3.5.dp.toPx(),
                  center = Offset(px, py)
                )
              }
            }
          }
        }

        // River Systems (Cyan Glowing Arteries)
        if (showRivers) {
          val nigerPath = Path().apply {
            moveTo(w * 0.30f, h * 0.26f)
            quadraticTo(w * 0.38f, h * 0.46f, w * 0.52f, h * 0.58f)
          }
          drawPath(nigerPath, Color(0xFF38BDF8), style = Stroke(width = 3.5.dp.toPx()))

          val benuePath = Path().apply {
            moveTo(w * 0.82f, h * 0.44f)
            quadraticTo(w * 0.68f, h * 0.52f, w * 0.52f, h * 0.58f)
          }
          drawPath(benuePath, Color(0xFF38BDF8), style = Stroke(width = 3.5.dp.toPx()))

          val deltaPath = Path().apply {
            moveTo(w * 0.52f, h * 0.58f)
            lineTo(w * 0.50f, h * 0.82f)
          }
          drawPath(deltaPath, Color(0xFF38BDF8), style = Stroke(width = 4.5.dp.toPx()))
        }

        // Relief Highlights
        if (showRelief) {
          drawCircle(NeonPurple.copy(alpha = 0.4f), 18.dp.toPx(), Offset(w * 0.63f, h * 0.45f)) // Jos
          drawCircle(NeonPurple.copy(alpha = 0.4f), 14.dp.toPx(), Offset(w * 0.70f, h * 0.74f)) // Obudu
          drawCircle(NeonPurple.copy(alpha = 0.4f), 18.dp.toPx(), Offset(w * 0.84f, h * 0.43f)) // Mandara
        }

        // Feature Hotspot Pins
        SeedData.gisFeatures.forEach { feat ->
          val isAllowed = when (feat.layer) {
            "RIVERS" -> showRivers
            "RELIEF" -> showRelief
            "MINERALS" -> showMinerals
            "AFRICA" -> preset == "Full Africa"
            else -> true
          }
          if (isAllowed) {
            val fx = feat.posX * w
            val fy = feat.posY * h
            val pinColor = when (feat.layer) {
              "MINERALS" -> NeonRed
              "RIVERS" -> CyanAccent
              "RELIEF" -> NeonPurple
              else -> NeonGreen
            }

            drawCircle(pinColor.copy(alpha = 0.35f), 10.dp.toPx(), Offset(fx, fy))
            drawCircle(pinColor, 5.dp.toPx(), Offset(fx, fy))
            drawCircle(Color.White, 2.dp.toPx(), Offset(fx, fy))
          }
        }

        // Distance Measurement Line
        if (tapPointA != null && tapPointB != null) {
          drawLine(
            color = NeonYellow,
            start = tapPointA!!,
            end = tapPointB!!,
            strokeWidth = 2.5.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)
          )
          drawCircle(color = NeonYellow, radius = 6.dp.toPx(), center = tapPointA!!)
          drawCircle(color = NeonYellow, radius = 6.dp.toPx(), center = tapPointB!!)
        }
      }
    }

    // 2. Google Maps Floating Top Search Bar
    Column(
      modifier = Modifier
        .align(Alignment.TopCenter)
        .padding(top = 16.dp, start = 12.dp, end = 12.dp)
        .fillMaxWidth()
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(24.dp))
          .background(Color(0xF0111827))
          .border(1.dp, Color(0x3338BDF8), RoundedCornerShape(24.dp))
          .shadow(8.dp, RoundedCornerShape(24.dp))
          .padding(horizontal = 6.dp, vertical = 2.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(onClick = onBack) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
          }

          OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search Nigeria states, rivers, capitals...", color = TextMuted, fontSize = 13.sp) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = Color.Transparent,
              unfocusedContainerColor = Color.Transparent,
              focusedBorderColor = Color.Transparent,
              unfocusedBorderColor = Color.Transparent,
              focusedTextColor = TextPrimary,
              unfocusedTextColor = TextPrimary
            ),
            modifier = Modifier.weight(1f)
          )

          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { searchQuery = "" }) {
              Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = TextMuted)
            }
          }

          IconButton(onClick = { showToolModal = "TIME_SOLVER" }) {
            Icon(imageVector = Icons.Default.AccessTime, contentDescription = "Time Solver", tint = CyanAccent)
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Layer Chips (Map Type + Layers)
      LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        item {
          MapFilterChip("Vector", mapType == "Vector", ElectricBlue) { mapType = "Vector" }
        }
        item {
          MapFilterChip("Satellite", mapType == "Satellite", CyanAccent) { mapType = "Satellite" }
        }
        item {
          MapFilterChip("Terrain", mapType == "Terrain", NeonGreen) { mapType = "Terrain" }
        }
        item {
          MapFilterChip("Rivers", showRivers, CyanAccent) { showRivers = !showRivers }
        }
        item {
          MapFilterChip("Minerals", showMinerals, NeonRed) { showMinerals = !showMinerals }
        }
        item {
          MapFilterChip("Relief", showRelief, NeonPurple) { showRelief = !showRelief }
        }
      }
    }

    // 3. Floating Google Maps Control Buttons (Right Side)
    Column(
      modifier = Modifier
        .align(Alignment.CenterEnd)
        .padding(end = 12.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      // Compass Needle
      Box(
        modifier = Modifier
          .size(42.dp)
          .clip(CircleShape)
          .background(Color(0xDD111827))
          .border(1.dp, ObsidianCardBorder, CircleShape)
          .clickable { offset = Offset.Zero; scale = 1.0f },
        contentAlignment = Alignment.Center
      ) {
        Icon(imageVector = Icons.Default.Navigation, contentDescription = "Recenter North", tint = NeonRed, modifier = Modifier.size(20.dp))
      }

      // Zoom In
      Box(
        modifier = Modifier
          .size(42.dp)
          .clip(CircleShape)
          .background(Color(0xDD111827))
          .border(1.dp, ObsidianCardBorder, CircleShape)
          .clickable { scale = (scale * 1.25f).coerceAtMost(5f) },
        contentAlignment = Alignment.Center
      ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Zoom In", tint = TextPrimary, modifier = Modifier.size(20.dp))
      }

      // Zoom Out
      Box(
        modifier = Modifier
          .size(42.dp)
          .clip(CircleShape)
          .background(Color(0xDD111827))
          .border(1.dp, ObsidianCardBorder, CircleShape)
          .clickable { scale = (scale / 1.25f).coerceAtLeast(0.8f) },
        contentAlignment = Alignment.Center
      ) {
        Icon(imageVector = Icons.Default.Remove, contentDescription = "Zoom Out", tint = TextPrimary, modifier = Modifier.size(20.dp))
      }

      // Recenter Location
      Box(
        modifier = Modifier
          .size(42.dp)
          .clip(CircleShape)
          .background(Color(0xDD111827))
          .border(1.dp, ObsidianCardBorder, CircleShape)
          .clickable { offset = Offset.Zero; scale = 1f },
        contentAlignment = Alignment.Center
      ) {
        Icon(imageVector = Icons.Default.LocationSearching, contentDescription = "Recenter", tint = CyanAccent, modifier = Modifier.size(20.dp))
      }
    }

    // 4. Google Maps Bottom Place Card (When State or Feature is selected)
    Box(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .fillMaxWidth()
        .padding(12.dp)
    ) {
      val state = selectedState
      val feat = selectedFeature

      if (state != null) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xF5111827))
            .border(1.dp, Color(0x333B82F6), RoundedCornerShape(16.dp))
            .padding(16.dp)
        ) {
          Column {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = state.name,
                  color = TextPrimary,
                  fontSize = 18.sp,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = "Capital: ${state.capital} • ${state.lgaCount} LGAs",
                  color = CyanAccent,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Medium
                )
              }

              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(Color(0xFF1E293B))
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Text(state.zone, color = NeonGreen, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = "Major Urban Hubs: ${state.majorHubs}",
              color = TextSecondary,
              fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // State Selector Pills
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              items(filteredStates.take(15)) { st ->
                val isSel = selectedState?.name == st.name
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isSel) ElectricBlue else Color(0xFF1E293B))
                    .clickable { selectedState = st }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  Text(st.name, color = if (isSel) Color.White else TextSecondary, fontSize = 11.sp)
                }
              }
            }
          }
        }
      }
    }
  }

  // Longitude-to-Time Calculator Modal
  if (showToolModal == "TIME_SOLVER") {
    var refLongitude by remember { mutableFloatStateOf(3.37f) }
    var targetLongitude by remember { mutableFloatStateOf(36.82f) }
    val diffDeg = targetLongitude - refLongitude
    val diffMinutes = (diffDeg * 4f).toInt()
    val diffHours = diffMinutes / 60
    val remMins = abs(diffMinutes % 60)

    ModalBottomSheet(
      onDismissRequest = { showToolModal = null },
      containerColor = ObsidianSurface
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        Text("LONGITUDE-TO-TIME DIFFERENCE SOLVER", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        Text("15° Longitude = 1 Hour (4 minutes per degree). Calculate exact local time across Africa.", color = TextSecondary, fontSize = 12.sp)

        Column {
          Text("Reference: Lagos (3.37° E WAT / GMT+1): ${"%.1f".format(refLongitude)}°", color = CyanAccent, fontSize = 12.sp)
          Slider(
            value = refLongitude,
            onValueChange = { refLongitude = it },
            valueRange = -20f..55f,
            colors = SliderDefaults.colors(thumbColor = CyanAccent, activeTrackColor = CyanAccent)
          )
        }

        Column {
          Text("Target: Nairobi (36.8° E EAT / GMT+3) or Cairo (31.2° E): ${"%.1f".format(targetLongitude)}°", color = ElectricBlue, fontSize = 12.sp)
          Slider(
            value = targetLongitude,
            onValueChange = { targetLongitude = it },
            valueRange = -20f..55f,
            colors = SliderDefaults.colors(thumbColor = ElectricBlue, activeTrackColor = ElectricBlue)
          )
        }

        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF0F172A))
            .border(1.dp, Color(0x333B82F6), RoundedCornerShape(10.dp))
            .padding(14.dp)
        ) {
          Column {
            Text("SOLVER OUTPUT:", color = TextMuted, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
            Text(
              text = "Time Difference: ${if (diffHours >= 0) "+$diffHours" else "$diffHours"}h ${remMins}m",
              color = NeonGreen,
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace
            )
          }
        }
        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}

@Composable
private fun MapFilterChip(label: String, selected: Boolean, color: Color, onClick: () -> Unit) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(16.dp))
      .background(if (selected) color else Color(0xDD111827))
      .border(1.dp, if (selected) color else ObsidianCardBorder, RoundedCornerShape(16.dp))
      .clickable { onClick() }
      .padding(horizontal = 10.dp, vertical = 6.dp)
  ) {
    Text(
      text = label,
      color = if (selected) Color.White else TextSecondary,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold
    )
  }
}
