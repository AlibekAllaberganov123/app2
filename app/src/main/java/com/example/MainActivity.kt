package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.components.AppBottomNavBar
import com.example.ui.components.MainTab
import com.example.ui.components.SentenceAnalysisBottomSheet
import com.example.ui.components.WordLookupBottomSheet
import com.example.ui.screens.ReaderScreen
import com.example.ui.screens.ReadingHistoryScreen
import com.example.ui.screens.ReadingListScreen
import com.example.ui.screens.VocabularyScreen
import com.example.ui.screens.WritingEditorScreen
import com.example.ui.screens.WritingListScreen
import com.example.ui.theme.ReadScribeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReadScribeTheme {
                MainAppContent()
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: MainViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val allReadings by viewModel.allReadings.collectAsStateWithLifecycle()
    val recentReadings by viewModel.recentReadings.collectAsStateWithLifecycle()
    val readingHistory by viewModel.readingHistory.collectAsStateWithLifecycle()
    val currentReadingItem by viewModel.currentReadingItem.collectAsStateWithLifecycle()
    val readerSettings by viewModel.readerSettings.collectAsStateWithLifecycle()

    val filteredNotes by viewModel.filteredNotes.collectAsStateWithLifecycle()
    val allNotes by viewModel.allNotes.collectAsStateWithLifecycle()
    val noteSearchQuery by viewModel.noteSearchQuery.collectAsStateWithLifecycle()
    val selectedNoteCategory by viewModel.selectedNoteCategory.collectAsStateWithLifecycle()

    val filteredVocabulary by viewModel.filteredVocabulary.collectAsStateWithLifecycle()
    val allVocabulary by viewModel.allVocabulary.collectAsStateWithLifecycle()
    val vocabSearchQuery by viewModel.vocabSearchQuery.collectAsStateWithLifecycle()
    val vocabFilter by viewModel.vocabFilter.collectAsStateWithLifecycle()
    val totalVocabCount by viewModel.totalVocabCount.collectAsStateWithLifecycle()

    val activeWordLookup by viewModel.activeWordLookup.collectAsStateWithLifecycle()
    val sentenceAnalysis by viewModel.sentenceAnalysis.collectAsStateWithLifecycle()
    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(userMessage) {
        userMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearMessage()
        }
    }

    val shouldShowBottomBar = currentScreen is Screen.ReadingList ||
            currentScreen is Screen.WritingList ||
            currentScreen is Screen.VocabularyList

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (shouldShowBottomBar) {
                AppBottomNavBar(
                    currentScreen = currentScreen,
                    onTabSelected = { tab ->
                        when (tab) {
                            MainTab.READING -> viewModel.switchMainTab(Screen.ReadingList)
                            MainTab.WRITING -> viewModel.switchMainTab(Screen.WritingList)
                            MainTab.VOCABULARY -> viewModel.switchMainTab(Screen.VocabularyList)
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen_transition"
            ) { targetScreen ->
                when (targetScreen) {
                    is Screen.ReadingList -> {
                        ReadingListScreen(
                            readings = allReadings,
                            recentReadings = recentReadings,
                            vocabCount = totalVocabCount,
                            onOpenReading = { readingId ->
                                viewModel.navigateTo(Screen.Reader(readingId))
                            },
                            onOpenHistory = {
                                viewModel.navigateTo(Screen.ReadingHistory)
                            },
                            onAddCustomReading = { title, author, category, content ->
                                viewModel.addCustomReading(title, author, category, content)
                            }
                        )
                    }

                    is Screen.Reader -> {
                        ReaderScreen(
                            readingItem = currentReadingItem,
                            settings = readerSettings,
                            onBack = { viewModel.navigateBack() },
                            onWordTapped = { word, sentence ->
                                viewModel.onWordTapped(word, sentence)
                            },
                            onAnalyzeSentence = { sentence ->
                                viewModel.analyzeSentence(sentence)
                            },
                            onUpdateProgress = { progress, paragraph ->
                                viewModel.updateReadingProgress(progress, paragraph)
                            },
                            onUpdateSettings = { fontSize, theme, font, lineSpacing, autoAdd ->
                                viewModel.updateReaderSettings(fontSize, theme, font, lineSpacing, autoAdd)
                            },
                            onPlayAudio = { text ->
                                viewModel.playTts(text)
                            }
                        )
                    }

                    is Screen.ReadingHistory -> {
                        ReadingHistoryScreen(
                            history = readingHistory,
                            onBack = { viewModel.navigateBack() },
                            onOpenReading = { readingId ->
                                viewModel.navigateTo(Screen.Reader(readingId))
                            },
                            onClearHistory = {
                                viewModel.clearReadingHistory()
                            }
                        )
                    }

                    is Screen.WritingList -> {
                        WritingListScreen(
                            notes = filteredNotes,
                            searchQuery = noteSearchQuery,
                            selectedCategory = selectedNoteCategory,
                            onSearchQueryChange = { viewModel.setNoteSearchQuery(it) },
                            onCategoryChange = { viewModel.setNoteCategory(it) },
                            onOpenNote = { noteId ->
                                viewModel.navigateTo(Screen.WritingEditor(noteId))
                            },
                            onDeleteNote = { noteId ->
                                viewModel.deleteNote(noteId)
                            }
                        )
                    }

                    is Screen.WritingEditor -> {
                        val initialNote = remember(targetScreen.noteId, allNotes) {
                            allNotes.firstOrNull { it.id == targetScreen.noteId }
                        }
                        WritingEditorScreen(
                            initialNote = initialNote,
                            vocabularyList = allVocabulary,
                            onBack = { viewModel.navigateBack() },
                            onSave = { id, title, content, category, isPinned ->
                                viewModel.saveNote(id, title, content, category, isPinned)
                            }
                        )
                    }

                    is Screen.VocabularyList, is Screen.VocabularyQuiz -> {
                        VocabularyScreen(
                            words = filteredVocabulary,
                            allWords = allVocabulary,
                            searchQuery = vocabSearchQuery,
                            filter = vocabFilter,
                            onSearchChange = { viewModel.setVocabSearchQuery(it) },
                            onFilterChange = { viewModel.setVocabFilter(it) },
                            onPlayAudio = { word ->
                                viewModel.playTts(word)
                            },
                            onToggleMastered = { word ->
                                viewModel.toggleWordMastered(word)
                            },
                            onToggleFavorite = { word ->
                                viewModel.toggleWordFavorite(word)
                            },
                            onDeleteWord = { id ->
                                viewModel.deleteVocabularyWord(id)
                            },
                            onAddWord = { word, translation, phonetic, definition, example ->
                                viewModel.addManualWord(word, translation, phonetic, definition, example)
                            }
                        )
                    }
                }
            }

            // Word Lookup Bottom Sheet
            activeWordLookup?.let { wordState ->
                WordLookupBottomSheet(
                    state = wordState,
                    onDismiss = { viewModel.dismissWordLookup() },
                    onToggleSave = { viewModel.toggleSaveWordFromLookup() },
                    onPlayAudio = { word -> viewModel.playTts(word) }
                )
            }

            // Sentence Analysis Bottom Sheet
            sentenceAnalysis?.let { analysisState ->
                SentenceAnalysisBottomSheet(
                    state = analysisState,
                    onDismiss = { viewModel.dismissSentenceAnalysis() },
                    onPlayAudio = { text -> viewModel.playTts(text) }
                )
            }
        }
    }
}
