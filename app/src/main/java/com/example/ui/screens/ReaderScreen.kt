package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ReadingFontFamily
import com.example.data.model.ReadingItem
import com.example.data.model.ReadingTheme
import com.example.ui.ReaderSettings
import com.example.ui.components.ReaderSettingsBottomSheet
import com.example.ui.components.SpeakEaseAudioBadge
import com.example.ui.components.SpeakEaseIconBadge
import com.example.ui.components.SpeakEaseIconButton
import com.example.ui.components.SpeakEasePrimaryButton
import com.example.ui.components.SpeakEaseSecondaryButton
import com.example.ui.theme.LoraFontFamily
import com.example.ui.theme.PaperDarkBg
import com.example.ui.theme.PaperDarkCard
import com.example.ui.theme.PaperDarkText
import com.example.ui.theme.PaperLightBg
import com.example.ui.theme.PaperLightCard
import com.example.ui.theme.PaperLightText
import com.example.ui.theme.PaperNightBg
import com.example.ui.theme.PaperNightCard
import com.example.ui.theme.PaperNightText
import com.example.ui.theme.PaperSepiaBg
import com.example.ui.theme.PaperSepiaCard
import com.example.ui.theme.PaperSepiaText
import com.example.ui.theme.PlusJakartaSans
import com.example.ui.theme.SpeakEaseAmber
import com.example.ui.theme.SpeakEaseAmberBorder
import com.example.ui.theme.SpeakEaseAmberSoft
import com.example.ui.theme.SpeakEaseCardBorder
import com.example.ui.theme.SpeakEaseCoral
import com.example.ui.theme.SpeakEaseMint
import com.example.ui.theme.SpeakEaseMintBorder
import com.example.ui.theme.SpeakEaseMintSoft
import com.example.ui.theme.SpeakEasePrimary
import com.example.ui.theme.SpeakEasePrimaryBorder
import com.example.ui.theme.SpeakEasePrimarySoft

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ReaderScreen(
    readingItem: ReadingItem?,
    settings: ReaderSettings,
    onBack: () -> Unit,
    onWordTapped: (word: String, sentence: String) -> Unit,
    onAnalyzeSentence: (sentence: String) -> Unit,
    onUpdateProgress: (progress: Float, paragraphIndex: Int) -> Unit,
    onUpdateSettings: (fontSizeSp: Float?, theme: ReadingTheme?, fontFamily: ReadingFontFamily?, lineSpacing: Float?, autoAddOnTap: Boolean?) -> Unit,
    onPlayAudio: (String) -> Unit
) {
    BackHandler { onBack() }

    var showSettingsSheet by remember { mutableStateOf(false) }

    // Colors according to user reading theme
    val (bgColor, textColor, cardBg) = when (settings.readingTheme) {
        ReadingTheme.LIGHT -> Triple(PaperLightBg, PaperLightText, PaperLightCard)
        ReadingTheme.SEPIA -> Triple(PaperSepiaBg, PaperSepiaText, PaperSepiaCard)
        ReadingTheme.DARK -> Triple(PaperDarkBg, PaperDarkText, PaperDarkCard)
        ReadingTheme.NIGHT -> Triple(PaperNightBg, PaperNightText, PaperNightCard)
    }

    val selectedFontFamily = when (settings.fontFamily) {
        ReadingFontFamily.SERIF -> LoraFontFamily
        ReadingFontFamily.SANS_SERIF -> PlusJakartaSans
        ReadingFontFamily.MONOSPACE -> FontFamily.Monospace
    }

    if (readingItem == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            Text("Matn topilmadi", color = textColor, fontFamily = PlusJakartaSans)
        }
        return
    }

    // Split content into clean paragraphs
    val paragraphs = remember(readingItem.content) {
        readingItem.content.split(Regex("\n\n+"))
            .map { it.trim() }
            .filter { it.isNotBlank() }
    }

    val listState = rememberLazyListState()

    // Restore previous read paragraph
    LaunchedEffect(readingItem.id) {
        if (readingItem.lastReadParagraph > 0 && readingItem.lastReadParagraph < paragraphs.size) {
            listState.scrollToItem(readingItem.lastReadParagraph)
        }
    }

    // Monitor progress
    LaunchedEffect(listState) {
        snapshotFlow {
            val totalItems = paragraphs.size
            if (totalItems <= 1) 1f
            else {
                val firstVisible = listState.firstVisibleItemIndex
                (firstVisible.toFloat() / (totalItems - 1)).coerceIn(0f, 1f)
            }
        }.collect { progress ->
            onUpdateProgress(progress, listState.firstVisibleItemIndex)
        }
    }

    val currentProgress = remember {
        derivedStateOf {
            if (paragraphs.size <= 1) 1f
            else (listState.firstVisibleItemIndex.toFloat() / (paragraphs.size - 1)).coerceIn(0f, 1f)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = readingItem.title,
                            maxLines = 1,
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = textColor
                        )
                        Text(
                            text = "${(currentProgress.value * 100).toInt()}% mutolaa qilindi • ${readingItem.category}",
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.sp,
                            color = textColor.copy(alpha = 0.7f)
                        )
                    }
                },
                navigationIcon = {
                    SpeakEaseIconButton(
                        icon = Icons.AutoMirrored.Filled.ArrowBack,
                        onClick = onBack,
                        containerColor = textColor.copy(alpha = 0.08f),
                        tintColor = textColor,
                        size = 40.dp,
                        iconSize = 20.dp,
                        contentDescription = "Orqaga",
                        testTag = "reader_back_button",
                        modifier = Modifier.padding(start = 12.dp)
                    )
                },
                actions = {
                    // Audio Pronounce Title in squircle
                    SpeakEaseIconButton(
                        icon = Icons.AutoMirrored.Filled.VolumeUp,
                        onClick = { onPlayAudio(readingItem.title) },
                        containerColor = textColor.copy(alpha = 0.08f),
                        tintColor = textColor,
                        size = 40.dp,
                        iconSize = 20.dp,
                        contentDescription = "Sarlavhani tinglash",
                        testTag = "reader_audio_button"
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Reading Style Settings in squircle
                    SpeakEaseIconButton(
                        icon = Icons.Default.FormatSize,
                        onClick = { showSettingsSheet = true },
                        containerColor = textColor.copy(alpha = 0.08f),
                        tintColor = textColor,
                        size = 40.dp,
                        iconSize = 20.dp,
                        contentDescription = "Sozlamalar",
                        testTag = "reader_settings_button",
                        modifier = Modifier.padding(end = 12.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = bgColor,
                    titleContentColor = textColor
                )
            )
        },
        bottomBar = {
            // Reading Progress Indicator at bottom
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(bgColor)
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                LinearProgressIndicator(
                    progress = { currentProgress.value },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp),
                    color = SpeakEasePrimary,
                    trackColor = textColor.copy(alpha = 0.1f)
                )
            }
        },
        containerColor = bgColor
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Header Article Meta Card
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Card(
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        border = BorderStroke(1.dp, textColor.copy(alpha = 0.08f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = readingItem.title,
                                fontSize = (settings.fontSizeSp + 4).sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = textColor,
                                fontFamily = selectedFontFamily
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Muallif: ${readingItem.author}",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 12.sp,
                                    color = textColor.copy(alpha = 0.75f)
                                )
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = SpeakEasePrimarySoft,
                                    border = BorderStroke(1.dp, SpeakEasePrimaryBorder)
                                ) {
                                    Text(
                                        text = "${readingItem.estimatedReadMinutes} daqiqalik mutolaa",
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SpeakEasePrimary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // SpeakEase Interactive Tip Bar
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = SpeakEaseAmberSoft,
                                border = BorderStroke(1.dp, SpeakEaseAmberBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    SpeakEaseIconBadge(
                                        icon = Icons.Default.AutoAwesome,
                                        containerColor = Color.White,
                                        tintColor = SpeakEaseAmber,
                                        size = 32.dp,
                                        iconSize = 16.dp,
                                        cornerRadius = 8.dp
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "Noma'lum so'zni bosing (lug'atga avtomatik qo'shiladi) yoki jumlani tarjima qiling!",
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF78350F),
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Interactive Paragraph Cards
                itemsIndexed(paragraphs) { pIndex, paragraph ->
                    ParagraphCard(
                        paragraphIndex = pIndex + 1,
                        paragraphText = paragraph,
                        textColor = textColor,
                        cardBg = cardBg,
                        fontSizeSp = settings.fontSizeSp,
                        fontFamily = selectedFontFamily,
                        lineSpacing = settings.lineSpacingMultiplier,
                        onWordClick = { word, sentence ->
                            onWordTapped(word, sentence)
                        },
                        onAnalyzeSentence = { sentence ->
                            onAnalyzeSentence(sentence)
                        },
                        onPlayAudio = onPlayAudio
                    )
                }

                // Completed Reading Celebration Card
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        border = BorderStroke(1.dp, SpeakEaseMintBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 36.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(22.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            SpeakEaseIconBadge(
                                icon = Icons.Default.Check,
                                containerColor = SpeakEaseMintSoft,
                                tintColor = SpeakEaseMint,
                                borderColor = SpeakEaseMintBorder,
                                size = 52.dp,
                                iconSize = 26.dp,
                                cornerRadius = 18.dp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Tabriklaymiz! Mutolaa yakunlandi 🎉",
                                fontFamily = PlusJakartaSans,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = textColor
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "O'rganilgan so'zlarni mashq qiling yoki o'z fikrlaringizni eslatma sifatida yozib qoldiring!",
                                fontFamily = PlusJakartaSans,
                                fontSize = 13.sp,
                                color = textColor.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }

    if (showSettingsSheet) {
        ReaderSettingsBottomSheet(
            settings = settings,
            onDismiss = { showSettingsSheet = false },
            onUpdateSettings = onUpdateSettings
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ParagraphCard(
    paragraphIndex: Int,
    paragraphText: String,
    textColor: Color,
    cardBg: Color,
    fontSizeSp: Float,
    fontFamily: FontFamily,
    lineSpacing: Float,
    onWordClick: (word: String, sentence: String) -> Unit,
    onAnalyzeSentence: (sentence: String) -> Unit,
    onPlayAudio: (String) -> Unit
) {
    val sentences = remember(paragraphText) {
        paragraphText.split(Regex("(?<=[.!?])\\s+"))
            .filter { it.isNotBlank() }
    }

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = BorderStroke(1.dp, textColor.copy(alpha = 0.08f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            sentences.forEachIndexed { sIndex, sentence ->
                val words = remember(sentence) {
                    sentence.split(Regex("\\s+")).filter { it.isNotBlank() }
                }

                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        words.forEach { rawWord ->
                            val cleanWord = rawWord.replace(Regex("[^a-zA-Z]"), "")

                            Text(
                                text = rawWord + " ",
                                fontSize = fontSizeSp.sp,
                                fontFamily = fontFamily,
                                lineHeight = (fontSizeSp * lineSpacing).sp,
                                color = textColor,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable {
                                        if (cleanWord.isNotBlank()) {
                                            onWordClick(cleanWord, sentence)
                                        }
                                    }
                                    .padding(horizontal = 2.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // SpeakEase Sentence Action Pill Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 8.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Pronounce sentence squircle button
                        SpeakEaseIconButton(
                            icon = Icons.AutoMirrored.Filled.VolumeUp,
                            onClick = { onPlayAudio(sentence) },
                            containerColor = SpeakEasePrimarySoft,
                            tintColor = SpeakEasePrimary,
                            borderColor = SpeakEasePrimaryBorder,
                            size = 32.dp,
                            iconSize = 16.dp,
                            contentDescription = "Jumlani tinglash"
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        // Translate & Explain Action Pill
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SpeakEasePrimarySoft,
                            border = BorderStroke(1.dp, SpeakEasePrimaryBorder),
                            modifier = Modifier.clickable { onAnalyzeSentence(sentence) }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Translate,
                                    contentDescription = null,
                                    tint = SpeakEasePrimary,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "Tarjima & Tushuntirish ✨",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = SpeakEasePrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
