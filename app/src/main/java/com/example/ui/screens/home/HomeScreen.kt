package com.example.ui.screens.home

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.ResearchNote
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
import com.example.util.TimeUtils

import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Create
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.ui.components.LabOriginsModal

data class LabToolItem(
  val title: String,
  val subtitle: String,
  val icon: ImageVector,
  val screenId: String,
  val accentColor: Color
)

@Composable
fun HomeScreen(
  notes: List<ResearchNote>,
  activeMistakeCount: Int,
  onOpenSubScreen: (String) -> Unit,
  onViewAllResearch: () -> Unit
) {
  var showLabOriginsModal by remember { mutableStateOf(false) }

  val labTools = listOf(
    LabToolItem("SC-991EX Calculator", "Obsidian scientific & statistics engine", Icons.Default.Calculate, "CALCULATOR", ElectricBlue),
    LabToolItem("merdd Whiteboard", "Excalidraw-style scratchpad with gallery export", Icons.Default.Create, "WHITEBOARD", CyanAccent),
    LabToolItem("Nigeria & Africa GIS", "Modern Google Maps style JAMB geography", Icons.Default.Explore, "GIS", Color(0xFF60A5FA)),
    LabToolItem("CBT & Mistake Bank", "Real past exam questions (2024–2017)", Icons.Default.Quiz, "CBT", NeonYellow),
    LabToolItem("PhET-Style Simulations", "10 physics & chemistry interactive labs", Icons.Default.Science, "SIMULATIONS", NeonGreen),
    LabToolItem("Periodic Table & SPDF", "118 elements, Bohr atom, 60s blitz", Icons.Default.GraphicEq, "PERIODIC_TABLE", NeonPurple),
    LabToolItem("Anatomy & Phonetics", "Interactive organ systems & oral English", Icons.Default.Psychology, "ANATOMY", Color(0xFFEC4899)),
    LabToolItem("merdd AI", "Research archive indexer & article links", Icons.Default.SmartToy, "LAB_AI", ElectricBlue),
    LabToolItem("Lab Origins Story", "David & Mercy's shared brand narrative", Icons.Default.AutoAwesome, "ORIGINS", NeonPurple)
  )

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(ObsidianBackground)
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Hero Banner
    item {
      Spacer(modifier = Modifier.height(4.dp))
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .background(
            Brush.linearGradient(
              colors = listOf(Color(0xFF1E3A8A), Color(0xFF0F172A), Color(0xFF0A0E17))
            )
          )
          .border(1.dp, Color(0x333B82F6), RoundedCornerShape(16.dp))
          .padding(20.dp)
      ) {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0x333B82F6))
                .clickable { showLabOriginsModal = true }
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = "⚛ LAB ORIGINS • DAVID & MERCY",
                color = ElectricBlue,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
              )
            }
            Text(
              text = "WAT: ${TimeUtils.getLagosFormattedTime().substringBefore(" WAT")}",
              color = TextMuted,
              fontSize = 11.sp,
              fontFamily = FontFamily.Monospace
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = "Exploring the Universe, One Experiment at a Time.",
            color = TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            lineHeight = 26.sp
          )

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "David & Mercy's collaborative research repository, SC-991EX computation suite, and JAMB UTME mastery ecosystem.",
            color = TextSecondary,
            fontSize = 13.sp,
            lineHeight = 18.sp
          )
        }
      }
    }

    // Quick Status Grid Cards
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // Status Card 1
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(ObsidianSurface)
            .border(1.dp, ObsidianCardBorder, RoundedCornerShape(12.dp))
            .padding(14.dp)
        ) {
          Column {
            Text("10 Simulations", color = CyanAccent, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text("Live Canvas Labs", color = TextSecondary, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text("RK4, Titration, Gas", color = TextMuted, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
          }
        }

        // Status Card 2
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(ObsidianSurface)
            .border(1.dp, ObsidianCardBorder, RoundedCornerShape(12.dp))
            .padding(14.dp)
        ) {
          Column {
            Text("21+ Modules", color = ElectricBlue, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text("Engines Active", color = TextSecondary, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text("SC-991EX & GIS Ready", color = TextMuted, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
          }
        }

        // Status Card 3 (Mistake Bank)
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(ObsidianSurface)
            .border(1.dp, if (activeMistakeCount > 0) NeonYellow.copy(alpha = 0.4f) else ObsidianCardBorder, RoundedCornerShape(12.dp))
            .clickable { onOpenSubScreen("CBT") }
            .padding(14.dp)
        ) {
          Column {
            Text("$activeMistakeCount Mistakes", color = if (activeMistakeCount > 0) NeonYellow else NeonGreen, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text("In Mistake Bank", color = TextSecondary, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text("2-Streak Clear Rule", color = TextMuted, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
          }
        }
      }
    }

    // Core Lab Modules Launchpad
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "LAB MODULES & ENGINES",
          color = TextPrimary,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
      }
    }

    items(labTools) { tool ->
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(ObsidianSurface)
          .border(1.dp, ObsidianCardBorder, RoundedCornerShape(14.dp))
          .clickable { onOpenSubScreen(tool.screenId) }
          .padding(16.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(tool.accentColor.copy(alpha = 0.15f))
                .border(1.dp, tool.accentColor.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = tool.icon,
                contentDescription = tool.title,
                tint = tool.accentColor,
                modifier = Modifier.size(24.dp)
              )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
              Text(
                text = tool.title,
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = tool.subtitle,
                color = TextSecondary,
                fontSize = 12.sp
              )
            }
          }

          Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Open",
            tint = TextMuted
          )
        }
      }
    }

    // Recent Findings Preview
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "RECENT LAB FINDINGS",
          color = TextPrimary,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
        Text(
          text = "View All (${notes.size})",
          color = ElectricBlue,
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold,
          modifier = Modifier.clickable { onViewAllResearch() }
        )
      }
    }

    items(notes.take(3)) { note ->
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(ObsidianSurface)
          .border(1.dp, ObsidianCardBorder, RoundedCornerShape(14.dp))
          .clickable { onViewAllResearch() }
          .padding(16.dp)
      ) {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(24.dp)
                  .clip(CircleShape)
                  .background(if (note.author == "David") ElectricBlue else NeonPurple),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = note.author.take(1),
                  color = Color.White,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold
                )
              }
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = note.author,
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "• ${TimeUtils.formatRelativeTime(note.timestamp)}",
                color = TextMuted,
                fontSize = 11.sp
              )
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF1E293B))
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = "#${note.category}",
                color = CyanAccent,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = note.title,
            color = TextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
          )

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = note.content,
            color = TextSecondary,
            fontSize = 13.sp,
            maxLines = 2,
            lineHeight = 18.sp
          )
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }

  if (showLabOriginsModal) {
    LabOriginsModal(onDismiss = { showLabOriginsModal = false })
  }
}
