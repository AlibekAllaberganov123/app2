package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ReadingHistoryEntry
import com.example.data.model.ReadingItem
import com.example.data.model.VocabularyWord
import com.example.data.model.WritingNote
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingDao {
    @Query("SELECT * FROM readings ORDER BY lastReadTimestamp DESC, id DESC")
    fun getAllReadings(): Flow<List<ReadingItem>>

    @Query("SELECT * FROM readings WHERE lastReadTimestamp > 0 ORDER BY lastReadTimestamp DESC LIMIT :limit")
    fun getRecentReadings(limit: Int = 5): Flow<List<ReadingItem>>

    @Query("SELECT * FROM readings WHERE id = :id LIMIT 1")
    fun getReadingById(id: Long): Flow<ReadingItem?>

    @Query("SELECT * FROM readings WHERE id = :id LIMIT 1")
    suspend fun getReadingByIdSync(id: Long): ReadingItem?

    @Query("SELECT COUNT(*) FROM readings")
    suspend fun getReadingCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReading(item: ReadingItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReadings(items: List<ReadingItem>)

    @Update
    suspend fun updateReading(item: ReadingItem)

    @Query("UPDATE readings SET scrollProgress = :progress, lastReadParagraph = :paragraph, lastReadTimestamp = :timestamp, isFinished = :isFinished WHERE id = :id")
    suspend fun updateReadingProgress(id: Long, progress: Float, paragraph: Int, timestamp: Long, isFinished: Boolean)

    @Query("DELETE FROM readings WHERE id = :id")
    suspend fun deleteReading(id: Long)
}

@Dao
interface WritingDao {
    @Query("SELECT * FROM writing_notes ORDER BY isPinned DESC, updatedAt DESC")
    fun getAllNotes(): Flow<List<WritingNote>>

    @Query("SELECT * FROM writing_notes WHERE id = :id LIMIT 1")
    fun getNoteById(id: Long): Flow<WritingNote?>

    @Query("SELECT * FROM writing_notes WHERE id = :id LIMIT 1")
    suspend fun getNoteByIdSync(id: Long): WritingNote?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: WritingNote): Long

    @Update
    suspend fun updateNote(note: WritingNote)

    @Query("DELETE FROM writing_notes WHERE id = :id")
    suspend fun deleteNote(id: Long)

    @Query("SELECT * FROM writing_notes WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' ORDER BY updatedAt DESC")
    fun searchNotes(query: String): Flow<List<WritingNote>>
}

@Dao
interface VocabularyDao {
    @Query("SELECT * FROM vocabulary_words ORDER BY createdAt DESC")
    fun getAllWords(): Flow<List<VocabularyWord>>

    @Query("SELECT * FROM vocabulary_words WHERE word LIKE '%' || :query || '%' OR translation LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun searchWords(query: String): Flow<List<VocabularyWord>>

    @Query("SELECT * FROM vocabulary_words WHERE LOWER(word) = LOWER(:word) LIMIT 1")
    suspend fun findWord(word: String): VocabularyWord?

    @Query("SELECT COUNT(*) FROM vocabulary_words")
    fun getWordCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: VocabularyWord): Long

    @Update
    suspend fun updateWord(word: VocabularyWord)

    @Query("DELETE FROM vocabulary_words WHERE id = :id")
    suspend fun deleteWord(id: Long)
}

@Dao
interface HistoryDao {
    @Query("SELECT * FROM reading_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<ReadingHistoryEntry>>

    @Query("SELECT * FROM reading_history ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentHistory(limit: Int = 10): Flow<List<ReadingHistoryEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(entry: ReadingHistoryEntry): Long

    @Query("DELETE FROM reading_history")
    suspend fun clearHistory()
}
