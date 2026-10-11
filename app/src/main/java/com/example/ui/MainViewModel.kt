package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.dict.OfflineDictionary
import com.example.data.model.ReadingFontFamily
import com.example.data.model.ReadingHistoryEntry
import com.example.data.model.ReadingItem
import com.example.data.model.ReadingTheme
import com.example.data.model.VocabularyWord
import com.example.data.model.WritingNote
import com.example.data.repository.AppRepository
import com.example.network.SentenceAnalysisResult
import com.example.util.TtsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class Screen {
    data object ReadingList : Screen()
    data class Reader(val readingId: Long) : Screen()
    data object ReadingHistory : Screen()

    data object WritingList : Screen()
    data class WritingEditor(val noteId: Long? = null) : Screen()

    data object VocabularyList : Screen()
    data object VocabularyQuiz : Screen()
}

data class WordLookupState(
    val word: String,
    val phonetic: String,
    val translation: String,
    val definition: String,
    val partOfSpeech: String,
    val example: String,
    val contextSentence: String,
    val isSaved: Boolean,
    val isAutoAdded: Boolean
)

data class SentenceAnalysisState(
    val originalText: String,
    val translation: String,
    val explanation: String,
    val isFromAi: Boolean,
    val isLoading: Boolean = false,
    val error: String? = null
)

