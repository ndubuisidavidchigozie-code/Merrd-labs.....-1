package com.example.ui.screens.ai

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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.ui.theme.NeonYellow
import com.example.ui.theme.ObsidianBackground
import com.example.ui.theme.ObsidianCardBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

data class AiMessage(
  val sender: String, // "USER" or "AI"
  val text: String,
  val matchedNote: ResearchNote? = null,
  val timestamp: Long = System.currentTimeMillis()
)

@Composable
fun FoundersAndAiScreen(
  researchNotes: List<ResearchNote>,
  onNavigateToNote: (ResearchNote) -> Unit,
  onNavigateToPublish: () -> Unit,
  onBack: () -> Unit
) {
  var userMessage by remember { mutableStateOf("") }
  val messages = remember {
    mutableStateListOf(
      AiMessage(
        sender = "AI",
        text = "Welcome to merdd AI Archive Navigator. Protocol notice: I do NOT answer general science questions. Instead, ask me if any research on a particular topic has been conducted on merdd, and I will search David & Mercy's archives and provide direct links to the published articles."
      )
    )
  }
  val listState = rememberLazyListState()
  val scope = rememberCoroutineScope()

  val sampleQueries = listOf(
    "Has any research on RK4 double pendulum been done?",
    "Is there research on Titration stoichiometry?",
    "Check if equipotential fields have been published",
    "Has cellular respiration ATP yield been researched?"
  )

  fun handleSend(query: String) {
    if (query.isBlank()) return
    messages.add(AiMessage("USER", query))
    userMessage = ""

    // Search existing published research in Merdd Lab
    val cleanQuery = query.lowercase()
      .replace("has any research on", "")
      .replace("is there research on", "")
      .replace("check if", "")
      .replace("been done on merdd", "")
      .replace("been done", "")
      .replace("been published", "")
      .replace("?", "")
      .trim()

    val tokens = cleanQuery.split(" ").filter { it.length > 2 }

    val matched = researchNotes.find { note ->
      tokens.any { token ->
        note.title.contains(token, ignoreCase = true) ||
          note.content.contains(token, ignoreCase = true) ||
          note.tags.contains(token, ignoreCase = true) ||
          note.category.contains(token, ignoreCase = true)
      }
    }

    if (matched != null) {
      val reply = "Yes! Research on this topic has been published in merdd lab:\n\n" +
        "📄 \"${matched.title}\"\n" +
        "👤 Author: ${matched.author} • Category: #${matched.category}\n\n" +
        "Excerpt: ${matched.content.take(150)}...\n\n" +
        "You can view the full published article directly in the Research space."
      messages.add(AiMessage("AI", reply, matchedNote = matched))
    } else {
      val reply = "No research on \"$cleanQuery\" has been published in merdd lab archives yet.\n\n" +
        "Protocol reminder: merdd AI does not generate or answer external science questions. I strictly search and index David and Mercy's published research space. Would you like to draft and publish a new note on this topic?"
      messages.add(AiMessage("AI", reply, matchedNote = null))
    }

    scope.launch {
      listState.animateScrollToItem(messages.size - 1)
    }
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
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = onBack) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
      }
      Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
          painter = painterResource(id = R.drawable.ic_merdd_ai),
          contentDescription = "merdd AI",
          modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
          Text(
            text = "merdd AI",
            color = TextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
          Text(
            text = "Research Archive Navigator",
            color = CyanAccent,
            fontSize = 10.sp
          )
        }
      }
    }

    // Messages Thread
    LazyColumn(
      state = listState,
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Header Notice
      item {
        Spacer(modifier = Modifier.height(6.dp))
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF0F172A))
            .border(1.dp, Color(0x333B82F6), RoundedCornerShape(12.dp))
            .padding(14.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
              painter = painterResource(id = R.drawable.ic_merdd_ai),
              contentDescription = "merdd AI",
              modifier = Modifier.size(46.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "merdd ARCHIVE INDEXER",
                color = ElectricBlue,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Query if research has been published on merdd. AI strictly links to articles in the Published space.",
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp
              )
            }
          }
        }
      }

      items(messages) { msg ->
        val isUser = msg.sender == "USER"
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth(0.9f)
              .clip(RoundedCornerShape(12.dp))
              .background(if (isUser) ElectricBlue else ObsidianSurface)
              .border(1.dp, if (isUser) ElectricBlue else ObsidianCardBorder, RoundedCornerShape(12.dp))
              .padding(14.dp)
          ) {
            Column {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = if (isUser) "Researcher" else "merdd AI",
                  color = if (isUser) Color.White else CyanAccent,
                  fontSize = 11.sp,
                  fontFamily = FontFamily.Monospace,
                  fontWeight = FontWeight.Bold
                )
              }

              Spacer(modifier = Modifier.height(6.dp))

              Text(
                text = msg.text,
                color = if (isUser) Color.White else TextPrimary,
                fontSize = 13.sp,
                lineHeight = 19.sp
              )

              // 1-Click Link Button to Published Article
              if (msg.matchedNote != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                  onClick = { onNavigateToNote(msg.matchedNote) },
                  colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                  shape = RoundedCornerShape(8.dp),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Icon(imageVector = Icons.Default.OpenInNew, contentDescription = null, tint = Color.White, modifier = Modifier.size(15.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("Open in Published Space", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
              } else if (!isUser && msg.text.contains("draft and publish")) {
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                  onClick = onNavigateToPublish,
                  colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                  shape = RoundedCornerShape(8.dp),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Icon(imageVector = Icons.Default.Publish, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(15.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("Draft in Publish Space", color = CyanAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }
      }

      // Sample Queries
      item {
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "SUGGESTED ARCHIVE SEARCHES:",
          color = TextMuted,
          fontSize = 10.sp,
          fontFamily = FontFamily.Monospace
        )
        Spacer(modifier = Modifier.height(4.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          items(sampleQueries) { q ->
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF1E293B))
                .border(1.dp, Color(0x333B82F6), RoundedCornerShape(8.dp))
                .clickable { handleSend(q) }
                .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Text(text = q, color = CyanAccent, fontSize = 11.sp)
            }
          }
        }
      }
    }

    // Input Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(ObsidianSurface)
        .padding(horizontal = 12.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      OutlinedTextField(
        value = userMessage,
        onValueChange = { userMessage = it },
        placeholder = { Text("Query if research on topic exists on merdd...", color = TextMuted, fontSize = 12.sp) },
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = Color(0xFF0F172A),
          unfocusedContainerColor = Color(0xFF0F172A),
          focusedBorderColor = ElectricBlue,
          unfocusedBorderColor = ObsidianCardBorder,
          focusedTextColor = TextPrimary,
          unfocusedTextColor = TextPrimary
        ),
        modifier = Modifier.weight(1f)
      )
      Spacer(modifier = Modifier.width(8.dp))
      IconButton(
        onClick = { handleSend(userMessage) },
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .background(ElectricBlue)
      ) {
        Icon(imageVector = Icons.Default.Send, contentDescription = "Query", tint = Color.White)
      }
    }
  }
}
