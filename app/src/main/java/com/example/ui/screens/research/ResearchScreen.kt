package com.example.ui.screens.research

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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ResearchNote
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonRed
import com.example.ui.theme.ObsidianBackground
import com.example.ui.theme.ObsidianCardBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.SearchHighlightBg
import com.example.ui.theme.SearchHighlightText
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.TimeUtils

@Composable
fun ResearchScreen(
  notes: List<ResearchNote>,
  searchQuery: String,
  selectedCategory: String,
  onSearchChange: (String) -> Unit,
  onCategorySelect: (String) -> Unit,
  onLikeNote: (Long) -> Unit
) {
  val categories = listOf("All", "Physics", "Chemistry", "Biology", "ChaosTheory", "JAMB", "Tech")

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(ObsidianBackground)
  ) {
    // Search Bar
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchChange,
        placeholder = { Text("Live search titles, notes, tags...", color = TextMuted, fontSize = 14.sp) },
        leadingIcon = {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = if (searchQuery.isNotBlank()) ElectricBlue else TextMuted
          )
        },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { onSearchChange("") }) {
              Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Clear search",
                tint = TextMuted
              )
            }
          }
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
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

    // Category Tags Filter
    LazyRow(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 4.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(categories) { cat ->
        val isSelected = selectedCategory.equals(cat, ignoreCase = true)
        val label = if (cat == "All") "All Archives" else "#$cat"

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) ElectricBlue else ObsidianSurface)
            .border(1.dp, if (isSelected) ElectricBlue else ObsidianCardBorder, RoundedCornerShape(8.dp))
            .clickable { onCategorySelect(cat) }
            .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Text(
            text = label,
            color = if (isSelected) Color.White else TextSecondary,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
          )
        }
      }
    }

    // Active Results Count Banner
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = if (searchQuery.isBlank()) "FEED (${notes.size} NOTES)" else "MATCHES: ${notes.size} FOUND",
        color = if (searchQuery.isNotBlank()) CyanAccent else TextMuted,
        fontSize = 11.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold
      )

      if (searchQuery.isNotBlank()) {
        Text(
          text = "Glowing highlights active",
          color = ElectricBlue,
          fontSize = 11.sp,
          fontFamily = FontFamily.Monospace
        )
      }
    }

    // Feed List
    if (notes.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = "No matching research notes found",
            color = TextSecondary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Try adjusting your search query or category filter.",
            color = TextMuted,
            fontSize = 13.sp
          )
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        items(notes, key = { it.id }) { note ->
          ResearchCard(
            note = note,
            query = searchQuery,
            onLike = { onLikeNote(note.id) }
          )
        }
        item {
          Spacer(modifier = Modifier.height(24.dp))
        }
      }
    }
  }
}

@Composable
private fun ResearchCard(
  note: ResearchNote,
  query: String,
  onLike: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .background(ObsidianSurface)
      .border(1.dp, ObsidianCardBorder, RoundedCornerShape(14.dp))
      .padding(16.dp)
  ) {
    Column {
      // Author header + relative time + category
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(28.dp)
              .clip(CircleShape)
              .background(if (note.author == "David") ElectricBlue else NeonPurple),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = note.author.take(1),
              color = Color.White,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column {
            Text(
              text = highlightQuery(note.author, query),
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = TimeUtils.formatRelativeTime(note.timestamp),
              color = TextMuted,
              fontSize = 10.sp
            )
          }
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF1E293B))
            .border(1.dp, Color(0x223B82F6), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = highlightQuery("#${note.category}", query),
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Note Title with Live Search Highlighting
      Text(
        text = highlightQuery(note.title, query),
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 22.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Note Body with Live Search Highlighting
      Text(
        text = highlightQuery(note.content, query),
        fontSize = 13.sp,
        lineHeight = 19.sp,
        color = TextSecondary
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Tags & Action Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = highlightQuery(note.tags, query),
          fontSize = 11.sp,
          fontFamily = FontFamily.Monospace,
          color = CyanAccent,
          modifier = Modifier.weight(1f)
        )

        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onLike() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Icon(
            imageVector = if (note.likes > 0) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = "Like",
            tint = if (note.likes > 0) NeonRed else TextMuted,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "${note.likes}",
            color = if (note.likes > 0) NeonRed else TextMuted,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}

/**
 * Live Search Highlighting Engine:
 * Dynamically wraps matching search terms with a glowing electric blue highlight background in real time!
 */
private fun highlightQuery(text: String, query: String): AnnotatedString {
  if (query.isBlank()) return AnnotatedString(text)

  return buildAnnotatedString {
    var startIndex = 0
    val lowerText = text.lowercase()
    val lowerQuery = query.lowercase().trim()

    if (lowerQuery.isEmpty()) {
      append(text)
      return@buildAnnotatedString
    }

    while (startIndex < text.length) {
      val foundIndex = lowerText.indexOf(lowerQuery, startIndex)
      if (foundIndex == -1) {
        append(text.substring(startIndex))
        break
      }

      // Append text before match
      append(text.substring(startIndex, foundIndex))

      // Append matching text with glowing electric blue highlight background & bright cyan-blue text
      pushStyle(
        SpanStyle(
          background = SearchHighlightBg,
          color = SearchHighlightText,
          fontWeight = FontWeight.ExtraBold
        )
      )
      append(text.substring(foundIndex, foundIndex + lowerQuery.length))
      pop()

      startIndex = foundIndex + lowerQuery.length
    }
  }
}
