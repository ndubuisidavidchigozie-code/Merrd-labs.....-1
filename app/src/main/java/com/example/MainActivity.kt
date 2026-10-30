package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sync.SyncManager
import com.example.sync.SyncStatus
import com.example.ui.MerddViewModel
import com.example.ui.components.MerddBottomNav
import com.example.ui.components.MerddHeader
import com.example.ui.components.PasscodeGateScreen
import com.example.ui.components.TechIntroScreen
import com.example.ui.screens.ai.FoundersAndAiScreen
import com.example.ui.screens.calculator.CalculatorScreen
import com.example.ui.screens.cbt.CbtHubScreen
import com.example.ui.screens.gis.GisLabScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.publish.PublishScreen
import com.example.ui.screens.research.ResearchScreen
import com.example.ui.screens.science.AnatomyAndEnglishScreen
import com.example.ui.screens.science.PeriodicTableScreen
import com.example.ui.screens.simulations.SimulationsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.ObsidianBackground

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        MerddApp()
      }
    }
  }
}

@Composable
fun MerddApp(viewModel: MerddViewModel = viewModel()) {
  var hasShownTechIntro by remember { mutableStateOf(false) }

  val isUnlocked by viewModel.isUnlocked.collectAsState()
  val passcodeError by viewModel.passcodeError.collectAsState()
  val currentTab by viewModel.currentTab.collectAsState()
  val activeSubScreen by viewModel.activeSubScreen.collectAsState()
  val researchNotes by viewModel.researchNotes.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()
  val selectedCategory by viewModel.selectedCategory.collectAsState()
  val activeMistakes by viewModel.activeMistakes.collectAsState()
  val calcDisplay by viewModel.calcDisplay.collectAsState()
  val calcSubDisplay by viewModel.calcSubDisplay.collectAsState()
  val isDegreeMode by viewModel.isDegreeMode.collectAsState()
  val isShiftActive by viewModel.isShiftActive.collectAsState()
  val isAlphaActive by viewModel.isAlphaActive.collectAsState()
  val calcHistory by viewModel.calcHistory.collectAsState()
  val cbtQuestions by viewModel.cbtActiveQuestions.collectAsState()
  val currentQIndex by viewModel.currentQuestionIndex.collectAsState()
  val selectedAnswers by viewModel.selectedAnswers.collectAsState()
  val questionTimes by viewModel.questionTimes.collectAsState()
  val cbtSubmitted by viewModel.cbtSubmitted.collectAsState()

  // Sync Manager
  val context = androidx.compose.ui.platform.LocalContext.current
  val syncManager = remember { SyncManager.getInstance(context) }
  val syncStatus by syncManager.syncStatus.collectAsState()

  // Back handling
  BackHandler(enabled = activeSubScreen != null || currentTab != 0) {
    if (activeSubScreen != null) {
      viewModel.closeSubScreen()
    } else {
      viewModel.selectTab(0)
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(ObsidianBackground)
  ) {
    when {
      !hasShownTechIntro -> {
        TechIntroScreen(onComplete = { hasShownTechIntro = true })
      }
      !isUnlocked -> {
        PasscodeGateScreen(
          onUnlock = { code -> viewModel.unlockWithPasscode(code) },
          hasError = passcodeError,
          onClearError = { viewModel.clearPasscodeError() }
        )
      }
      activeSubScreen != null -> {
        when (activeSubScreen) {
          "CALCULATOR" -> {
            CalculatorScreen(
              display = calcDisplay,
              subDisplay = calcSubDisplay,
              isDegreeMode = isDegreeMode,
              isShiftActive = isShiftActive,
              isAlphaActive = isAlphaActive,
              calcHistory = calcHistory,
              storageInfo = viewModel.getStorageInfo(),
              onToken = { viewModel.appendCalcToken(it) },
              onClear = { viewModel.clearCalc() },
              onDelete = { viewModel.deleteCalcToken() },
              onEquals = { viewModel.executeEquals() },
              onToggleDegree = { viewModel.toggleDegreeMode() },
              onToggleShift = { viewModel.toggleShift() },
              onToggleAlpha = { viewModel.toggleAlpha() },
              onPurgeHistory = { viewModel.purgeCalcHistory() },
              onBack = { viewModel.closeSubScreen() }
            )
          }
          "GIS" -> {
            GisLabScreen(onBack = { viewModel.closeSubScreen() })
          }
          "CBT" -> {
            CbtHubScreen(
              questions = cbtQuestions,
              currentIndex = currentQIndex,
              selectedAnswers = selectedAnswers,
              questionTimes = questionTimes,
              isSubmitted = cbtSubmitted,
              mistakes = activeMistakes,
              topicMasteries = viewModel.getTopicMasteries(),
              onSelectAnswer = { qId, opt, timeSec -> viewModel.selectAnswer(qId, opt, timeSec) },
              onNavigateQuestion = { idx -> viewModel.setQuestionIndex(idx) },
              onSubmitTest = {
                viewModel.submitCbtTest()
                syncManager.broadcastQuizResult(
                  "JAMB UTME Comprehensive",
                  selectedAnswers.count { it.value == cbtQuestions.find { q -> q.id == it.key }?.correctOption },
                  cbtQuestions.size,
                  3,
                  180
                )
              },
              onResetTest = { viewModel.resetCbtTest() },
              onAnswerMistake = { mistake, char -> viewModel.answerMistake(mistake, char) },
              onQueueWeakTopics = { viewModel.queueWeakTopics() },
              onBack = { viewModel.closeSubScreen() }
            )
          }
          "SIMULATIONS" -> {
            SimulationsScreen(onBack = { viewModel.closeSubScreen() })
          }
          "PERIODIC_TABLE" -> {
            PeriodicTableScreen(onBack = { viewModel.closeSubScreen() })
          }
          "ANATOMY" -> {
            AnatomyAndEnglishScreen(onBack = { viewModel.closeSubScreen() })
          }
          "WHITEBOARD" -> {
            com.example.ui.screens.whiteboard.WhiteboardScreen(onBack = { viewModel.closeSubScreen() })
          }
          "ORIGINS" -> {
            com.example.ui.components.LabOriginsModal(onDismiss = { viewModel.closeSubScreen() })
          }
          "LAB_AI" -> {
            FoundersAndAiScreen(
              researchNotes = researchNotes,
              onNavigateToNote = { note ->
                viewModel.closeSubScreen()
                viewModel.selectTab(1)
                viewModel.updateSearchQuery(note.title)
              },
              onNavigateToPublish = {
                viewModel.closeSubScreen()
                viewModel.selectTab(2)
              },
              onBack = { viewModel.closeSubScreen() }
            )
          }
        }
      }
      else -> {
        // Core 3-Tab Experience (Home, Research, Publish)
        Scaffold(
          topBar = {
            MerddHeader(
              syncStatus = syncStatus,
              onCycleSync = {
                val next = when (syncStatus) {
                  SyncStatus.SYNCED_LIVE -> SyncStatus.RECONNECTING
                  SyncStatus.RECONNECTING -> SyncStatus.OFFLINE_LOCAL
                  SyncStatus.OFFLINE_LOCAL -> SyncStatus.SYNCED_LIVE
                }
                syncManager.setNetworkSimulation(next)
              },
              onLock = { viewModel.lockLab() }
            )
          },
          bottomBar = {
            MerddBottomNav(
              selectedTab = currentTab,
              onSelectTab = { viewModel.selectTab(it) }
            )
          },
          containerColor = ObsidianBackground
        ) { innerPadding ->
          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding(innerPadding)
          ) {
            Crossfade(targetState = currentTab, label = "tabTransition") { tab ->
              when (tab) {
                0 -> HomeScreen(
                  notes = researchNotes,
                  activeMistakeCount = activeMistakes.count { !it.cleared },
                  onOpenSubScreen = { screenId -> viewModel.openSubScreen(screenId) },
                  onViewAllResearch = { viewModel.selectTab(1) }
                )
                1 -> ResearchScreen(
                  notes = researchNotes,
                  searchQuery = searchQuery,
                  selectedCategory = selectedCategory,
                  onSearchChange = { viewModel.updateSearchQuery(it) },
                  onCategorySelect = { viewModel.selectCategory(it) },
                  onLikeNote = { viewModel.likeNote(it) }
                )
                2 -> PublishScreen(
                  onPublish = { title, content, author, category, tags ->
                    viewModel.publishNote(title, content, author, category, tags)
                    syncManager.queueSync("PUBLISH_NOTE", title, "$author: $title")
                  }
                )
              }
            }
          }
        }
      }
    }
  }
}
