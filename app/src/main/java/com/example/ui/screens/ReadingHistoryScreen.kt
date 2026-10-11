package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ReadingHistoryEntry
import com.example.ui.components.SpeakEaseIconBadge
import com.example.ui.components.SpeakEaseIconButton
import com.example.ui.theme.PlusJakartaSans
import com.example.ui.theme.SpeakEaseCardBorder
import com.example.ui.theme.SpeakEaseMint
import com.example.ui.theme.SpeakEaseMintBorder
import com.example.ui.theme.SpeakEaseMintSoft
import com.example.ui.theme.SpeakEasePrimary
import com.example.ui.theme.SpeakEasePrimaryBorder
import com.example.ui.theme.SpeakEasePrimaryDark
import com.example.ui.theme.SpeakEasePrimarySoft
import com.example.ui.theme.SpeakEaseTextMuted
import com.example.ui.theme.SpeakEaseTextPrimary
import com.example.ui.theme.SpeakEaseTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingHistoryScreen(
    history: List<ReadingHistoryEntry>,
    onBack: () -> Unit,
    onOpenReading: (Long) -> Unit,
    onClearHistory: () -> Unit
) {
    BackHandler { onBack() }

    val totalSessions = history.size
    val completedSessions = remember(history) { history.count { it.progressPercentage >= 95 } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mutolaa Tarixi",
                        fontFamily = PlusJakartaSans,
                        fontSize = 18.sp,
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
                        testTag = "history_back_button",
                        modifier = Modifier.padding(start = 12.dp)
                    )
                },
                actions = {
                    if (history.isNotEmpty()) {
                        SpeakEaseIconButton(
                            icon = Icons.Default.DeleteSweep,
                            onClick = onClearHistory,
                            containerColor = Color(0xFFF1F3FA),
                            tintColor = SpeakEaseTextMuted,
                            size = 40.dp,
                            iconSize = 20.dp,
                            contentDescription = "Tarixni tozalash",
                            modifier = Modifier.padding(end = 12.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF8F9FE))
            )
        },
        containerColor = Color(0xFFF8F9FE)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Stats Header
            item {
                Card(
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, SpeakEaseCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(SpeakEasePrimarySoft.copy(alpha = 0.5f), Color.White)
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                SpeakEaseIconBadge(
                                    icon = Icons.Default.History,
                                    containerColor = Color.White,
                                    tintColor = SpeakEasePrimary,
                                    size = 44.dp,
                                    iconSize = 22.dp,
                                    cornerRadius = 14.dp
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Mutolaa Kundaligi 📖",
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = SpeakEaseTextPrimary
                                    )
                                    Text(
                                        text = "O'qilgan sahifalar va to'xtagan joylar",
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 12.sp,
                                        color = SpeakEaseTextSecondary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = SpeakEasePrimarySoft,
                                    border = BorderStroke(1.dp, SpeakEasePrimaryBorder),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            "Jami sessiyalar",
                                            fontFamily = PlusJakartaSans,
                                            fontSize = 11.sp,
                                            color = SpeakEasePrimary,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            "$totalSessions marta",
                                            fontFamily = PlusJakartaSans,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = SpeakEasePrimary
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = SpeakEaseMintSoft,
                                    border = BorderStroke(1.dp, SpeakEaseMintBorder),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            "Yakunlangan",
                                            fontFamily = PlusJakartaSans,
                                            fontSize = 11.sp,
                                            color = SpeakEaseMint,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            "$completedSessions ta matn",
                                            fontFamily = PlusJakartaSans,
                                            fontSize = 16.sp,
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

            if (history.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SpeakEaseIconBadge(
                            icon = Icons.Default.History,
                            containerColor = SpeakEasePrimarySoft,
                            tintColor = SpeakEasePrimary,
                            size = 64.dp,
                            iconSize = 32.dp,
                            cornerRadius = 22.dp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Tarix hali bo'sh",
                            fontFamily = PlusJakartaSans,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SpeakEaseTextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Matnlarni o'qishni boshlang, bu yerda o'qilgan sahifalaringiz avtomatik saqlanadi!",
                            fontFamily = PlusJakartaSans,
                            fontSize = 13.sp,
                            color = SpeakEaseTextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }

            items(history, key = { it.id }) { entry ->
                SpeakEaseHistoryItemCard(
                    entry = entry,
                    onClick = { onOpenReading(entry.readingId) }
                )
            }
        }
    }
}

@Composable
fun SpeakEaseHistoryItemCard(
    entry: ReadingHistoryEntry,
    onClick: () -> Unit
) {
    val formattedDate = remember(entry.timestamp) {
        val sdf = SimpleDateFormat("dd MMMM, HH:mm", Locale.getDefault())
        sdf.format(Date(entry.timestamp))
    }

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, SpeakEaseCardBorder),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formattedDate,
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    color = SpeakEaseTextMuted,
                    fontWeight = FontWeight.Medium
                )

                if (entry.progressPercentage >= 95) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SpeakEaseMintSoft,
                        border = BorderStroke(1.dp, SpeakEaseMintBorder)
                    ) {
                        Text(
                            text = "Tugallangan",
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SpeakEaseMint,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                } else {
                    Text(
                        text = "${entry.progressPercentage}%",
                        fontFamily = PlusJakartaSans,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SpeakEasePrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = entry.readingTitle,
                fontFamily = PlusJakartaSans,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = SpeakEaseTextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (entry.timeSpentSeconds > 0) "${entry.timeSpentSeconds / 60 + 1} daqiqa mutolaa qilindi" else "Mutolaa saqlangan",
                fontFamily = PlusJakartaSans,
                fontSize = 12.sp,
                color = SpeakEaseTextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { entry.progressPercentage / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (entry.progressPercentage >= 95) SpeakEaseMint else SpeakEasePrimary,
                trackColor = Color(0xFFF1F3FA)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(SpeakEasePrimarySoft)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Davom ettirish",
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SpeakEasePrimary
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = SpeakEasePrimary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
