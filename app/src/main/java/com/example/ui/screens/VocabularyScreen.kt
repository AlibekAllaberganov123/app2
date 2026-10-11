package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.VocabularyWord
import com.example.ui.components.AddWordDialog
import com.example.ui.components.SpeakEaseAudioBadge
import com.example.ui.components.SpeakEaseIconBadge
import com.example.ui.components.SpeakEaseIconButton
import com.example.ui.components.SpeakEasePrimaryButton
import com.example.ui.components.VocabularyQuizDialog
import com.example.ui.theme.PlusJakartaSans
import com.example.ui.theme.SpeakEaseCardBorder
import com.example.ui.theme.SpeakEaseCoral
import com.example.ui.theme.SpeakEaseCoralBorder
import com.example.ui.theme.SpeakEaseCoralSoft
import com.example.ui.theme.SpeakEaseMint
import com.example.ui.theme.SpeakEaseMintBorder
import com.example.ui.theme.SpeakEaseMintDark
import com.example.ui.theme.SpeakEaseMintSoft
import com.example.ui.theme.SpeakEasePrimary
import com.example.ui.theme.SpeakEasePrimaryBorder
import com.example.ui.theme.SpeakEasePrimaryDark
import com.example.ui.theme.SpeakEasePrimaryGradientEnd
import com.example.ui.theme.SpeakEasePrimaryGradientStart
import com.example.ui.theme.SpeakEasePrimarySoft
import com.example.ui.theme.SpeakEaseTextMuted
import com.example.ui.theme.SpeakEaseTextPrimary
import com.example.ui.theme.SpeakEaseTextSecondary

