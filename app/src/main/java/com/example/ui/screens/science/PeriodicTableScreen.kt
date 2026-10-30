package com.example.ui.screens.science

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import kotlinx.coroutines.delay

data class ElementInfo(
  val z: Int,
  val symbol: String,
  val name: String,
  val mass: Double,
  val category: String, // "Nonmetal", "Noble Gas", "Alkali Metal", "Transition Metal", etc.
  val spdf: String,
  val shells: String // e.g. "2, 8, 14, 2"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodicTableScreen(onBack: () -> Unit) {
  val elements = remember {
    listOf(
      ElementInfo(1, "H", "Hydrogen", 1.008, "Reactive Nonmetal", "1s¹", "1"),
      ElementInfo(2, "He", "Helium", 4.0026, "Noble Gas", "1s²", "2"),
      ElementInfo(3, "Li", "Lithium", 6.94, "Alkali Metal", "[He] 2s¹", "2, 1"),
      ElementInfo(4, "Be", "Beryllium", 9.0122, "Alkaline Earth", "[He] 2s²", "2, 2"),
      ElementInfo(5, "B", "Boron", 10.81, "Metalloid", "[He] 2s² 2p¹", "2, 3"),
      ElementInfo(6, "C", "Carbon", 12.011, "Reactive Nonmetal", "[He] 2s² 2p²", "2, 4"),
      ElementInfo(7, "N", "Nitrogen", 14.007, "Reactive Nonmetal", "[He] 2s² 2p³", "2, 5"),
      ElementInfo(8, "O", "Oxygen", 15.999, "Reactive Nonmetal", "[He] 2s² 2p⁴", "2, 6"),
      ElementInfo(9, "F", "Fluorine", 18.998, "Halogen", "[He] 2s² 2p⁵", "2, 7"),
      ElementInfo(10, "Ne", "Neon", 20.180, "Noble Gas", "[He] 2s² 2p⁶", "2, 8"),
      ElementInfo(11, "Na", "Sodium", 22.990, "Alkali Metal", "[Ne] 3s¹", "2, 8, 1"),
      ElementInfo(12, "Mg", "Magnesium", 24.305, "Alkaline Earth", "[Ne] 3s²", "2, 8, 2"),
      ElementInfo(13, "Al", "Aluminium", 26.982, "Post-Transition Metal", "[Ne] 3s² 3p¹", "2, 8, 3"),
      ElementInfo(14, "Si", "Silicon", 28.085, "Metalloid", "[Ne] 3s² 3p²", "2, 8, 4"),
      ElementInfo(15, "P", "Phosphorus", 30.974, "Reactive Nonmetal", "[Ne] 3s² 3p³", "2, 8, 5"),
      ElementInfo(16, "S", "Sulfur", 32.06, "Reactive Nonmetal", "[Ne] 3s² 3p⁴", "2, 8, 6"),
      ElementInfo(17, "Cl", "Chlorine", 35.45, "Halogen", "[Ne] 3s² 3p⁵", "2, 8, 7"),
      ElementInfo(18, "Ar", "Argon", 39.948, "Noble Gas", "[Ne] 3s² 3p⁶", "2, 8, 8"),
      ElementInfo(19, "K", "Potassium", 39.098, "Alkali Metal", "[Ar] 4s¹", "2, 8, 8, 1"),
      ElementInfo(20, "Ca", "Calcium", 40.078, "Alkaline Earth", "[Ar] 4s²", "2, 8, 8, 2"),
      ElementInfo(26, "Fe", "Iron", 55.845, "Transition Metal", "[Ar] 3d⁶ 4s²", "2, 8, 14, 2"),
      ElementInfo(29, "Cu", "Copper", 63.546, "Transition Metal", "[Ar] 3d¹⁰ 4s¹", "2, 8, 18, 1"),
      ElementInfo(30, "Zn", "Zinc", 65.38, "Transition Metal", "[Ar] 3d¹⁰ 4s²", "2, 8, 18, 2"),
      ElementInfo(47, "Ag", "Silver", 107.87, "Transition Metal", "[Kr] 4d¹⁰ 5s¹", "2, 8, 18, 18, 1"),
      ElementInfo(79, "Au", "Gold", 196.97, "Transition Metal", "[Xe] 4f¹⁴ 5d¹⁰ 6s¹", "2, 8, 18, 32, 18, 1"),
      ElementInfo(82, "Pb", "Lead", 207.2, "Post-Transition Metal", "[Xe] 4f¹⁴ 5d¹⁰ 6s² 6p²", "2, 8, 18, 32, 18, 4"),
      ElementInfo(92, "U", "Uranium", 238.03, "Actinide", "[Rn] 5f³ 6d¹ 7s²", "2, 8, 18, 32, 21, 9, 2")
    )
  }

  var searchFilter by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf("All") }
  var inspectElement by remember { mutableStateOf<ElementInfo?>(null) }
  var isSpeedQuizMode by remember { mutableStateOf(false) }

  // 60s Speed Quiz Blitz state
  var quizTimer by remember { mutableIntStateOf(60) }
  var quizScore by remember { mutableIntStateOf(0) }
  var currentQuizQuestion by remember { mutableStateOf(elements.random()) }

  LaunchedEffect(isSpeedQuizMode) {
    if (isSpeedQuizMode) {
      quizTimer = 60
      quizScore = 0
      while (quizTimer > 0 && isSpeedQuizMode) {
        delay(1000)
        quizTimer--
      }
    }
  }

  val filteredElements = elements.filter { el ->
    val matchesCat = selectedCategory == "All" || el.category.contains(selectedCategory, ignoreCase = true)
    val matchesSearch = searchFilter.isBlank() || el.name.contains(searchFilter, ignoreCase = true) || el.symbol.contains(searchFilter, ignoreCase = true) || el.z.toString() == searchFilter
    matchesCat && matchesSearch
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
          text = "PERIODIC TABLE & SPDF ORBITALS",
          color = TextPrimary,
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
      }

      Button(
        onClick = { isSpeedQuizMode = !isSpeedQuizMode },
        colors = ButtonDefaults.buttonColors(containerColor = if (isSpeedQuizMode) NeonRed else ElectricBlue),
        shape = RoundedCornerShape(8.dp)
      ) {
        Icon(imageVector = Icons.Default.Timer, contentDescription = null, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(if (isSpeedQuizMode) "Exit Blitz" else "60s Blitz", fontSize = 11.sp, fontWeight = FontWeight.Bold)
      }
    }

    if (isSpeedQuizMode) {
      // 60s Speed Quiz Blitz Mode
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        Text("60-SECOND PERIODIC BLITZ QUIZ", color = NeonYellow, fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
        Spacer(modifier = Modifier.height(10.dp))
        Text("Time Remaining: ${quizTimer}s | Score: $quizScore", color = CyanAccent, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, fontFamily = FontFamily.Monospace)
        Spacer(modifier = Modifier.height(24.dp))

        Box(
          modifier = Modifier
            .size(120.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(ObsidianSurface)
            .border(2.dp, ElectricBlue, RoundedCornerShape(16.dp)),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("${currentQuizQuestion.z}", color = TextMuted, fontSize = 12.sp)
            Text(currentQuizQuestion.symbol, color = TextPrimary, fontSize = 36.sp, fontWeight = FontWeight.Black)
            Text(currentQuizQuestion.name, color = CyanAccent, fontSize = 11.sp)
          }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text("What is the correct SPDF electronic configuration?", color = TextSecondary, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(14.dp))

        val options = listOf(
          currentQuizQuestion.spdf,
          "[Ar] 4s² 3d⁸",
          "[Ne] 3s² 3p⁴",
          "1s² 2s² 2p⁶"
        ).shuffled()

        options.forEach { opt ->
          Button(
            onClick = {
              if (opt == currentQuizQuestion.spdf) {
                quizScore++
              }
              currentQuizQuestion = elements.random()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
            modifier = Modifier
              .fillMaxWidth(0.9f)
              .padding(vertical = 4.dp),
            shape = RoundedCornerShape(8.dp)
          ) {
            Text(opt, color = TextPrimary, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
          }
        }
      }
    } else {
      // Search Bar & Filter Row
      Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
        OutlinedTextField(
          value = searchFilter,
          onValueChange = { searchFilter = it },
          placeholder = { Text("Filter element symbol, name, or atomic number...", color = TextMuted, fontSize = 12.sp) },
          leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = TextMuted) },
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
      }

      // Elements Grid
      LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(filteredElements) { el ->
          val color = when {
            el.category.contains("Noble") -> CyanAccent
            el.category.contains("Transition") -> ElectricBlue
            el.category.contains("Alkali") -> NeonYellow
            el.category.contains("Nonmetal") -> NeonGreen
            else -> NeonPurple
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .background(ObsidianSurface)
              .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
              .clickable { inspectElement = el }
              .padding(10.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text("${el.z}", color = TextMuted, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
              Text(el.symbol, color = color, fontSize = 20.sp, fontWeight = FontWeight.Bold)
              Text(el.name, color = TextPrimary, fontSize = 10.sp, maxLines = 1)
              Text(el.spdf.take(9), color = TextMuted, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
            }
          }
        }
      }
    }
  }

  // Element Inspector Modal
  if (inspectElement != null) {
    val el = inspectElement!!
    ModalBottomSheet(
      onDismissRequest = { inspectElement = null },
      containerColor = ObsidianSurface,
      dragHandle = { BottomSheetDefaults.DragHandle(color = TextMuted) }
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text("${el.name} (${el.symbol})", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Atomic Number: Z = ${el.z} • Relative Atomic Mass: ${el.mass}", color = TextSecondary, fontSize = 12.sp)
          }
          Box(
            modifier = Modifier
              .size(50.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xFF1E293B)),
            contentAlignment = Alignment.Center
          ) {
            Text(el.symbol, color = ElectricBlue, fontSize = 24.sp, fontWeight = FontWeight.Black)
          }
        }

        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF0F172A))
            .border(1.dp, Color(0x333B82F6), RoundedCornerShape(10.dp))
            .padding(14.dp)
        ) {
          Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("SPDF ELECTRON CONFIGURATION:", color = CyanAccent, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
            Text(el.spdf, color = TextPrimary, fontSize = 16.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Bohr Shell Populations (K, L, M, N...):", color = TextMuted, fontSize = 11.sp)
            Text(el.shells, color = NeonGreen, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Classification: ${el.category}", color = TextSecondary, fontSize = 12.sp)
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}
