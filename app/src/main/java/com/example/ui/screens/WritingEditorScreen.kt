package com.example.ui.screens

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.VocabularyWord
import com.example.data.model.WritingNote
import com.example.ui.components.SpeakEaseIconBadge
import com.example.ui.components.SpeakEaseIconButton
import com.example.ui.components.SpeakEasePrimaryButton
import com.example.ui.theme.PlusJakartaSans
import com.example.ui.theme.SpeakEaseCardBorder
import com.example.ui.theme.SpeakEaseCoral
import com.example.ui.theme.SpeakEaseCoralBorder
import com.example.ui.theme.SpeakEaseCoralSoft
import com.example.ui.theme.SpeakEaseMint
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
fun WritingEditorScreen(
    initialNote: WritingNote?,
    vocabularyList: List<VocabularyWord>,
    onBack: () -> Unit,
    onSave: (id: Long, title: String, content: String, category: String, isPinned: Boolean) -> Unit
) {
    BackHandler { onBack() }

    var title by remember { mutableStateOf(initialNote?.title ?: "") }
    var content by remember { mutableStateOf(initialNote?.content ?: "") }
    var category by remember { mutableStateOf(initialNote?.category ?: "Eslatmalar") }
    var isPinned by remember { mutableStateOf(initialNote?.isPinned ?: false) }
    val context = LocalContext.current

    val categories = listOf("Eslatmalar", "Insho", "Kundalik", "Lug'at mashqi", "Hikoya", "Fikrlar")

    val wordCount = remember(content) {
        content.split(Regex("\\s+")).count { it.isNotBlank() }
    }
    val charCount = remember(content) { content.length }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (initialNote != null) "Tahrirlash" else "Yangi Yozuv",
                        fontFamily = PlusJakartaSans,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SpeakEaseTextPrimary
                    )
                },
                navigationIcon = {
                    SpeakEaseIconButton(
                        icon = Icons.AutoMirrored.Filled.ArrowBack,
                        onClick = onBack,
                        containerColor = Color(0xFFF1F3FA),
                        tintColor = SpeakEaseTextPrimary,
                        size = 40.dp,
                        iconSize = 20.dp,
                        contentDescription = "Orqaga",
                        testTag = "editor_back_button",
                        modifier = Modifier.padding(start = 12.dp)
                    )
                },
                actions = {
                    // Share Button in squircle
                    SpeakEaseIconButton(
                        icon = Icons.Default.Share,
                        onClick = {
                            if (content.isNotBlank()) {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_SUBJECT, title.ifBlank { "ReadScribe Yozuvi" })
                                    putExtra(Intent.EXTRA_TEXT, "${title.ifBlank { "" }}\n\n$content")
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Yozuvni ulashish"))
                            }
                        },
                        containerColor = Color(0xFFF1F3FA),
                        tintColor = if (content.isNotBlank()) SpeakEasePrimary else SpeakEaseTextMuted,
                        size = 40.dp,
                        iconSize = 20.dp,
                        contentDescription = "Ulashish",
                        testTag = "editor_share_button"
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Pin Button in squircle
                    SpeakEaseIconButton(
                        icon = if (isPinned) Icons.Default.PushPin else Icons.Outlined.PushPin,
                        onClick = { isPinned = !isPinned },
                        containerColor = if (isPinned) SpeakEaseCoralSoft else Color(0xFFF1F3FA),
                        tintColor = if (isPinned) SpeakEaseCoral else SpeakEaseTextMuted,
                        size = 40.dp,
                        iconSize = 20.dp,
                        contentDescription = "Biriktirish",
                        testTag = "editor_pin_button"
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Save Button
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(SpeakEasePrimaryGradientStart, SpeakEasePrimaryGradientEnd)
                                )
                            )
                            .clickable {
                                onSave(initialNote?.id ?: 0L, title, content, category, isPinned)
                                onBack()
                            }
                            .padding(horizontal = 14.dp, vertical = 9.dp)
                            .testTag("editor_save_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Saqlash",
                                fontFamily = PlusJakartaSans,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp,
                                color = Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF8F9FE))
            )
        },
        containerColor = Color(0xFFF8F9FE)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Category selector pills
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    val isSelected = category == cat
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) SpeakEasePrimary else Color.White)
                            .border(1.dp, if (isSelected) SpeakEasePrimary else SpeakEaseCardBorder, RoundedCornerShape(14.dp))
                            .clickable { category = cat }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = cat,
                            fontFamily = PlusJakartaSans,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else SpeakEaseTextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Title input
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = {
                    Text(
                        "Sarlavha...",
                        fontFamily = PlusJakartaSans,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = SpeakEaseTextMuted
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = SpeakEasePrimary,
                    unfocusedIndicatorColor = SpeakEaseCardBorder
                ),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontFamily = PlusJakartaSans,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = SpeakEaseTextPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("editor_title_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Helpful Vocabulary insertion bar (SpeakEase feature: use learned words in writing!)
            if (vocabularyList.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SpeakEasePrimarySoft,
                    border = BorderStroke(1.dp, SpeakEasePrimaryBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = null,
                                tint = SpeakEasePrimary,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Lug'atdan so'z qo'shish (bosib matnga kiriting):",
                                fontFamily = PlusJakartaSans,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SpeakEasePrimaryDark
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(vocabularyList.take(8)) { vWord ->
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color.White,
                                    border = BorderStroke(1.dp, SpeakEasePrimaryBorder),
                                    modifier = Modifier.clickable {
                                        content = if (content.isBlank()) vWord.word else "$content ${vWord.word}"
                                    }
                                ) {
                                    Text(
                                        text = "+ ${vWord.word}",
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SpeakEasePrimary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            // Quick Formatting Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .border(1.dp, SpeakEaseCardBorder, RoundedCornerShape(14.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = {
                    content = "$content\n• "
                }, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.AutoMirrored.Filled.FormatListBulleted, contentDescription = "Ro'yxat", tint = SpeakEasePrimary)
                }

                IconButton(onClick = {
                    content = "$content **muhim** "
                }, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.Default.FormatBold, contentDescription = "Qalin", tint = SpeakEasePrimary)
                }

                IconButton(onClick = {
                    content = "$content\n> \"Iqtibos\" "
                }, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.Default.FormatQuote, contentDescription = "Iqtibos", tint = SpeakEasePrimary)
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "$wordCount so'z • $charCount belgi",
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpeakEaseTextMuted,
                    modifier = Modifier.padding(end = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Content text area
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                placeholder = {
                    Text(
                        "Fikrlaringiz, yangi so'zlardan foydalanib insho yoki eslatma yozing...",
                        fontFamily = PlusJakartaSans,
                        fontSize = 15.sp,
                        color = SpeakEaseTextMuted,
                        lineHeight = 22.sp
                    )
                },
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = SpeakEasePrimary,
                    unfocusedIndicatorColor = SpeakEaseCardBorder
                ),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontFamily = PlusJakartaSans,
                    fontSize = 15.sp,
                    lineHeight = 24.sp,
                    color = SpeakEaseTextPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp)
                    .testTag("editor_content_input")
            )
        }
    }
}
