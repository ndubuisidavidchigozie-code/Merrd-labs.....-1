package com.example.ui.screens.publish

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.ui.theme.ObsidianBackground
import com.example.ui.theme.ObsidianCardBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun PublishScreen(
  onPublish: (title: String, content: String, author: String, category: String, tags: String) -> Unit
) {
  var author by remember { mutableStateOf("David") }
  var title by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf("Physics") }
  var tags by remember { mutableStateOf("#Physics #Experiment") }
  var content by remember { mutableStateOf("") }
  var showSuccessToast by remember { mutableStateOf(false) }

  val categories = listOf("Physics", "Chemistry", "Biology", "ChaosTheory", "JAMB", "Tech")
  val formulaSnippets = listOf(
    "ΔH = -q/n",
    "PV = nRT",
    "E = hf",
    "v = fλ",
    "∫ f(x)dx",
    "pH = -log[H+]",
    "k = A·e^(-Ea/RT)",
    "F = ma"
  )

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(ObsidianBackground)
      .verticalScroll(rememberScrollState())
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header Banner
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(ObsidianSurface)
        .border(1.dp, ObsidianCardBorder, RoundedCornerShape(12.dp))
        .padding(16.dp)
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0x333B82F6)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Publish,
            contentDescription = null,
            tint = ElectricBlue
          )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(
            text = "PUBLISH TO LAB ARCHIVE",
            color = TextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
          Text(
            text = "Sync joint findings, derivations & UTME prep notes",
            color = TextSecondary,
            fontSize = 12.sp
          )
        }
      }
    }

    // Author Selector: David vs Mercy
    Column {
      Text(
        text = "RESEARCH OPERATOR",
        color = TextMuted,
        fontSize = 11.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(8.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        listOf("David", "Mercy").forEach { name ->
          val isSelected = author == name
          val color = if (name == "David") ElectricBlue else NeonPurple

          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(10.dp))
              .background(if (isSelected) color.copy(alpha = 0.2f) else ObsidianSurface)
              .border(
                1.5.dp,
                if (isSelected) color else ObsidianCardBorder,
                RoundedCornerShape(10.dp)
              )
              .clickable { author = name }
              .padding(vertical = 12.dp, horizontal = 16.dp),
            contentAlignment = Alignment.Center
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(24.dp)
                  .clip(CircleShape)
                  .background(color),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = name.take(1),
                  color = Color.White,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold
                )
              }
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = name,
                color = if (isSelected) Color.White else TextSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }

    // Research Title Input
    Column {
      Text(
        text = "RESEARCH TITLE",
        color = TextMuted,
        fontSize = 11.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(6.dp))
      OutlinedTextField(
        value = title,
        onValueChange = { title = it },
        placeholder = { Text("e.g. Runge-Kutta 4th Order in Chaotic Orbits", color = TextMuted) },
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

    // Category Selector
    Column {
      Text(
        text = "DOMAIN CATEGORY",
        color = TextMuted,
        fontSize = 11.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(6.dp))
      LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(categories) { cat ->
          val isSelected = selectedCategory == cat
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(if (isSelected) ElectricBlue else ObsidianSurface)
              .border(1.dp, if (isSelected) ElectricBlue else ObsidianCardBorder, RoundedCornerShape(8.dp))
              .clickable {
                selectedCategory = cat
                if (!tags.contains("#$cat")) {
                  tags = "#$cat $tags".trim()
                }
              }
              .padding(horizontal = 12.dp, vertical = 6.dp)
          ) {
            Text(
              text = "#$cat",
              color = if (isSelected) Color.White else TextSecondary,
              fontSize = 12.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
          }
        }
      }
    }

    // Quick Formula Insertion Chips
    Column {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Functions,
          contentDescription = null,
          tint = CyanAccent,
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "INSERT STEM FORMULA SNIPPET",
          color = CyanAccent,
          fontSize = 11.sp,
          fontFamily = FontFamily.Monospace,
          fontWeight = FontWeight.Bold
        )
      }
      Spacer(modifier = Modifier.height(6.dp))
      LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(formulaSnippets) { snippet ->
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(Color(0xFF1E293B))
              .border(1.dp, Color(0x333B82F6), RoundedCornerShape(6.dp))
              .clickable {
                content = if (content.isBlank()) snippet else "$content\n$snippet"
              }
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = snippet,
              color = CyanAccent,
              fontSize = 11.sp,
              fontFamily = FontFamily.Monospace
            )
          }
        }
      }
    }

    // Content Field
    Column {
      Text(
        text = "FINDINGS / NOTES CONTENT",
        color = TextMuted,
        fontSize = 11.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(6.dp))
      OutlinedTextField(
        value = content,
        onValueChange = { content = it },
        placeholder = { Text("Enter detailed scientific observations, equations, or exam notes...", color = TextMuted) },
        minLines = 5,
        maxLines = 10,
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

    // Tags Input
    Column {
      Text(
        text = "HASHTAGS & KEYWORDS",
        color = TextMuted,
        fontSize = 11.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(6.dp))
      OutlinedTextField(
        value = tags,
        onValueChange = { tags = it },
        placeholder = { Text("#Physics #JAMB #Titration", color = TextMuted) },
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

    // 1-Click Publish Action Button
    Button(
      onClick = {
        if (title.isNotBlank() && content.isNotBlank()) {
          onPublish(title, content, author, selectedCategory, tags)
        }
      },
      enabled = title.isNotBlank() && content.isNotBlank(),
      shape = RoundedCornerShape(12.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = ElectricBlue,
        disabledContainerColor = Color(0xFF1E293B)
      ),
      modifier = Modifier
        .fillMaxWidth()
        .height(52.dp)
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Publish,
          contentDescription = null,
          tint = Color.White
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "PUBLISH TO LAB ARCHIVE",
          color = Color.White,
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(24.dp))
  }
}
