package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.VocabularyWord
import com.example.ui.theme.PlusJakartaSans
import com.example.ui.theme.SpeakEaseAmber
import com.example.ui.theme.SpeakEaseAmberBorder
import com.example.ui.theme.SpeakEaseAmberSoft
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

enum class QuizMode {
    FLASHCARDS,
    MULTIPLE_CHOICE
}

@Composable
fun VocabularyQuizDialog(
    words: List<VocabularyWord>,
    onDismiss: () -> Unit,
    onPlayAudio: (String) -> Unit,
    onMarkMastered: (VocabularyWord) -> Unit
) {
    if (words.isEmpty()) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, SpeakEaseCardBorder),
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(26.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SpeakEaseIconBadge(
                        icon = Icons.Default.Quiz,
                        containerColor = SpeakEasePrimarySoft,
                        tintColor = SpeakEasePrimary,
                        size = 56.dp,
                        iconSize = 28.dp,
                        cornerRadius = 18.dp
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Lug'at Hali Bo'sh",
                        fontFamily = PlusJakartaSans,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SpeakEaseTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Mashq qilish uchun avval matnlarni o'qib, yangi so'zlarni saqlang!",
                        textAlign = TextAlign.Center,
                        fontFamily = PlusJakartaSans,
                        fontSize = 13.sp,
                        color = SpeakEaseTextSecondary
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    SpeakEasePrimaryButton(
                        text = "Tushunarli",
                        onClick = onDismiss
                    )
                }
            }
        }
        return
    }

    var quizMode by remember { mutableStateOf(QuizMode.FLASHCARDS) }
    var currentIndex by remember { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }

    // Multiple choice state
    var selectedAnswerIndex by remember { mutableStateOf<Int?>(null) }
    var score by remember { mutableIntStateOf(0) }
    var totalAnswered by remember { mutableIntStateOf(0) }

    val currentWord = words[currentIndex % words.size]

    // Generate 4 options for multiple choice
    val options = remember(currentIndex, words) {
        val correctAnswer = currentWord.translation
        val wrongAnswers = (words.filter { it.id != currentWord.id }.map { it.translation } +
                listOf("qobiliyat", "o'rganmoq", "ajoyib, chiroyli", "foydali, muhim", "teran fikr"))
            .distinct()
            .shuffled()
            .take(3)
        (wrongAnswers + correctAnswer).shuffled()
    }

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 350),
        label = "card_flip"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, SpeakEaseCardBorder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(22.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Top Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SpeakEaseIconBadge(
                            icon = Icons.Default.Quiz,
                            containerColor = SpeakEasePrimarySoft,
                            tintColor = SpeakEasePrimary,
                            size = 38.dp,
                            iconSize = 20.dp,
                            cornerRadius = 12.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Lug'at Mashqi 🎯",
                            fontFamily = PlusJakartaSans,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SpeakEaseTextPrimary
                        )
                    }

                    SpeakEaseIconButton(
                        icon = Icons.Default.Close,
                        onClick = onDismiss,
                        containerColor = Color(0xFFF1F3FA),
                        tintColor = SpeakEaseTextPrimary,
                        size = 34.dp,
                        iconSize = 18.dp,
                        contentDescription = "Yopish"
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Mode Selector Segmented Pill
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFF1F3FA))
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(13.dp))
                            .background(if (quizMode == QuizMode.FLASHCARDS) Color.White else Color.Transparent)
                            .clickable {
                                quizMode = QuizMode.FLASHCARDS
                                isFlipped = false
                            }
                            .padding(vertical = 9.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🃏 Flashcard",
                            fontFamily = PlusJakartaSans,
                            fontSize = 13.sp,
                            fontWeight = if (quizMode == QuizMode.FLASHCARDS) FontWeight.ExtraBold else FontWeight.SemiBold,
                            color = if (quizMode == QuizMode.FLASHCARDS) SpeakEasePrimary else SpeakEaseTextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(13.dp))
                            .background(if (quizMode == QuizMode.MULTIPLE_CHOICE) Color.White else Color.Transparent)
                            .clickable {
                                quizMode = QuizMode.MULTIPLE_CHOICE
                                selectedAnswerIndex = null
                            }
                            .padding(vertical = 9.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🎯 Test Sinovi",
                            fontFamily = PlusJakartaSans,
                            fontSize = 13.sp,
                            fontWeight = if (quizMode == QuizMode.MULTIPLE_CHOICE) FontWeight.ExtraBold else FontWeight.SemiBold,
                            color = if (quizMode == QuizMode.MULTIPLE_CHOICE) SpeakEasePrimary else SpeakEaseTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Step Counter & XP Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "So'z: ${((currentIndex % words.size) + 1)} / ${words.size}",
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SpeakEaseTextSecondary
                    )

                    SpeakEaseXpBadge(xp = score * 15 + 10)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // ===================================
                // 1. FLASHCARD MODE
                // ===================================
                if (quizMode == QuizMode.FLASHCARDS) {
                    // 3D Flipping Card
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isFlipped) SpeakEasePrimarySoft else Color(0xFFF8F9FE)
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (isFlipped) SpeakEasePrimaryBorder else SpeakEaseCardBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .graphicsLayer {
                                rotationY = rotation
                                cameraDistance = 12f * density
                            }
                            .clickable { isFlipped = !isFlipped }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (rotation <= 90f) {
                                // FRONT: English word
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = currentWord.word,
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = SpeakEaseTextPrimary
                                    )

                                    if (currentWord.phonetic.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = currentWord.phonetic,
                                            fontFamily = PlusJakartaSans,
                                            fontSize = 14.sp,
                                            color = SpeakEaseTextSecondary,
                                            fontStyle = FontStyle.Italic
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))

                                    SpeakEaseAudioBadge(
                                        onClick = { onPlayAudio(currentWord.word) },
                                        size = 44.dp,
                                        containerColor = Color.White,
                                        tintColor = SpeakEasePrimary
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Text(
                                        text = "Aylantirish uchun bosing 👆",
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 11.sp,
                                        color = SpeakEaseTextMuted,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            } else {
                                // BACK: Uzbek translation & Definition
                                Column(
                                    modifier = Modifier.graphicsLayer { rotationY = 180f },
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = currentWord.translation,
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = SpeakEasePrimaryDark,
                                        textAlign = TextAlign.Center
                                    )

                                    if (currentWord.definition.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = currentWord.definition,
                                            fontFamily = PlusJakartaSans,
                                            fontSize = 13.sp,
                                            color = SpeakEaseTextSecondary,
                                            textAlign = TextAlign.Center
                                        )
                                    }

                                    if (currentWord.contextSentence.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "\"${currentWord.contextSentence}\"",
                                            fontFamily = PlusJakartaSans,
                                            fontSize = 12.sp,
                                            fontStyle = FontStyle.Italic,
                                            color = SpeakEaseTextMuted,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Next / Result Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SpeakEaseSecondaryButton(
                            text = "Qaytarish 🔄",
                            containerColor = SpeakEaseCoralSoft,
                            contentColor = SpeakEaseCoral,
                            borderColor = SpeakEaseCoralBorder,
                            onClick = {
                                currentIndex++
                                isFlipped = false
                            },
                            modifier = Modifier.weight(1f)
                        )

                        SpeakEasePrimaryButton(
                            text = "Yodladim! ✅",
                            gradientColors = listOf(SpeakEaseMint, SpeakEaseMint),
                            onClick = {
                                onMarkMastered(currentWord)
                                score++
                                currentIndex++
                                isFlipped = false
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // ===================================
                // 2. MULTIPLE CHOICE TEST MODE
                // ===================================
                if (quizMode == QuizMode.MULTIPLE_CHOICE) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFFF8F9FE),
                        border = BorderStroke(1.dp, SpeakEaseCardBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Bu so'zning ma'nosini toping:",
                                fontFamily = PlusJakartaSans,
                                fontSize = 12.sp,
                                color = SpeakEaseTextSecondary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = currentWord.word,
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = SpeakEaseTextPrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                SpeakEaseAudioBadge(
                                    onClick = { onPlayAudio(currentWord.word) },
                                    size = 36.dp,
                                    containerColor = Color.White,
                                    tintColor = SpeakEasePrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 4 Options
                    options.forEachIndexed { optIndex, optionText ->
                        val letter = ('A' + optIndex).toString()
                        val isSelected = selectedAnswerIndex == optIndex
                        val isCorrectOption = optionText == currentWord.translation

                        val isCorrectResult = if (selectedAnswerIndex != null) {
                            if (isSelected) isCorrectOption else if (isCorrectOption) true else null
                        } else null

                        SpeakEaseQuizOptionButton(
                            letter = letter,
                            text = optionText,
                            isSelected = isSelected,
                            isCorrect = isCorrectResult,
                            onClick = {
                                if (selectedAnswerIndex == null) {
                                    selectedAnswerIndex = optIndex
                                    totalAnswered++
                                    if (isCorrectOption) {
                                        score++
                                        onMarkMastered(currentWord)
                                    }
                                }
                            },
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (selectedAnswerIndex != null) {
                        SpeakEasePrimaryButton(
                            text = "Keyingi savol ➔",
                            onClick = {
                                currentIndex++
                                selectedAnswerIndex = null
                            }
                        )
                    }
                }
            }
        }
    }
}
