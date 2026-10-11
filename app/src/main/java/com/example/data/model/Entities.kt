package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "readings")
data class ReadingItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val author: String,
    val category: String,
    val level: String, // Beginner, Intermediate, Advanced
    val content: String,
    val estimatedReadMinutes: Int,
    val lastReadTimestamp: Long = 0,
    val scrollProgress: Float = 0f, // 0.0 to 1.0
    val lastReadParagraph: Int = 0,
    val isFinished: Boolean = false,
    val isCustom: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "writing_notes")
data class WritingNote(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val category: String, // Eslatmalar, Insho, Hikoya, Kundalik, Lug'at mashqi
    val wordCount: Int = 0,
    val isPinned: Boolean = false,
    val colorTag: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "vocabulary_words")
data class VocabularyWord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val word: String,
    val phonetic: String = "",
    val translation: String,
    val definition: String = "",
    val contextSentence: String = "",
    val sourceTitle: String = "",
    val partOfSpeech: String = "", // noun, verb, adjective, etc.
    val isMastered: Boolean = false,
    val isFavorite: Boolean = false,
    val reviewCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "reading_history")
data class ReadingHistoryEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val readingId: Long,
    val readingTitle: String,
    val timestamp: Long = System.currentTimeMillis(),
    val progressPercentage: Int = 0,
    val timeSpentSeconds: Int = 0
)

enum class ReadingTheme(val displayName: String) {
    LIGHT("Yorug'"),
    SEPIA("Sepia (Issiq)"),
    DARK("Tungi (Qorong'i)"),
    NIGHT("Mutlaq Qora")
}

enum class ReadingFontFamily(val displayName: String) {
    SERIF("Serif (Kitobiy)"),
    SANS_SERIF("Sans (Zamonaviy)"),
    MONOSPACE("Monospace (Aniq)")
}
