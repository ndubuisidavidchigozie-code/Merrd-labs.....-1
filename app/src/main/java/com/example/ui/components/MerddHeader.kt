package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.sync.SyncStatus
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonYellow
import com.example.ui.theme.ObsidianCardBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.TimeUtils

@Composable
fun MerddHeader(
  syncStatus: SyncStatus,
  onCycleSync: () -> Unit,
  onLock: () -> Unit
) {
  val greeting = TimeUtils.getLagosGreeting()

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .background(ObsidianSurface)
      .border(1.dp, ObsidianCardBorder)
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Left: Interlocking MD logo + David & Mercy's Lab badge
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
              Brush.linearGradient(
                colors = listOf(Color(0xFF1E3A8A), Color(0xFF0F172A))
              )
            )
            .border(1.5.dp, ElectricBlue, RoundedCornerShape(10.dp)),
          contentAlignment = Alignment.Center
        ) {
          Image(
            painter = painterResource(id = R.drawable.ic_merdd_md),
            contentDescription = "Merdd Interlocking Logo",
            modifier = Modifier.size(36.dp)
          )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "MERDD",
              color = TextPrimary,
              fontSize = 17.sp,
              fontWeight = FontWeight.Black,
              letterSpacing = 1.2.sp
            )
            Spacer(modifier = Modifier.width(6.dp))
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF1E293B))
                .border(1.dp, Color(0x333B82F6), RoundedCornerShape(6.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = "⚛ David & Mercy's Lab",
                color = CyanAccent,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
              )
            }
          }

          Text(
            text = greeting,
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }

      // Right: Live Sync Badge & Lock Button
      Row(verticalAlignment = Alignment.CenterVertically) {
        val badgeColor = when (syncStatus) {
          SyncStatus.SYNCED_LIVE -> NeonGreen
          SyncStatus.RECONNECTING -> NeonYellow
          SyncStatus.OFFLINE_LOCAL -> Color(0xFF94A3B8)
        }

        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF0F172A))
            .border(1.dp, badgeColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .clickable { onCycleSync() }
            .padding(horizontal = 8.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(7.dp)
              .clip(CircleShape)
              .background(badgeColor)
          )
          Spacer(modifier = Modifier.width(5.dp))
          Text(
            text = syncStatus.label,
            color = badgeColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
          )
        }

        Spacer(modifier = Modifier.width(6.dp))

        IconButton(
          onClick = onLock,
          modifier = Modifier.size(32.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Lock Lab",
            tint = TextMuted,
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }
  }
}
