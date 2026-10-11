package com.example

import com.example.data.dict.OfflineDictionary
import com.example.data.model.ReadingItem
import com.example.data.model.WritingNote
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun dictionary_lookup_findsCommonWords() {
        val entry = OfflineDictionary.lookup("captivate")
        assertEquals("captivate", entry.word.lowercase())
        assertTrue(entry.translationUz.contains("maftun"))
        assertEquals("fe'l", entry.partOfSpeech)
    }

    @Test
    fun dictionary_lookup_handlesStemmedWords() {
        val entry = OfflineDictionary.lookup("captivating")
        assertEquals("captivate", entry.word.lowercase())
    }

    @Test
    fun sentence_analysis_providesTranslationsAndBreakdown() {
        val (trans, expl) = OfflineDictionary.analyzeSentenceOffline("She had a brilliant ambition to acquire wisdom.")
        assertNotNull(trans)
        assertNotNull(expl)
        assertTrue(expl.contains("ambition") || expl.contains("wisdom") || expl.contains("brilliant"))
    }

    @Test
    fun reading_item_creation_isCorrect() {
        val item = ReadingItem(
            id = 1,
            title = "Test Article",
            author = "Test Author",
            category = "Test Category",
            level = "Intermediate",
            content = "This is a sample content for reading test.",
            estimatedReadMinutes = 2
        )
        assertEquals("Test Article", item.title)
        assertEquals(2, item.estimatedReadMinutes)
        assertEquals(0f, item.scrollProgress)
    }

    @Test
    fun writing_note_creation_isCorrect() {
        val note = WritingNote(
            id = 1,
            title = "My Thoughts",
            content = "Testing writing note creation.",
            category = "Insho",
            wordCount = 4
        )
        assertEquals("My Thoughts", note.title)
        assertEquals(4, note.wordCount)
    }
}
