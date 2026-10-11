package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.ReadingFontFamily
import com.example.data.model.ReadingTheme
import com.example.ui.ReaderSettings
import com.example.ui.SentenceAnalysisState
import com.example.ui.WordLookupState
import com.example.ui.theme.PaperDarkBg
import com.example.ui.theme.PaperLightBg
import com.example.ui.theme.PaperNightBg
import com.example.ui.theme.PaperSepiaBg
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordLookupBottomSheet(
    state: WordLookupState,
    onDismiss: () -> Unit,
    onToggleSave: () -> Unit,
    onPlayAudio: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        modifier = Modifier.testTag("word_lookup_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            // Header with Word, Phonetic and Pronounce Audio
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = state.word,
                            fontFamily = PlusJakartaSans,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SpeakEaseTextPrimary
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        if (state.partOfSpeech.isNotBlank()) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = SpeakEasePrimarySoft,
                                border = BorderStroke(1.dp, SpeakEasePrimaryBorder)
                            ) {
                                Text(
                                    text = state.partOfSpeech,
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SpeakEasePrimary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    if (state.phonetic.isNotBlank()) {
                        Text(
                            text = state.phonetic,
                            fontFamily = PlusJakartaSans,
                            fontSize = 14.sp,
                            color = SpeakEaseTextSecondary,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }

                // Audio Button in SpeakEase style
                SpeakEaseAudioBadge(
                    onClick = { onPlayAudio(state.word) },
                    size = 46.dp,
                    containerColor = SpeakEasePrimarySoft,
                    tintColor = SpeakEasePrimary,
                    testTag = "tts_play_button"
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Uzbek Translation Card
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = SpeakEasePrimarySoft,
                border = BorderStroke(1.dp, SpeakEasePrimaryBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "O'zbekcha tarjimasi:",
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        color = SpeakEasePrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = state.translation,
                        fontFamily = PlusJakartaSans,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SpeakEasePrimaryDark
                    )
                }
            }

            // Definition & Example
            if (state.definition.isNotBlank()) {
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Ta'rif (Ingliz tilida):",
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpeakEaseTextSecondary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = state.definition,
                    fontFamily = PlusJakartaSans,
                    fontSize = 14.sp,
                    color = SpeakEaseTextPrimary
                )
            }

            if (state.contextSentence.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Matndan namuna:",
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpeakEaseTextSecondary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF8F9FE),
                    border = BorderStroke(1.dp, SpeakEaseCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "\"${state.contextSentence}\"",
                        fontFamily = PlusJakartaSans,
                        fontSize = 13.sp,
                        fontStyle = FontStyle.Italic,
                        color = SpeakEaseTextSecondary,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SpeakEasePrimaryButton(
                    text = if (state.isSaved) "Lug'atda saqlangan ✓" else "Lug'atga qo'shish 💡",
                    onClick = onToggleSave,
                    icon = if (state.isSaved) Icons.Default.Check else Icons.Default.Bookmark,
                    gradientColors = if (state.isSaved) listOf(SpeakEaseMint, SpeakEaseMint) else listOf(SpeakEasePrimaryGradientStart, SpeakEasePrimaryGradientEnd),
                    modifier = Modifier.weight(1.2f),
                    testTag = "toggle_save_vocab_button"
                )

                SpeakEaseSecondaryButton(
                    text = "Yopish",
                    onClick = onDismiss,
                    modifier = Modifier.weight(0.8f),
                    testTag = "close_word_sheet_button"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentenceAnalysisBottomSheet(
    state: SentenceAnalysisState,
    onDismiss: () -> Unit,
    onPlayAudio: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        modifier = Modifier.testTag("sentence_analysis_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SpeakEaseIconBadge(
                        icon = Icons.Default.AutoAwesome,
                        containerColor = SpeakEaseCoralSoft,
                        tintColor = SpeakEaseCoral,
                        size = 38.dp,
                        iconSize = 20.dp,
                        cornerRadius = 12.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Jumla Tahlili & Izoh ✨",
                        fontFamily = PlusJakartaSans,
                        fontSize = 18.sp,
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

            Spacer(modifier = Modifier.height(16.dp))

            // Original Text Card
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFFF8F9FE),
                border = BorderStroke(1.dp, SpeakEaseCardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Asl jumla:",
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SpeakEaseTextMuted
                        )
                        SpeakEaseIconButton(
                            icon = Icons.AutoMirrored.Filled.VolumeUp,
                            onClick = { onPlayAudio(state.originalText) },
                            containerColor = SpeakEasePrimarySoft,
                            tintColor = SpeakEasePrimary,
                            size = 32.dp,
                            iconSize = 16.dp,
                            contentDescription = "Tinglash"
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "\"${state.originalText}\"",
                        fontFamily = PlusJakartaSans,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SpeakEaseTextPrimary,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = SpeakEasePrimary)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Tarjima va qoidalar tahlil qilinmoqda...",
                            fontFamily = PlusJakartaSans,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = SpeakEaseTextSecondary
                        )
                    }
                }
            } else {
                // Translation
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = SpeakEasePrimarySoft,
                    border = BorderStroke(1.dp, SpeakEasePrimaryBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "O'zbekcha Tarjimasi",
                                fontFamily = PlusJakartaSans,
                                fontSize = 12.sp,
                                color = SpeakEasePrimary,
                                fontWeight = FontWeight.ExtraBold
                            )
                            if (state.isFromAi) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = SpeakEasePrimary
                                ) {
                                    Text(
                                        text = "AI Tahlili",
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = state.translation,
                            fontFamily = PlusJakartaSans,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SpeakEasePrimaryDark,
                            lineHeight = 22.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Grammar & Explanation
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, SpeakEaseCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Grammatika & So'zlar Izohi:",
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.sp,
                            color = SpeakEaseCoral,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.explanation,
                            fontFamily = PlusJakartaSans,
                            fontSize = 14.sp,
                            color = SpeakEaseTextPrimary,
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            SpeakEasePrimaryButton(
                text = "Tushunarli, Rahmat ✨",
                onClick = onDismiss
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderSettingsBottomSheet(
    settings: ReaderSettings,
    onDismiss: () -> Unit,
    onUpdateSettings: (fontSizeSp: Float?, theme: ReadingTheme?, fontFamily: ReadingFontFamily?, lineSpacing: Float?, autoAddOnTap: Boolean?) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SpeakEaseIconBadge(
                        icon = Icons.Default.FormatSize,
                        containerColor = SpeakEasePrimarySoft,
                        tintColor = SpeakEasePrimary,
                        size = 38.dp,
                        iconSize = 20.dp,
                        cornerRadius = 12.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Mutolaa Sozlamalari",
                        fontFamily = PlusJakartaSans,
                        fontSize = 18.sp,
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

            Spacer(modifier = Modifier.height(18.dp))

            // Reading Background Theme
            Text(
                text = "Rang Mavzusi (Fon)",
                fontFamily = PlusJakartaSans,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = SpeakEaseTextPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ReadingTheme.entries.forEach { theme ->
                    val isSelected = settings.readingTheme == theme
                    val (bg, textColor) = when (theme) {
                        ReadingTheme.LIGHT -> Pair(PaperLightBg, Color(0xFF0F172A))
                        ReadingTheme.SEPIA -> Pair(PaperSepiaBg, Color(0xFF3D2F1D))
                        ReadingTheme.DARK -> Pair(PaperDarkBg, Color(0xFFF1F5F9))
                        ReadingTheme.NIGHT -> Pair(PaperNightBg, Color(0xFFFFFFFF))
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(bg)
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) SpeakEasePrimary else Color(0xFFE2E8F0),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable { onUpdateSettings(null, theme, null, null, null) }
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = theme.displayName.split(" ").first(),
                            color = textColor,
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Font Size Slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Shrift Kattaligi",
                    fontFamily = PlusJakartaSans,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = SpeakEaseTextPrimary
                )
                Text(
                    text = "${settings.fontSizeSp.toInt()} sp",
                    fontFamily = PlusJakartaSans,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = SpeakEasePrimary
                )
            }
            Slider(
                value = settings.fontSizeSp,
                onValueChange = { onUpdateSettings(it, null, null, null, null) },
                valueRange = 14f..28f,
                steps = 7,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Font Family Choice
            Text(
                text = "Shrift Turi",
                fontFamily = PlusJakartaSans,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = SpeakEaseTextPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ReadingFontFamily.entries.forEach { font ->
                    val isSelected = settings.fontFamily == font
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) SpeakEasePrimary else Color(0xFFF8F9FE))
                            .border(1.dp, if (isSelected) SpeakEasePrimary else SpeakEaseCardBorder, RoundedCornerShape(14.dp))
                            .clickable { onUpdateSettings(null, null, font, null, null) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = font.displayName,
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                            color = if (isSelected) Color.White else SpeakEaseTextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Auto-Add to Vocabulary Toggle (Core User Request!)
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SpeakEasePrimarySoft,
                border = BorderStroke(1.dp, SpeakEasePrimaryBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "So'zlarni Avtomatik Qo'shish 💡",
                            fontFamily = PlusJakartaSans,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SpeakEasePrimaryDark
                        )
                        Text(
                            text = "Matnda bosilgan har bir so'z darhol lug'atga saqlanadi",
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.sp,
                            color = SpeakEaseTextSecondary
                        )
                    }
                    Switch(
                        checked = settings.autoAddOnTap,
                        onCheckedChange = { onUpdateSettings(null, null, null, null, it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = SpeakEasePrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun AddReadingDialog(
    onDismiss: () -> Unit,
    onSave: (title: String, author: String, category: String, content: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Hikoyalar") }
    var content by remember { mutableStateOf("") }

    val categories = listOf("Hikoyalar", "Shaxsiy Rivojlanish", "Fan va Texnologiya", "Tarix va Madaniyat", "Falsafa")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, SpeakEaseCardBorder),
            modifier = Modifier.padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(22.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Yangi Matn Qo'shish",
                        fontFamily = PlusJakartaSans,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SpeakEaseTextPrimary
                    )
                    SpeakEaseIconButton(
                        icon = Icons.Default.Close,
                        onClick = onDismiss,
                        containerColor = Color(0xFFF1F3FA),
                        tintColor = SpeakEaseTextPrimary,
                        size = 32.dp,
                        iconSize = 16.dp,
                        contentDescription = "Yopish"
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Sarlavha", fontFamily = PlusJakartaSans) },
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = SpeakEasePrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("Muallif", fontFamily = PlusJakartaSans) },
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = SpeakEasePrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Bo'lim:",
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpeakEaseTextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(categories) { cat ->
                        val isSelected = category == cat
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) SpeakEasePrimary else Color(0xFFF8F9FE),
                            border = BorderStroke(1.dp, if (isSelected) SpeakEasePrimary else SpeakEaseCardBorder),
                            modifier = Modifier.clickable { category = cat }
                        ) {
                            Text(
                                text = cat,
                                fontFamily = PlusJakartaSans,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else SpeakEaseTextPrimary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Matn mazmuni", fontFamily = PlusJakartaSans) },
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = SpeakEasePrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                SpeakEasePrimaryButton(
                    text = "Matnni Saqlash",
                    enabled = title.isNotBlank() && content.isNotBlank(),
                    onClick = {
                        onSave(title, author.ifBlank { "Noma'lum" }, category, content)
                    }
                )
            }
        }
    }
}

@Composable
fun AddWordDialog(
    onDismiss: () -> Unit,
    onSave: (word: String, translation: String, phonetic: String, definition: String, example: String) -> Unit
) {
    var word by remember { mutableStateOf("") }
    var translation by remember { mutableStateOf("") }
    var phonetic by remember { mutableStateOf("") }
    var definition by remember { mutableStateOf("") }
    var example by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, SpeakEaseCardBorder),
            modifier = Modifier.padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(22.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Yangi So'z Qo'shish",
                        fontFamily = PlusJakartaSans,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SpeakEaseTextPrimary
                    )
                    SpeakEaseIconButton(
                        icon = Icons.Default.Close,
                        onClick = onDismiss,
                        containerColor = Color(0xFFF1F3FA),
                        tintColor = SpeakEaseTextPrimary,
                        size = 32.dp,
                        iconSize = 16.dp,
                        contentDescription = "Yopish"
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = word,
                    onValueChange = { word = it },
                    label = { Text("Inglizcha so'z", fontFamily = PlusJakartaSans) },
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = SpeakEasePrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = translation,
                    onValueChange = { translation = it },
                    label = { Text("O'zbekcha tarjimasi", fontFamily = PlusJakartaSans) },
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = SpeakEasePrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = phonetic,
                    onValueChange = { phonetic = it },
                    label = { Text("Transkripsiya (masalan: /ˌɛl.əˈkwənt/)", fontFamily = PlusJakartaSans) },
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = SpeakEasePrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = definition,
                    onValueChange = { definition = it },
                    label = { Text("Ta'rif (Ingliz tilida)", fontFamily = PlusJakartaSans) },
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = SpeakEasePrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = example,
                    onValueChange = { example = it },
                    label = { Text("Misol jumla", fontFamily = PlusJakartaSans) },
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = SpeakEasePrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))

                SpeakEasePrimaryButton(
                    text = "Lug'atga Saqlash",
                    enabled = word.isNotBlank() && translation.isNotBlank(),
                    onClick = {
                        onSave(word.trim(), translation.trim(), phonetic.trim(), definition.trim(), example.trim())
                    }
                )
            }
        }
    }
}
