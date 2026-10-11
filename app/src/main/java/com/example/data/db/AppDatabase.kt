package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.HistoryDao
import com.example.data.dao.ReadingDao
import com.example.data.dao.VocabularyDao
import com.example.data.dao.WritingDao
import com.example.data.model.ReadingHistoryEntry
import com.example.data.model.ReadingItem
import com.example.data.model.VocabularyWord
import com.example.data.model.WritingNote
import com.example.data.sample.SampleReadingData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ReadingItem::class,
        WritingNote::class,
        VocabularyWord::class,
        ReadingHistoryEntry::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun readingDao(): ReadingDao
    abstract fun writingDao(): WritingDao
    abstract fun vocabularyDao(): VocabularyDao
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "readscribe.db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Populate default readings and starter sample note
                            CoroutineScope(Dispatchers.IO).launch {
                                val database = getInstance(context)
                                database.readingDao().insertReadings(SampleReadingData.items)

                                // Insert welcome starter note in writing
                                database.writingDao().insertNote(
                                    WritingNote(
                                        title = "ReadScribe-ga xush kelibsiz!",
                                        content = """Bu ilova sizning mutolaa (reading) va ijodiy yozish (writing) mahoratingizni yangi bosqichga olib chiqish uchun yaratilgan.

Asosiy imkoniyatlar:
1. **Qulay Mutolaa**: Har qanday so'zni bitta bosish orqali lug'atga qo'shishingiz va talaffuzini tinglashingiz mumkin.
2. **Jumlalar Tarjimasi**: Murakkab gaplarni belgilang yoki bosing — sun'iy intellekt yoki ichki lug'at uni o'zbek tiliga tushunarli qilib izohlab beradi.
3. **O'qish Tarixi**: Oxirgi qolgan sahifangiz va o'qish foizi avtomatik saqlanib boradi.
4. **Yozuv Studiyasi**: Bu yerda o'qigan kitoblaringizdan olgan xulosalarni, yangi so'zlar bilan gaplarni va shaxsiy fikrlaringizni yozib boring!""",
                                        category = "Eslatmalar",
                                        wordCount = 92,
                                        isPinned = true
                                    )
                                )

                                // Insert starter vocabulary items
                                database.vocabularyDao().insertWord(
                                    VocabularyWord(
                                        word = "Captivate",
                                        phonetic = "/ˈkæptɪveɪt/",
                                        translation = "maftun etmoq, rom qilmoq",
                                        definition = "To attract and hold interest and attention",
                                        contextSentence = "The novel's opening scene captivated readers.",
                                        partOfSpeech = "fe'l"
                                    )
                                )
                                database.vocabularyDao().insertWord(
                                    VocabularyWord(
                                        word = "Tenacious",
                                        phonetic = "/təˈneɪʃəs/",
                                        translation = "qat'iyatli, sabotli",
                                        definition = "Tending to keep a firm hold of something",
                                        contextSentence = "However, this seed was tenacious.",
                                        partOfSpeech = "sifat"
                                    )
                                )
                                database.vocabularyDao().insertWord(
                                    VocabularyWord(
                                        word = "Meticulous",
                                        phonetic = "/məˈtɪkjələs/",
                                        translation = "o'ta sinchkov, mayda-chuydasigacha e'tiborli",
                                        definition = "Showing great attention to detail; very careful",
                                        contextSentence = "Each mosaic tile was placed with meticulous care.",
                                        partOfSpeech = "sifat"
                                    )
                                )
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
