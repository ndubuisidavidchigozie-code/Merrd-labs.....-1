package com.example.ui.screens.science

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import kotlin.math.sin

data class OrganHotspot(
  val name: String,
  val system: String,
  val function: String,
  val jambHook: String,
  val clinicalFact: String,
  val posX: Float,
  val posY: Float
)

data class PhoneticsDrill(
  val word: String,
  val phoneticIpa: String,
  val stressSyl: String,
  val silentLetter: String,
  val explanation: String
)

@Composable
fun AnatomyAndEnglishScreen(onBack: () -> Unit) {
  var selectedTab by remember { mutableIntStateOf(0) } // 0: High-End Anatomy, 1: Oral English & Phonetics

  val organs = remember {
    listOf(
      OrganHotspot(
        name = "Heart (Cardiovascular)",
        system = "Circulatory System",
        function = "4-chambered myogenic pump. Right atrium receives deoxygenated vena cava blood; right ventricle propels to pulmonary circuit; left ventricle pumps oxygenated blood into aorta under 120 mmHg systolic pressure.",
        jambHook = "Left ventricular myocardium is 3× thicker than right due to high systemic peripheral vascular resistance.",
        clinicalFact = "Sinoatrial (SA) node pacemaker generates autonomous action potentials at 70–80 BPM.",
        posX = 0.48f,
        posY = 0.34f
      ),
      OrganHotspot(
        name = "Lungs & Alveoli",
        system = "Respiratory System",
        function = "Gas exchange across 300 million micro-alveoli (70 m² surface area). Surfactant lowers alveolar surface tension preventing atelectasis (collapse).",
        jambHook = "Gas diffusion obeys Fick's Law: Rate is directly proportional to alveolar surface area and concentration gradient (pO₂ in alveoli ~104 mmHg vs pulmonary capillary ~40 mmHg).",
        clinicalFact = "Diaphragm contraction flattens muscle, increasing intra-thoracic volume and dropping intra-pleural pressure to -6 mmHg.",
        posX = 0.58f,
        posY = 0.32f
      ),
      OrganHotspot(
        name = "Brain (Cerebrum & Medulla)",
        system = "Central Nervous System",
        function = "Cerebrum governs higher cognition, reasoning, and voluntary motor output. Medulla oblongata contains vital cardiac and respiratory rhythmicity pacemakers.",
        jambHook = "Medulla respiratory centers detect blood acidity (low pH caused by elevated pCO₂ in cerebrospinal fluid via carbonic acid H₂CO₃ dissociation).",
        clinicalFact = "Cerebellum coordinates fine motor precision and vestibular equilibrium.",
        posX = 0.50f,
        posY = 0.14f
      ),
      OrganHotspot(
        name = "Liver & Hepatic Portal",
        system = "Metabolic & Digestive",
        function = "Deaminates excess dietary amino acids into toxic ammonia, rapidly converted to urea via the ornithine cycle. Synthesizes bile salts for fat emulsification; stores glycogen.",
        jambHook = "Hepatic portal vein conveys blood laden with digested monosaccharides and amino acids directly from small intestinal villi to liver sinusoids.",
        clinicalFact = "Kupffer cells in liver sinusoids phagocytose worn-out erythrocytes and recycling heme iron.",
        posX = 0.43f,
        posY = 0.46f
      ),
      OrganHotspot(
        name = "Kidneys & Nephrons",
        system = "Renal Excretory System",
        function = "Over 1 million functional nephrons per kidney. Ultrafiltration occurs across fenestrated glomerular endothelium and podocyte filtration slits into Bowman's capsule.",
        jambHook = "Antidiuretic Hormone (ADH / Vasopressin) increases water reabsorption by inserting aquaporin channels into collecting duct apical membranes.",
        clinicalFact = "Countercurrent multiplier in the Loop of Henle generates an osmotic gradient up to 1200 mOsm/kg in renal medulla.",
        posX = 0.54f,
        posY = 0.53f
      )
    )
  }

  val phonetics = remember {
    listOf(
      PhoneticsDrill("Doubt", "/daʊt/", "Monosyllable", "b is silent", "English orthography retains silent 'b' from Latin dubitare."),
      PhoneticsDrill("Psychology", "/saɪˈkɒlədʒi/", "Stress on 2nd syllable (-CHOL-)", "p is silent", "Greek ps- initial cluster reduces to /s/ in English phonology."),
      PhoneticsDrill("Receipt", "/rɪˈsiːt/", "Stress on 2nd syllable (-CEIPT)", "p is silent", "Silent 'p' inserted during Renaissance by analogy to Latin recepta."),
      PhoneticsDrill("Knight", "/naɪt/", "Monosyllable", "k and gh are silent", "Old English /kn/ cluster simplified; homophone with 'night'."),
      PhoneticsDrill("Debris", "/ˈdeɪbriː/ or /dəˈbriː/", "Stress on final syllable", "s is silent", "French loanword retaining silent terminal 's'.")
    )
  }

  var selectedOrgan by remember { mutableStateOf(organs.first()) }

  // Heartbeat & Pulse animation
  val infiniteTransition = rememberInfiniteTransition(label = "cardiacPulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = 1.15f,
    animationSpec = infiniteRepeatable(
      animation = tween(400, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse"
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
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = onBack) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
      }
      Column {
        Text(
          text = "INTERACTIVE ANATOMY & ORAL ENGLISH",
          color = TextPrimary,
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace,
          letterSpacing = 1.sp
        )
        Text(
          text = "High-Fidelity Organ Systems & JAMB Phonetics",
          color = CyanAccent,
          fontSize = 10.sp
        )
      }
    }

    TabRow(
      selectedTabIndex = selectedTab,
      containerColor = ObsidianSurface,
      contentColor = ElectricBlue,
      indicator = { tabPositions ->
        TabRowDefaults.SecondaryIndicator(
          modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
          color = ElectricBlue
        )
      }
    ) {
      Tab(
        selected = selectedTab == 0,
        onClick = { selectedTab = 0 },
        text = { Text("Interactive Organ Explorer", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
      )
      Tab(
        selected = selectedTab == 1,
        onClick = { selectedTab = 1 },
        text = { Text("JAMB Phonetics & Oral Lab", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
      )
    }

    when (selectedTab) {
      0 -> {
        // High-Fidelity Anatomical Model Stage
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          // Interactive Anatomical Canvas with EKG & Pulse
          item {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(290.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF070B12))
                .border(1.5.dp, Color(0x333B82F6), RoundedCornerShape(14.dp))
            ) {
              Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Human Anatomical Contours
                val headCenter = Offset(w * 0.50f, h * 0.14f)
                drawCircle(Color(0x2238BDF8), 34.dp.toPx(), headCenter)

                // Neck & Clavicle
                drawLine(Color(0x3338BDF8), Offset(w * 0.44f, h * 0.22f), Offset(w * 0.56f, h * 0.22f), 4f)

                // Torso & Ribcage boundary
                drawRoundRect(
                  color = Color(0x1F38BDF8),
                  topLeft = Offset(w * 0.34f, h * 0.24f),
                  size = Size(w * 0.32f, h * 0.48f),
                  cornerRadius = androidx.compose.ui.geometry.CornerRadius(20f, 20f)
                )

                // Ribcage crossbars
                for (i in 1..4) {
                  val ribY = h * (0.26f + i * 0.045f)
                  drawLine(Color(0x1860A5FA), Offset(w * 0.36f, ribY), Offset(w * 0.64f, ribY), 2.5f)
                }

                // Render Organs
                organs.forEach { org ->
                  val ox = org.posX * w
                  val oy = org.posY * h
                  val isSelected = selectedOrgan.name == org.name

                  if (org.name.contains("Heart")) {
                    // Pulsating cardiac marker
                    val r = (if (isSelected) 14.dp.toPx() else 9.dp.toPx()) * pulseScale
                    drawCircle(NeonRed.copy(alpha = 0.35f), r * 1.5f, Offset(ox, oy))
                    drawCircle(NeonRed, r, Offset(ox, oy))
                    drawCircle(Color.White, 3.dp.toPx(), Offset(ox, oy))
                  } else {
                    val r = if (isSelected) 12.dp.toPx() else 7.dp.toPx()
                    val pinColor = if (isSelected) NeonGreen else ElectricBlue
                    drawCircle(pinColor.copy(alpha = 0.3f), r * 1.6f, Offset(ox, oy))
                    drawCircle(pinColor, r, Offset(ox, oy))
                    drawCircle(Color.White, 2.5.dp.toPx(), Offset(ox, oy))
                  }
                }
              }

              // Top Right EKG Cardiac Rhythm Monitor
              Box(
                modifier = Modifier
                  .align(Alignment.TopEnd)
                  .padding(10.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(Color(0xEE0F172A))
                  .border(1.dp, NeonRed.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                  .padding(horizontal = 8.dp, vertical = 5.dp)
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint = NeonRed, modifier = Modifier.size(14.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "72 BPM • NSR",
                    color = NeonRed,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                  )
                }
              }
            }
          }

          // Organ Selector Pills
          item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              items(organs) { org ->
                val isSelected = selectedOrgan.name == org.name
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) ElectricBlue else ObsidianSurface)
                    .border(1.dp, if (isSelected) ElectricBlue else ObsidianCardBorder, RoundedCornerShape(8.dp))
                    .clickable { selectedOrgan = org }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                  Text(
                    text = org.name.substringBefore(" "),
                    color = if (isSelected) Color.White else TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }
          }

          // Organ Medical & JAMB Details
          item {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(ObsidianSurface)
                .border(1.dp, ObsidianCardBorder, RoundedCornerShape(12.dp))
                .padding(16.dp)
            ) {
              Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(selectedOrgan.name, color = TextPrimary, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(6.dp))
                      .background(Color(0xFF1E293B))
                      .padding(horizontal = 8.dp, vertical = 4.dp)
                  ) {
                    Text(selectedOrgan.system, color = CyanAccent, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                  }
                }

                Column {
                  Text("PHYSIOLOGICAL MECHANISM:", color = CyanAccent, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                  Spacer(modifier = Modifier.height(2.dp))
                  Text(selectedOrgan.function, color = TextSecondary, fontSize = 13.sp, lineHeight = 18.sp)
                }

                Column {
                  Text("JAMB HIGH-YIELD EXAM HOOK:", color = NeonYellow, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                  Spacer(modifier = Modifier.height(2.dp))
                  Text(selectedOrgan.jambHook, color = TextPrimary, fontSize = 13.sp, lineHeight = 18.sp)
                }

                Column {
                  Text("CLINICAL & CELLULAR CORRELATE:", color = NeonGreen, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                  Spacer(modifier = Modifier.height(2.dp))
                  Text(selectedOrgan.clinicalFact, color = TextSecondary, fontSize = 12.sp, lineHeight = 17.sp)
                }
              }
            }
          }

          item {
            Spacer(modifier = Modifier.height(20.dp))
          }
        }
      }

      1 -> {
        // Oral English & Phonetics Lab
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          item {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(ObsidianSurface)
                .border(1.dp, ObsidianCardBorder, RoundedCornerShape(12.dp))
                .padding(16.dp)
            ) {
              Column {
                Text("JAMB ORAL ENGLISH & PHONETICS", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "IPA transcription drills, stress marking patterns, and silent consonants tested in UTME Use of English.",
                  color = TextSecondary,
                  fontSize = 12.sp
                )
              }
            }
          }

          items(phonetics) { drill ->
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(ObsidianSurface)
                .border(1.dp, ObsidianCardBorder, RoundedCornerShape(10.dp))
                .padding(14.dp)
            ) {
              Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(drill.word, color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                  Text(drill.phoneticIpa, color = CyanAccent, fontSize = 15.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(4.dp))
                      .background(Color(0x33EF4444))
                      .padding(horizontal = 6.dp, vertical = 2.dp)
                  ) {
                    Text(drill.silentLetter, color = NeonRed, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                  }
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(4.dp))
                      .background(Color(0xFF1E293B))
                      .padding(horizontal = 6.dp, vertical = 2.dp)
                  ) {
                    Text(drill.stressSyl, color = NeonGreen, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                  }
                }

                Text(drill.explanation, color = TextSecondary, fontSize = 12.sp, lineHeight = 16.sp)
              }
            }
          }
        }
      }
    }
  }
}