data class ReaderSettings(
    val fontSizeSp: Float = 18f,
    val readingTheme: ReadingTheme = ReadingTheme.SEPIA,
    val fontFamily: ReadingFontFamily = ReadingFontFamily.SERIF,
    val lineSpacingMultiplier: Float = 1.7f,
    val autoAddOnTap: Boolean = true
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AppRepository
    private val ttsManager: TtsManager = TtsManager(application)

    init {
        val db = AppDatabase.getInstance(application)
        repository = AppRepository(
            readingDao = db.readingDao(),
            writingDao = db.writingDao(),
            vocabularyDao = db.vocabularyDao(),
            historyDao = db.historyDao()
        )
    }

    // Navigation state
    private val _screenStack = MutableStateFlow<List<Screen>>(listOf(Screen.ReadingList))
    val currentScreen: StateFlow<Screen> = MutableStateFlow<Screen>(Screen.ReadingList).apply {
        viewModelScope.launch {
            _screenStack.collect { stack ->
                value = stack.lastOrNull() ?: Screen.ReadingList
            }
        }
    }

    // Readings & History
    val allReadings = repository.getAllReadings().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val recentReadings = repository.getRecentReadings().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val readingHistory = repository.getAllHistory().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    // Notes
    private val _noteSearchQuery = MutableStateFlow("")
    val noteSearchQuery = _noteSearchQuery.asStateFlow()

    private val _selectedNoteCategory = MutableStateFlow("Barchasi")
    val selectedNoteCategory = _selectedNoteCategory.asStateFlow()

    val allNotes = repository.getAllNotes().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val filteredNotes = combine(allNotes, _noteSearchQuery, _selectedNoteCategory) { notes, query, category ->
        notes.filter { note ->
            val matchesQuery = query.isBlank() ||
                    note.title.contains(query, ignoreCase = true) ||
                    note.content.contains(query, ignoreCase = true)
            val matchesCategory = category == "Barchasi" || note.category == category
            matchesQuery && matchesCategory
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Vocabulary
    private val _vocabSearchQuery = MutableStateFlow("")
    val vocabSearchQuery = _vocabSearchQuery.asStateFlow()

    private val _vocabFilter = MutableStateFlow("Barchasi") // Barchasi, O'rganilmoqda, Yodlandi, Sevimlilar
    val vocabFilter = _vocabFilter.asStateFlow()

    val allVocabulary = repository.getAllWords().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val filteredVocabulary = combine(allVocabulary, _vocabSearchQuery, _vocabFilter) { words, query, filter ->
        words.filter { word ->
            val matchesQuery = query.isBlank() ||
                    word.word.contains(query, ignoreCase = true) ||
                    word.translation.contains(query, ignoreCase = true)
            val matchesFilter = when (filter) {
                "O'rganilmoqda" -> !word.isMastered
                "Yodlandi" -> word.isMastered
                "Sevimlilar" -> word.isFavorite
                else -> true
            }
            matchesQuery && matchesFilter
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalVocabCount = repository.getWordCount().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0
    )

    // Reader UI State
    private val _currentReadingItem = MutableStateFlow<ReadingItem?>(null)
    val currentReadingItem = _currentReadingItem.asStateFlow()

    private val _readerSettings = MutableStateFlow(ReaderSettings())
    val readerSettings = _readerSettings.asStateFlow()

    private val _activeWordLookup = MutableStateFlow<WordLookupState?>(null)
    val activeWordLookup = _activeWordLookup.asStateFlow()

    private val _sentenceAnalysis = MutableStateFlow<SentenceAnalysisState?>(null)
    val sentenceAnalysis = _sentenceAnalysis.asStateFlow()

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage = _userMessage.asStateFlow()

    // Navigation methods
    fun navigateTo(screen: Screen) {
        _screenStack.update { current ->
            current + screen
        }
        if (screen is Screen.Reader) {
            loadReading(screen.readingId)
        }
    }

    fun navigateBack(): Boolean {
        if (_screenStack.value.size > 1) {
            _screenStack.update { it.dropLast(1) }
            val current = _screenStack.value.lastOrNull()
            if (current is Screen.Reader) {
                loadReading(current.readingId)
            }
            return true
        }
        return false
    }

    fun switchMainTab(screen: Screen) {
        _screenStack.value = listOf(screen)
        if (screen is Screen.Reader) {
            loadReading(screen.readingId)
        }
    }

    // Reading Operations
    private fun loadReading(id: Long) {
        viewModelScope.launch {
            val item = repository.getReadingByIdSync(id)
            _currentReadingItem.value = item
        }
    }

    fun updateReadingProgress(progress: Float, paragraph: Int) {
        val current = _currentReadingItem.value ?: return
        val isFinished = progress >= 0.95f
        _currentReadingItem.value = current.copy(
            scrollProgress = progress,
            lastReadParagraph = paragraph,
            isFinished = isFinished
        )
        viewModelScope.launch {
            repository.saveReadingProgress(current.id, progress, paragraph, isFinished)
        }
    }

    fun updateReaderSettings(
        fontSizeSp: Float? = null,
        theme: ReadingTheme? = null,
        fontFamily: ReadingFontFamily? = null,
        lineSpacing: Float? = null,
        autoAddOnTap: Boolean? = null
    ) {
        _readerSettings.update { cur ->
            cur.copy(
                fontSizeSp = fontSizeSp ?: cur.fontSizeSp,
                readingTheme = theme ?: cur.readingTheme,
                fontFamily = fontFamily ?: cur.fontFamily,
                lineSpacingMultiplier = lineSpacing ?: cur.lineSpacingMultiplier,
                autoAddOnTap = autoAddOnTap ?: cur.autoAddOnTap
            )
        }
    }

    fun addCustomReading(title: String, author: String, category: String, content: String) {
        viewModelScope.launch {
            val words = content.split(Regex("\\s+")).size
            val readMinutes = (words / 180).coerceAtLeast(1)
            val id = repository.insertReading(
                ReadingItem(
                    title = title.ifBlank { "Nomsiz matn" },
                    author = author.ifBlank { "Muallif" },
                    category = category.ifBlank { "Shaxsiy" },
                    level = "Barcha darajalar",
                    content = content,
                    estimatedReadMinutes = readMinutes,
                    isCustom = true
                )
            )
            showMessage("Yangi matn muvaffaqiyatli saqlandi!")
            navigateTo(Screen.Reader(id))
        }
    }

    fun deleteReading(id: Long) {
        viewModelScope.launch {
            repository.deleteReading(id)
            showMessage("Matn o'chirildi")
            if (currentScreen.value is Screen.Reader) {
                navigateBack()
            }
        }
    }

    // Word Lookup & Vocabulary Tap
    fun onWordTapped(rawWord: String, contextSentence: String = "") {
        val clean = rawWord.trim().replace(Regex("[^a-zA-Z]"), "")
        if (clean.isBlank()) return

        val articleTitle = _currentReadingItem.value?.title ?: ""
        val dictEntry = OfflineDictionary.lookup(clean)

        viewModelScope.launch {
            val alreadySaved = repository.isWordSaved(dictEntry.word)
            val shouldAutoAdd = _readerSettings.value.autoAddOnTap && !alreadySaved

            if (shouldAutoAdd) {
                repository.addWordFromReading(clean, contextSentence, articleTitle)
                showMessage("\"${dictEntry.word}\" avtomatik lug'atingizga qo'shildi!")
            }

            _activeWordLookup.value = WordLookupState(
                word = dictEntry.word,
                phonetic = dictEntry.phonetic,
                translation = dictEntry.translationUz,
                definition = dictEntry.definitionEn,
                partOfSpeech = dictEntry.partOfSpeech,
                example = dictEntry.example,
                contextSentence = contextSentence,
                isSaved = alreadySaved || shouldAutoAdd,
                isAutoAdded = shouldAutoAdd
            )
        }
    }

    fun toggleSaveWordFromLookup() {
        val current = _activeWordLookup.value ?: return
        viewModelScope.launch {
            if (current.isSaved) {
                // Find and delete
                val wordItem = allVocabulary.value.firstOrNull { it.word.equals(current.word, ignoreCase = true) }
                if (wordItem != null) {
                    repository.deleteWord(wordItem.id)
                    _activeWordLookup.update { it?.copy(isSaved = false) }
                    showMessage("\"${current.word}\" lug'atdan olib tashlandi")
                }
            } else {
                repository.addWordFromReading(
                    rawWord = current.word,
                    contextSentence = current.contextSentence,
                    sourceTitle = _currentReadingItem.value?.title ?: ""
                )
                _activeWordLookup.update { it?.copy(isSaved = true) }
                showMessage("\"${current.word}\" lug'atga saqlandi!")
            }
        }
    }

    fun dismissWordLookup() {
        _activeWordLookup.value = null
    }

    // Sentence Translation & AI Explanation
    fun analyzeSentence(sentence: String) {
        val trimmed = sentence.trim()
        if (trimmed.isBlank()) return

        val articleTitle = _currentReadingItem.value?.title ?: ""
        _sentenceAnalysis.value = SentenceAnalysisState(
            originalText = trimmed,
            translation = "Tarjima qilinmoqda...",
            explanation = "Tahlil tayyorlanmoqda...",
            isFromAi = false,
            isLoading = true
        )

        viewModelScope.launch {
            val result = repository.analyzeSentence(trimmed, articleTitle)
            _sentenceAnalysis.value = SentenceAnalysisState(
                originalText = trimmed,
                translation = result.translation,
                explanation = result.explanation,
                isFromAi = result.isFromAi,
                isLoading = false
            )
        }
    }

    fun dismissSentenceAnalysis() {
        _sentenceAnalysis.value = null
    }

    // Audio TTS
    fun playTts(text: String) {
        ttsManager.speak(text)
    }

    // Writing Notes Operations
    fun saveNote(id: Long, title: String, content: String, category: String, isPinned: Boolean) {
        viewModelScope.launch {
            val words = content.split(Regex("\\s+")).filter { it.isNotBlank() }.size
            val note = WritingNote(
                id = id,
                title = title.ifBlank { "Nomsiz eslatma" },
                content = content,
                category = category.ifBlank { "Eslatmalar" },
                wordCount = words,
                isPinned = isPinned,
                updatedAt = System.currentTimeMillis()
            )
            repository.insertOrUpdateNote(note)
            showMessage("Yozuv muvaffaqiyatli saqlandi!")
            navigateBack()
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            repository.deleteNote(id)
            showMessage("Eslatma o'chirildi")
        }
    }

    fun setNoteSearchQuery(query: String) {
        _noteSearchQuery.value = query
    }

    fun setNoteCategory(category: String) {
        _selectedNoteCategory.value = category
    }

    // Vocabulary Operations
    fun addManualWord(word: String, translation: String, phonetic: String, definition: String, example: String) {
        viewModelScope.launch {
            repository.insertWord(
                VocabularyWord(
                    word = word.trim(),
                    phonetic = phonetic.trim().let { if (it.isNotBlank() && !it.startsWith("/")) "/$it/" else it },
                    translation = translation.trim(),
                    definition = definition.trim(),
                    contextSentence = example.trim(),
                    partOfSpeech = "so'z",
                    createdAt = System.currentTimeMillis()
                )
            )
            showMessage("\"$word\" lug'atga qo'shildi!")
        }
    }

    fun toggleWordMastered(word: VocabularyWord) {
        viewModelScope.launch {
            val updated = word.copy(isMastered = !word.isMastered)
            repository.updateWord(updated)
            showMessage(if (updated.isMastered) "\"${word.word}\" yodlandi deb belgilandi! 🎉" else "\"${word.word}\" qayta o'rganishga o'tkazildi")
        }
    }

    fun toggleWordFavorite(word: VocabularyWord) {
        viewModelScope.launch {
            val updated = word.copy(isFavorite = !word.isFavorite)
            repository.updateWord(updated)
        }
    }

    fun deleteVocabularyWord(id: Long) {
        viewModelScope.launch {
            repository.deleteWord(id)
            showMessage("So'z lug'atdan o'chirildi")
        }
    }

    fun setVocabSearchQuery(query: String) {
        _vocabSearchQuery.value = query
    }

    fun setVocabFilter(filter: String) {
        _vocabFilter.value = filter
    }

    // Reading History
    fun clearReadingHistory() {
        viewModelScope.launch {
            repository.clearHistory()
            showMessage("O'qish tarixi tozalandi")
        }
    }

    fun showMessage(msg: String) {
        _userMessage.value = msg
    }

    fun clearMessage() {
        _userMessage.value = null
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.shutdown()
    }
}
