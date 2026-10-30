package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MerddRepository
import com.example.data.SeedData
import com.example.model.CbtQuestion
import com.example.model.GisFeature
import com.example.model.MistakeCategory
import com.example.model.MistakeItem
import com.example.model.NigerianState
import com.example.model.ResearchNote
import com.example.model.TopicMastery
import com.example.util.CalculatorEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MerddViewModel(application: Application) : AndroidViewModel(application) {
  private val repository = MerddRepository(application)

  // Auth & Boot State
  private val _isUnlocked = MutableStateFlow(repository.isLabUnlocked())
  val isUnlocked: StateFlow<Boolean> = _isUnlocked.asStateFlow()

  private val _passcodeError = MutableStateFlow(false)
  val passcodeError: StateFlow<Boolean> = _passcodeError.asStateFlow()

  fun unlockWithPasscode(code: String): Boolean {
    val ok = repository.verifyPasscode(code)
    if (ok) {
      _isUnlocked.value = true
      _passcodeError.value = false
    } else {
      _passcodeError.value = true
    }
    return ok
  }

  fun lockLab() {
    repository.setLabUnlocked(false)
    _isUnlocked.value = false
  }

  fun clearPasscodeError() {
    _passcodeError.value = false
  }

  // Navigation: 0 = Home, 1 = Research, 2 = Publish, or sub-tools (3=Calculator, 4=GIS, 5=Simulations, 6=CBT, 7=PeriodicTable, 8=Anatomy, 9=AI)
  private val _currentTab = MutableStateFlow(0)
  val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

  private val _activeSubScreen = MutableStateFlow<String?>(null)
  val activeSubScreen: StateFlow<String?> = _activeSubScreen.asStateFlow()

  fun selectTab(tab: Int) {
    _currentTab.value = tab
    _activeSubScreen.value = null
  }

  fun openSubScreen(screenName: String) {
    _activeSubScreen.value = screenName
  }

  fun closeSubScreen() {
    _activeSubScreen.value = null
  }

  // Research Feed & Live Search Highlighting
  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _selectedCategory = MutableStateFlow("All")
  val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

  val researchNotes: StateFlow<List<ResearchNote>> = combine(
    repository.allNotes,
    _searchQuery,
    _selectedCategory
  ) { notes, query, category ->
    notes.filter { note ->
      val matchesCategory = (category == "All" || note.category.equals(category, ignoreCase = true) || note.tags.contains(category, ignoreCase = true))
      val matchesQuery = if (query.isBlank()) true else {
        note.title.contains(query, ignoreCase = true) ||
          note.content.contains(query, ignoreCase = true) ||
          note.tags.contains(query, ignoreCase = true) ||
          note.author.contains(query, ignoreCase = true)
      }
      matchesCategory && matchesQuery
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  fun updateSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun selectCategory(cat: String) {
    _selectedCategory.value = cat
  }

  fun publishNote(title: String, content: String, author: String, category: String, tags: String) {
    viewModelScope.launch {
      val note = ResearchNote(
        title = title.trim(),
        content = content.trim(),
        author = author,
        category = category,
        tags = tags.trim()
      )
      repository.insertNote(note)
      // Switch to Research tab to view published note
      _currentTab.value = 1
      _searchQuery.value = ""
      _selectedCategory.value = "All"
    }
  }

  fun likeNote(id: Long) {
    viewModelScope.launch {
      repository.likeNote(id)
    }
  }

  // CBT & Mistake Bank
  val activeMistakes: StateFlow<List<MistakeItem>> = repository.activeMistakes
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  private val _selectedMistakeCategoryFilter = MutableStateFlow<String?>("All")
  val selectedMistakeCategoryFilter: StateFlow<String?> = _selectedMistakeCategoryFilter.asStateFlow()

  fun filterMistakesByCategory(cat: String?) {
    _selectedMistakeCategoryFilter.value = cat
  }

  // Active CBT test session
  private val _cbtActiveQuestions = MutableStateFlow<List<CbtQuestion>>(SeedData.cbtQuestions)
  val cbtActiveQuestions: StateFlow<List<CbtQuestion>> = _cbtActiveQuestions.asStateFlow()

  private val _currentQuestionIndex = MutableStateFlow(0)
  val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

  private val _selectedAnswers = MutableStateFlow<Map<Int, Char>>(emptyMap())
  val selectedAnswers: StateFlow<Map<Int, Char>> = _selectedAnswers.asStateFlow()

  private val _questionTimes = MutableStateFlow<Map<Int, Long>>(emptyMap()) // seconds
  val questionTimes: StateFlow<Map<Int, Long>> = _questionTimes.asStateFlow()

  private val _cbtSubmitted = MutableStateFlow(false)
  val cbtSubmitted: StateFlow<Boolean> = _cbtSubmitted.asStateFlow()

  fun selectAnswer(questionId: Int, option: Char, timeSpentSeconds: Long) {
    _selectedAnswers.value = _selectedAnswers.value + (questionId to option)
    _questionTimes.value = _questionTimes.value + (questionId to timeSpentSeconds)
  }

  fun setQuestionIndex(index: Int) {
    if (index in 0 until _cbtActiveQuestions.value.size) {
      _currentQuestionIndex.value = index
    }
  }

  fun submitCbtTest() {
    _cbtSubmitted.value = true
    viewModelScope.launch {
      val questions = _cbtActiveQuestions.value
      val answers = _selectedAnswers.value
      val times = _questionTimes.value

      questions.forEach { q ->
        val userAns = answers[q.id]
        val timeSpent = times[q.id] ?: 30L

        if (userAns != null && userAns != q.correctOption) {
          // Categorize mistake
          val cat = when {
            timeSpent > 120 -> MistakeCategory.TIME_PRESSURE
            q.questionText.contains("NOT", ignoreCase = false) || q.questionText.contains("EXCEPT", ignoreCase = false) -> MistakeCategory.MISREAD_QUESTION
            q.formula != null -> MistakeCategory.CALCULATION_ERROR
            else -> MistakeCategory.CONCEPTUAL_GAP
          }

          repository.insertMistake(
            MistakeItem(
              questionId = q.id,
              subject = q.subject,
              topic = q.topic,
              questionText = q.questionText,
              optionA = q.optionA,
              optionB = q.optionB,
              optionC = q.optionC,
              optionD = q.optionD,
              correctOption = q.correctOption,
              userSelectedOption = userAns,
              explanation = q.explanation,
              category = cat.name,
              consecutiveCorrect = 0,
              cleared = false,
              timeSpentSeconds = timeSpent
            )
          )
        }
      }
    }
  }

  fun resetCbtTest(customQuestions: List<CbtQuestion>? = null) {
    _cbtActiveQuestions.value = customQuestions ?: SeedData.cbtQuestions
    _currentQuestionIndex.value = 0
    _selectedAnswers.value = emptyMap()
    _questionTimes.value = emptyMap()
    _cbtSubmitted.value = false
  }

  // Answer mistake in Mistake Bank drill
  fun answerMistake(item: MistakeItem, selectedChar: Char): Boolean {
    val isCorrect = selectedChar == item.correctOption
    viewModelScope.launch {
      repository.recordMistakeAnswer(item.id, isCorrect)
    }
    return isCorrect
  }

  // Diagnostic Topic Mastery calculations
  fun getTopicMasteries(): List<TopicMastery> {
    val list = mutableListOf<TopicMastery>()
    val grouped = SeedData.cbtQuestions.groupBy { "${it.subject} - ${it.topic}" }
    val answers = _selectedAnswers.value

    grouped.forEach { (key, questions) ->
      val parts = key.split(" - ")
      val subject = parts[0]
      val topic = parts.getOrElse(1) { "General" }
      var correct = 0
      var total = 0
      questions.forEach { q ->
        val ans = answers[q.id]
        if (ans != null) {
          total++
          if (ans == q.correctOption) correct++
        }
      }
      if (total == 0) {
        // baseline sample mastery
        total = 4
        correct = if (topic.contains("Mechanics") || topic.contains("Titration")) 1 else 3
      }
      list.add(TopicMastery(subject, topic, correct, total))
    }
    return list
  }

  fun queueWeakTopics() {
    val weakTopics = getTopicMasteries().filter { it.percentage < 75f }.map { it.topic }
    val filteredQuestions = SeedData.cbtQuestions.filter { q ->
      weakTopics.any { wt -> q.topic.contains(wt, ignoreCase = true) }
    }
    if (filteredQuestions.isNotEmpty()) {
      resetCbtTest(filteredQuestions)
    } else {
      resetCbtTest()
    }
  }

  // SC-991EX Calculator Engine State
  private val _calcDisplay = MutableStateFlow("0")
  val calcDisplay: StateFlow<String> = _calcDisplay.asStateFlow()

  private val _calcSubDisplay = MutableStateFlow("")
  val calcSubDisplay: StateFlow<String> = _calcSubDisplay.asStateFlow()

  private val _isDegreeMode = MutableStateFlow(true) // DEG vs RAD
  val isDegreeMode: StateFlow<Boolean> = _isDegreeMode.asStateFlow()

  private val _isShiftActive = MutableStateFlow(false)
  val isShiftActive: StateFlow<Boolean> = _isShiftActive.asStateFlow()

  private val _isAlphaActive = MutableStateFlow(false)
  val isAlphaActive: StateFlow<Boolean> = _isAlphaActive.asStateFlow()

  private val _lastAnswer = MutableStateFlow(0.0)
  val lastAnswer: StateFlow<Double> = _lastAnswer.asStateFlow()

  private val _memoryValue = MutableStateFlow(0.0)
  val memoryValue: StateFlow<Double> = _memoryValue.asStateFlow()

  val calcHistory: MutableStateFlow<List<String>> = MutableStateFlow(repository.getCalcHistory())

  fun appendCalcToken(token: String) {
    if (_calcDisplay.value == "0" && token !in listOf(".", "+", "−", "×", "÷", "%", "^")) {
      _calcDisplay.value = token
    } else {
      _calcDisplay.value += token
    }
  }

  fun clearCalc() {
    _calcDisplay.value = "0"
    _calcSubDisplay.value = ""
  }

  fun deleteCalcToken() {
    if (_calcDisplay.value.length > 1) {
      _calcDisplay.value = _calcDisplay.value.dropLast(1)
    } else {
      _calcDisplay.value = "0"
    }
  }

  fun toggleDegreeMode() {
    _isDegreeMode.value = !_isDegreeMode.value
  }

  fun toggleShift() {
    _isShiftActive.value = !_isShiftActive.value
  }

  fun toggleAlpha() {
    _isAlphaActive.value = !_isAlphaActive.value
  }

  fun executeEquals() {
    val expr = _calcDisplay.value
    try {
      val res = CalculatorEngine.evaluate(expr, _isDegreeMode.value, _lastAnswer.value)
      val formattedRes = if (res % 1.0 == 0.0) res.toLong().toString() else "%.6f".format(res).trimEnd('0').trimEnd('.')
      _calcSubDisplay.value = "$expr ="
      _calcDisplay.value = formattedRes
      _lastAnswer.value = res
      repository.saveCalcExpression(expr, formattedRes)
      calcHistory.value = repository.getCalcHistory()
    } catch (e: Exception) {
      _calcSubDisplay.value = "Syntax / Math Error"
    }
  }

  fun purgeCalcHistory() {
    repository.clearCalcHistory()
    calcHistory.value = emptyList()
  }

  fun getStorageInfo(): Pair<String, Float> {
    val bytes = repository.getStorageUsageBytes()
    val mb = bytes.toFloat() / (1024f * 1024f)
    val totalMb = 5.0f
    val fraction = (mb / totalMb).coerceIn(0f, 1f)
    val text = "%.2f MB / 5.0 MB Used".format(mb)
    return Pair(text, fraction)
  }

  // GIS Lab State
  private val _gisPreset = MutableStateFlow("Nigeria") // "Nigeria", "West Africa", "Full Africa"
  val gisPreset: StateFlow<String> = _gisPreset.asStateFlow()

  val showRivers = MutableStateFlow(true)
  val showRelief = MutableStateFlow(true)
  val showMinerals = MutableStateFlow(true)
  val showVegetation = MutableStateFlow(true)
  val showStateLines = MutableStateFlow(true)

  private val _selectedGisFeature = MutableStateFlow<GisFeature?>(SeedData.gisFeatures.first())
  val selectedGisFeature: StateFlow<GisFeature?> = _selectedGisFeature.asStateFlow()

  private val _selectedState = MutableStateFlow<NigerianState?>(null)
  val selectedState: StateFlow<NigerianState?> = _selectedState.asStateFlow()

  fun setGisPreset(preset: String) {
    _gisPreset.value = preset
  }

  fun selectGisFeature(feature: GisFeature?) {
    _selectedGisFeature.value = feature
    _selectedState.value = null
  }

  fun selectNigerianState(state: NigerianState?) {
    _selectedState.value = state
    _selectedGisFeature.value = null
  }
}