@Composable
fun VocabularyScreen(
    words: List<VocabularyWord>,
    allWords: List<VocabularyWord>,
    searchQuery: String,
    filter: String,
    onSearchChange: (String) -> Unit,
    onFilterChange: (String) -> Unit,
    onPlayAudio: (String) -> Unit,
    onToggleMastered: (VocabularyWord) -> Unit,
    onToggleFavorite: (VocabularyWord) -> Unit,
    onDeleteWord: (Long) -> Unit,
    onAddWord: (word: String, translation: String, phonetic: String, definition: String, example: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var showQuizDialog by remember { mutableStateOf(false) }

    val filterOptions = listOf("Barchasi", "O'rganilmoqda", "Yodlandi", "Sevimlilar")

    val masteredCount = remember(allWords) { allWords.count { it.isMastered } }
    val learningCount = remember(allWords) { allWords.count { !it.isMastered } }
    val totalCount = allWords.size
    val masteryPercent = if (totalCount > 0) (masteredCount.toFloat() / totalCount) else 0f

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // SpeakEase Mastery Progress Card
            item {
                Card(
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, SpeakEaseCardBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(SpeakEasePrimarySoft.copy(alpha = 0.6f), Color.White)
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Lug'at Boyligi 🎯",
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = SpeakEaseTextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "$totalCount ta so'zdan $masteredCount tasi yodlandi",
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 12.sp,
                                        color = SpeakEaseTextSecondary
                                    )
                                }

                                // Start Quiz Pill
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(SpeakEasePrimaryGradientStart, SpeakEasePrimaryGradientEnd)
                                            )
                                        )
                                        .clickable { showQuizDialog = true }
                                        .padding(horizontal = 14.dp, vertical = 9.dp)
                                        .testTag("start_quiz_button"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.Quiz,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Mashq",
                                            fontFamily = PlusJakartaSans,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 13.sp,
                                            color = Color.White
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Mastery Progress
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "O'zlashtirish ko'rsatkichi",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SpeakEaseTextSecondary
                                )
                                Text(
                                    text = "${(masteryPercent * 100).toInt()}%",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = SpeakEasePrimary
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            LinearProgressIndicator(
                                progress = { masteryPercent },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = SpeakEasePrimary,
                                trackColor = Color(0xFFF1F3FA)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // 2 Metric Pills (Learning & Mastered)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = SpeakEaseCoralSoft,
                                    border = BorderStroke(1.dp, SpeakEaseCoralBorder),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        SpeakEaseIconBadge(
                                            icon = Icons.Default.Bookmark,
                                            containerColor = Color.White,
                                            tintColor = SpeakEaseCoral,
                                            size = 34.dp,
                                            iconSize = 18.dp,
                                            cornerRadius = 10.dp
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                "O'rganilmoqda",
                                                fontFamily = PlusJakartaSans,
                                                fontSize = 11.sp,
                                                color = SpeakEaseCoral,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Text(
                                                "$learningCount ta",
                                                fontFamily = PlusJakartaSans,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = SpeakEaseCoral
                                            )
                                        }
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = SpeakEaseMintSoft,
                                    border = BorderStroke(1.dp, SpeakEaseMintBorder),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        SpeakEaseIconBadge(
                                            icon = Icons.Default.CheckCircle,
                                            containerColor = Color.White,
                                            tintColor = SpeakEaseMint,
                                            size = 34.dp,
                                            iconSize = 18.dp,
                                            cornerRadius = 10.dp
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                "Yodlangan",
                                                fontFamily = PlusJakartaSans,
                                                fontSize = 11.sp,
                                                color = SpeakEaseMint,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Text(
                                                "$masteredCount ta",
                                                fontFamily = PlusJakartaSans,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = SpeakEaseMint
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Search Bar with SpeakEase styling
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    placeholder = {
                        Text(
                            "So'z yoki tarjima qidirish...",
                            fontFamily = PlusJakartaSans,
                            fontSize = 14.sp
                        )
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Qidirish", tint = SpeakEasePrimary)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { onSearchChange("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Tozalash")
                            }
                        }
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = SpeakEasePrimary,
                        unfocusedIndicatorColor = SpeakEaseCardBorder
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .testTag("search_vocab_field")
                )
            }

            // Filter Chips
            item {
                LazyRow(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filterOptions) { opt ->
                        val isSelected = filter == opt
                        val count = when (opt) {
                            "O'rganilmoqda" -> learningCount
                            "Yodlandi" -> masteredCount
                            "Sevimlilar" -> allWords.count { it.isFavorite }
                            else -> totalCount
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    if (isSelected) SpeakEasePrimary else Color.White
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) SpeakEasePrimary else SpeakEaseCardBorder,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable { onFilterChange(opt) }
                                .padding(horizontal = 16.dp, vertical = 9.dp)
                        ) {
                            Text(
                                text = "$opt ($count)",
                                color = if (isSelected) Color.White else SpeakEaseTextPrimary,
                                fontFamily = PlusJakartaSans,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // Empty State
            if (words.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SpeakEaseIconBadge(
                            icon = Icons.Default.Bookmark,
                            containerColor = SpeakEasePrimarySoft,
                            tintColor = SpeakEasePrimary,
                            size = 64.dp,
                            iconSize = 32.dp,
                            cornerRadius = 22.dp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = if (searchQuery.isNotBlank()) "Qidiruv bo'yicha so'z topilmadi" else "Lug'atda hali so'zlar yo'q",
                            fontFamily = PlusJakartaSans,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SpeakEaseTextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Mutolaa bo'limiga o'ting va matndagi istalgan so'zni bosing!",
                            fontFamily = PlusJakartaSans,
                            fontSize = 13.sp,
                            color = SpeakEaseTextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }

            // Word Cards
            items(words, key = { it.id }) { wordItem ->
                SpeakEaseWordCard(
                    word = wordItem,
                    onPlayAudio = { onPlayAudio(wordItem.word) },
                    onToggleMastered = { onToggleMastered(wordItem) },
                    onToggleFavorite = { onToggleFavorite(wordItem) },
                    onDelete = { onDeleteWord(wordItem.id) }
                )
            }
        }

        // FAB
        FloatingActionButton(
            onClick = { showAddDialog = true },
            containerColor = SpeakEasePrimary,
            contentColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 90.dp, end = 20.dp)
                .shadow(8.dp, RoundedCornerShape(20.dp), spotColor = SpeakEasePrimary.copy(alpha = 0.4f))
                .testTag("add_word_fab")
        ) {
            Icon(Icons.Default.Add, contentDescription = "So'z qo'shish", modifier = Modifier.size(26.dp))
        }
    }

    if (showAddDialog) {
        AddWordDialog(
            onDismiss = { showAddDialog = false },
            onSave = { word, translation, phonetic, definition, example ->
                onAddWord(word, translation, phonetic, definition, example)
                showAddDialog = false
            }
        )
    }

    if (showQuizDialog) {
        VocabularyQuizDialog(
            words = allWords,
            onDismiss = { showQuizDialog = false },
            onPlayAudio = onPlayAudio,
            onMarkMastered = onToggleMastered
        )
    }
}

@Composable
fun SpeakEaseWordCard(
    word: VocabularyWord,
    onPlayAudio: () -> Unit,
    onToggleMastered: () -> Unit,
    onToggleFavorite: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, SpeakEaseCardBorder),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header Row: Word, Pronounce Audio, Favorite & Delete
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = word.word,
                        fontFamily = PlusJakartaSans,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SpeakEaseTextPrimary
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    SpeakEaseAudioBadge(
                        onClick = onPlayAudio,
                        size = 36.dp,
                        containerColor = SpeakEasePrimarySoft,
                        tintColor = SpeakEasePrimary
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SpeakEaseIconButton(
                        icon = if (word.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        onClick = onToggleFavorite,
                        containerColor = if (word.isFavorite) Color(0xFFFFECEE) else Color(0xFFF8F9FE),
                        tintColor = if (word.isFavorite) Color(0xFFEF4444) else SpeakEaseTextMuted,
                        size = 36.dp,
                        iconSize = 18.dp,
                        contentDescription = "Sevimlilar"
                    )

                    SpeakEaseIconButton(
                        icon = Icons.Default.DeleteOutline,
                        onClick = onDelete,
                        containerColor = Color(0xFFF8F9FE),
                        tintColor = SpeakEaseTextMuted,
                        size = 36.dp,
                        iconSize = 18.dp,
                        contentDescription = "O'chirish"
                    )
                }
            }

            if (word.phonetic.isNotBlank() || word.partOfSpeech.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (word.partOfSpeech.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SpeakEasePrimarySoft,
                            border = BorderStroke(1.dp, SpeakEasePrimaryBorder)
                        ) {
                            Text(
                                text = word.partOfSpeech,
                                fontFamily = PlusJakartaSans,
                                fontSize = 11.sp,
                                color = SpeakEasePrimary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    if (word.phonetic.isNotBlank()) {
                        Text(
                            text = word.phonetic,
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.sp,
                            color = SpeakEaseTextSecondary,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Uzbek Translation in soft card
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFF8F9FE),
                border = BorderStroke(1.dp, SpeakEaseCardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = word.translation,
                    fontFamily = PlusJakartaSans,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpeakEasePrimaryDark,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                )
            }

            if (word.definition.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = word.definition,
                    fontFamily = PlusJakartaSans,
                    fontSize = 13.sp,
                    color = SpeakEaseTextSecondary,
                    lineHeight = 18.sp
                )
            }

            if (word.contextSentence.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "\"${word.contextSentence}\"",
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    color = SpeakEaseTextMuted,
                    fontStyle = FontStyle.Italic
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Footer Toggle Mastered
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (word.sourceTitle.isNotBlank()) "📖 Mutolaadan qo'shilgan" else "✍️ Qo'lda qo'shilgan",
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.sp,
                    color = SpeakEaseTextMuted
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (word.isMastered) SpeakEaseMintSoft else SpeakEaseCoralSoft,
                    border = BorderStroke(1.dp, if (word.isMastered) SpeakEaseMintBorder else SpeakEaseCoralBorder),
                    modifier = Modifier.clickable { onToggleMastered() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (word.isMastered) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = null,
                            tint = if (word.isMastered) SpeakEaseMint else SpeakEaseCoral,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = if (word.isMastered) "Yodlangan" else "O'rganilmoqda",
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (word.isMastered) SpeakEaseMintDark else SpeakEaseCoral
                        )
                    }
                }
            }
        }
    }
}
