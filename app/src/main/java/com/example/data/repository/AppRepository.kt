package com.example.data.repository

import com.example.data.dao.HistoryDao
import com.example.data.dao.ReadingDao
import com.example.data.dao.VocabularyDao
import com.example.data.dao.WritingDao
import com.example.data.dict.OfflineDictionary
import com.example.data.model.ReadingHistoryEntry
import com.example.data.model.ReadingItem
import com.example.data.model.VocabularyWord
import com.example.data.model.WritingNote
import com.example.network.GeminiApiClient
import com.example.network.SentenceAnalysisResult
import kotlinx.coroutines.flow.Flow

class AppRepository(
    private val readingDao: ReadingDao,
    private val writingDao: WritingDao,
    private val vocabularyDao: VocabularyDao,
    private val historyDao: HistoryDao
) {
    // Readings
    fun getAllReadings(): Flow<List<ReadingItem>> = readingDao.getAllReadings()
    fun getRecentReadings(limit: Int = 5): Flow<List<ReadingItem>> = readingDao.getRecentReadings(limit)
    fun getReadingById(id: Long): Flow<ReadingItem?> = readingDao.getReadingById(id)
    suspend fun getReadingByIdSync(id: Long): ReadingItem? = readingDao.getReadingByIdSync(id)

    suspend fun saveReadingProgress(id: Long, progress: Float, paragraph: Int, isFinished: Boolean) {
        val now = System.currentTimeMillis()
        readingDao.updateReadingProgress(id, progress, paragraph, now, isFinished)

        // Also record to reading history
        val item = readingDao.getReadingByIdSync(id)
        if (item != null) {
            val progressInt = (progress * 100).toInt().coerceIn(0, 100)
            historyDao.insertHistory(
                ReadingHistoryEntry(
                    readingId = id,
                    readingTitle = item.title,
                    timestamp = now,
                    progressPercentage = progressInt,
                    timeSpentSeconds = 60
                )
            )
        }
    }

    suspend fun insertReading(item: ReadingItem): Long = readingDao.insertReading(item)
    suspend fun deleteReading(id: Long) = readingDao.deleteReading(id)

    // Notes / Writing
    fun getAllNotes(): Flow<List<WritingNote>> = writingDao.getAllNotes()
    fun searchNotes(query: String): Flow<List<WritingNote>> = writingDao.searchNotes(query)
    fun getNoteById(id: Long): Flow<WritingNote?> = writingDao.getNoteById(id)
    suspend fun getNoteByIdSync(id: Long): WritingNote? = writingDao.getNoteByIdSync(id)
    suspend fun insertOrUpdateNote(note: WritingNote): Long {
        return if (note.id == 0L) {
            writingDao.insertNote(note)
        } else {
            writingDao.updateNote(note)
            note.id
        }
    }
    suspend fun deleteNote(id: Long) = writingDao.deleteNote(id)

    // Vocabulary
    fun getAllWords(): Flow<List<VocabularyWord>> = vocabularyDao.getAllWords()
    fun searchWords(query: String): Flow<List<VocabularyWord>> = vocabularyDao.searchWords(query)
    fun getWordCount(): Flow<Int> = vocabularyDao.getWordCount()

    suspend fun addWordFromReading(
        rawWord: String,
        contextSentence: String = "",
        sourceTitle: String = ""
    ): VocabularyWord {
        val dictEntry = OfflineDictionary.lookup(rawWord)
        val existing = vocabularyDao.findWord(dictEntry.word)
        if (existing != null) {
            return existing
        }

        val newWord = VocabularyWord(
            word = dictEntry.word,
            phonetic = dictEntry.phonetic,
            translation = dictEntry.translationUz,
            definition = dictEntry.definitionEn,
            contextSentence = contextSentence,
            sourceTitle = sourceTitle,
            partOfSpeech = dictEntry.partOfSpeech
        )
        val id = vocabularyDao.insertWord(newWord)
        return newWord.copy(id = id)
    }

    suspend fun insertWord(word: VocabularyWord): Long = vocabularyDao.insertWord(word)
    suspend fun updateWord(word: VocabularyWord) = vocabularyDao.updateWord(word)
    suspend fun deleteWord(id: Long) = vocabularyDao.deleteWord(id)
    suspend fun isWordSaved(word: String): Boolean {
        return vocabularyDao.findWord(word) != null
    }

    // Reading History
    fun getAllHistory(): Flow<List<ReadingHistoryEntry>> = historyDao.getAllHistory()
    suspend fun clearHistory() = historyDao.clearHistory()

    // AI & Sentence analysis
    suspend fun analyzeSentence(sentence: String, contextTitle: String = ""): SentenceAnalysisResult {
        return GeminiApiClient.analyzeSentence(sentence, contextTitle)
    }
}
