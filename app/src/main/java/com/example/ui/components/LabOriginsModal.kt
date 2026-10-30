package com.example.ui.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.ObsidianCardBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabOriginsModal(
  onDismiss: () -> Unit
) {
  val context = LocalContext.current

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    containerColor = ObsidianSurface,
    dragHandle = { BottomSheetDefaults.DragHandle(color = TextMuted) }
  ) {
    LazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 10.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Header
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(54.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(Color.Black)
              .border(1.5.dp, ElectricBlue, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
          ) {
            Image(
              painter = painterResource(id = R.drawable.ic_merdd_md),
              contentDescription = "Merdd Brand Emblem",
              modifier = Modifier.size(44.dp)
            )
          }

          Spacer(modifier = Modifier.width(14.dp))

          Column {
            Text(
              text = "LAB ORIGINS: THE MERDD STORY",
              color = TextPrimary,
              fontSize = 17.sp,
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 1.sp
            )
            Text(
              text = "David & Mercy's Collaborative STEM Ecosystem",
              color = CyanAccent,
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }
      }

      // Brand Narrative
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF0F172A))
            .border(1.dp, Color(0x333B82F6), RoundedCornerShape(12.dp))
            .padding(16.dp)
        ) {
          Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
              text = "THE SHARED PASSION",
              color = ElectricBlue,
              fontSize = 11.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "merdd lab was born from a shared passion between two science enthusiasts, David and Mercy. Driven by a mutual obsession with uncovering how the universe functions from first principles, they united David's mathematical and computational rigor with Mercy's deep biochemical and experimental insight.\n\nmerdd is their personal proving ground—built to elevate STEM discovery and empower fellow UTME/JAMB scholars with precision tools, authentic data, and simulation engines.",
              color = TextSecondary,
              fontSize = 13.sp,
              lineHeight = 20.sp
            )
          }
        }
      }

      // Profile Cards: David & Mercy
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          // David
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(12.dp))
              .background(Color(0xFF1E293B))
              .border(1.dp, ElectricBlue.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
              .padding(14.dp)
          ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(ElectricBlue),
                  contentAlignment = Alignment.Center
                ) {
                  Text("D", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                  Text("David", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                  Text("Physics & Code", color = CyanAccent, fontSize = 10.sp)
                }
              }

              Text(
                text = "• Physics Mechanics\n• Numerical Modeling & RK4\n• SC-991EX Logic\n• Problem Solving",
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 17.sp
              )
            }
          }

          // Mercy
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(12.dp))
              .background(Color(0xFF1E293B))
              .border(1.dp, NeonPurple.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
              .padding(14.dp)
          ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(NeonPurple),
                  contentAlignment = Alignment.Center
                ) {
                  Text("M", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                  Text("Mercy", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                  Text("Chemistry & Bio", color = NeonPurple, fontSize = 10.sp)
                }
              }

              Text(
                text = "• Chemistry Titrations\n• Stoichiometry & Redox\n• Cellular Genetics\n• Empirical Research",
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 17.sp
              )
            }
          }
        }
      }

      // Share Merdd Lab with Mercy / Friends
      item {
        Button(
          onClick = { shareMerddApp(context) },
          colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
          Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Share merdd Lab with Mercy & Friends", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
      }

      item {
        Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }
}

private fun shareMerddApp(context: Context) {
  val intent = Intent(Intent.ACTION_SEND).apply {
    type = "text/plain"
    putExtra(
      Intent.EXTRA_SUBJECT,
      "merdd Lab: David & Mercy's STEM & JAMB Hub"
    )
    putExtra(
      Intent.EXTRA_TEXT,
      "Explore merdd Lab - David & Mercy's joint STEM & JAMB Hub with SC-991EX calculator, 10 PhET-style simulations, GIS map lab, mistake bank, and collaborative research notes!"
    )
  }
  context.startActivity(Intent.createChooser(intent, "Share merdd Lab"))
}
