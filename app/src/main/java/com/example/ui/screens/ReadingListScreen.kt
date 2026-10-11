package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ReadingItem
import com.example.ui.components.AddReadingDialog
import com.example.ui.components.SpeakEaseArrowActionBadge
import com.example.ui.components.SpeakEaseCategoryPill
import com.example.ui.components.SpeakEaseFeatureGridCard
import com.example.ui.components.SpeakEaseHeroActionButton
import com.example.ui.components.SpeakEaseIconBadge
import com.example.ui.components.SpeakEaseIconButton
import com.example.ui.components.SpeakEasePlayCircleBadge
import com.example.ui.components.SpeakEaseStreakBadge
import com.example.ui.components.SpeakEaseXpBadge
import com.example.ui.theme.PlusJakartaSans
import com.example.ui.theme.SpeakEaseAmber
import com.example.ui.theme.SpeakEaseAmberBorder
import com.example.ui.theme.SpeakEaseAmberSoft
import com.example.ui.theme.SpeakEaseCardBorder
import com.example.ui.theme.SpeakEaseCoral
import com.example.ui.theme.SpeakEaseCoralBorder
import com.example.ui.theme.SpeakEaseCoralSoft
import com.example.ui.theme.SpeakEaseCream
import com.example.ui.theme.SpeakEaseCreamBorder
import com.example.ui.theme.SpeakEaseMint
import com.example.ui.theme.SpeakEaseMintBorder
import com.example.ui.theme.SpeakEaseMintSoft
import com.example.ui.theme.SpeakEasePrimary
import com.example.ui.theme.SpeakEasePrimaryBorder
import com.example.ui.theme.SpeakEasePrimaryDark
import com.example.ui.theme.SpeakEasePrimaryGradientEnd
import com.example.ui.theme.SpeakEasePrimaryGradientStart
import com.example.ui.theme.SpeakEasePrimarySoft
import com.example.ui.theme.SpeakEaseSky
import com.example.ui.theme.SpeakEaseSkyBorder
import com.example.ui.theme.SpeakEaseSkySoft
import com.example.ui.theme.SpeakEaseTextMuted
import com.example.ui.theme.SpeakEaseTextPrimary
import com.example.ui.theme.SpeakEaseTextSecondary

@Composable
fun ReadingListScreen(
    readings: List<ReadingItem>,
    recentReadings: List<ReadingItem>,
    vocabCount: Int,
    onOpenReading: (Long) -> Unit,
    onOpenHistory: () -> Unit,
    onAddCustomReading: (title: String, author: String, category: String, content: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("Barchasi") }
    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }

    val categories = remember(readings) {
        listOf("Barchasi") + readings.map { it.category }.distinct()
    }

    val filteredReadings = remember(readings, selectedCategory, searchQuery) {
        readings.filter { item ->
            val matchesCategory = selectedCategory == "Barchasi" || item.category == selectedCategory
            val matchesSearch = searchQuery.isBlank() ||
                    item.title.contains(searchQuery, ignoreCase = true) ||
                    item.author.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    val lastActiveReading = recentReadings.firstOrNull() ?: readings.firstOrNull()

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // =========================================================================
            // 1. Dribbble Top Header: Avatar + "Hi 👋 Alice Johnson" + Streak + History
            // =========================================================================
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp, vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Circular Student Avatar with gold border ring
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, SpeakEaseAmber, CircleShape)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_avatar_student_1791736765129),
                                    contentDescription = "Profil",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Salom 👋",
                                        fontFamily = PlusJakartaSans,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 13.sp,
                                        color = SpeakEaseTextSecondary
                                    )
                                }
                                Text(
                                    text = "Alice Johnson",
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 18.sp,
                                    color = SpeakEaseTextPrimary
                                )
                            }
                        }

                        // Right Controls: Streak Fire Badge + Notification/History circle
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SpeakEaseStreakBadge(streakDays = 5)

                            SpeakEaseIconButton(
                                icon = Icons.Default.History,
                                onClick = onOpenHistory,
                                containerColor = Color.White,
                                tintColor = SpeakEaseTextPrimary,
                                contentDescription = "Tarix",
                                testTag = "history_icon_button"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Signature Large Dribbble Headline
                    Text(
                        text = "Your Smart AI\nLanguage Coach",
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 26.sp,
                        lineHeight = 32.sp,
                        color = SpeakEaseTextPrimary
                    )
                }
            }

            // =========================================================================
            // 2. Search Bar (Pill shape with Filter icon like Image 1)
            // =========================================================================
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(
                                text = "Matn yoki mavzuni qidirish...",
                                fontFamily = PlusJakartaSans,
                                fontSize = 14.sp,
                                color = SpeakEaseTextMuted
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Qidirish",
                                tint = SpeakEasePrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(SpeakEasePrimarySoft),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Tune,
                                    contentDescription = "Filtr",
                                    tint = SpeakEasePrimary,
                                    modifier = Modifier.size(17.dp)
                                )
                            }
                        },
                        shape = RoundedCornerShape(26.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = SpeakEasePrimary,
                            unfocusedBorderColor = SpeakEaseCardBorder
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .shadow(2.dp, RoundedCornerShape(26.dp), spotColor = Color(0x0E000000))
                    )
                }
            }

            // =========================================================================
            // 3. Category Filter Chips (Active purple pill with white text & capsules)
            // =========================================================================
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 22.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { cat ->
                        val count = if (cat == "Barchasi") readings.size
                        else readings.count { it.category == cat }

                        SpeakEaseCategoryPill(
                            label = cat,
                            count = count,
                            isSelected = selectedCategory == cat,
                            onClick = { selectedCategory = cat }
                        )
                    }
                }
            }

            // =========================================================================
            // 4. The 2x2 Feature Cards Grid (Exact replica of Image 2!)
            // =========================================================================
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Card 1: Reading
                        SpeakEaseFeatureGridCard(
                            title = "Reading",
                            progressText = "65% completed",
                            progressFraction = 0.65f,
                            icon = Icons.Default.AutoStories,
                            iconBgColor = SpeakEasePrimarySoft,
                            iconTintColor = SpeakEasePrimary,
                            onClick = { lastActiveReading?.let { onOpenReading(it.id) } },
                            modifier = Modifier.weight(1f)
                        )

                        // Card 2: Listening
                        SpeakEaseFeatureGridCard(
                            title = "Listening",
                            progressText = "45% completed",
                            progressFraction = 0.45f,
                            icon = Icons.Default.Headphones,
                            iconBgColor = SpeakEaseSkySoft,
                            iconTintColor = SpeakEaseSky,
                            onClick = { lastActiveReading?.let { onOpenReading(it.id) } },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Card 3: Vocabulary
                        SpeakEaseFeatureGridCard(
                            title = "Speaking",
                            progressText = "${vocabCount} ta so'z",
                            progressFraction = 0.70f,
                            icon = Icons.Default.Mic,
                            iconBgColor = SpeakEaseCoralSoft,
                            iconTintColor = SpeakEaseCoral,
                            onClick = { lastActiveReading?.let { onOpenReading(it.id) } },
                            modifier = Modifier.weight(1f)
                        )

                        // Card 4: Conversation
                        SpeakEaseFeatureGridCard(
                            title = "Conversation",
                            progressText = "80% completed",
                            progressFraction = 0.80f,
                            icon = Icons.Default.EditNote,
                            iconBgColor = SpeakEaseMintSoft,
                            iconTintColor = SpeakEaseMint,
                            onClick = { lastActiveReading?.let { onOpenReading(it.id) } },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // =========================================================================
            // 5. Featured 3D AI Coach Hero Card (Using the new 3D Bot generated image)
            // =========================================================================
            item {
                Surface(
                    shape = RoundedCornerShape(28.dp),
                    color = SpeakEasePrimarySoft,
                    border = BorderStroke(1.dp, SpeakEasePrimaryBorder),
                    shadowElevation = 3.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color.White,
                                    border = BorderStroke(1.dp, SpeakEasePrimaryBorder)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = null,
                                            tint = SpeakEasePrimary,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Smart Bot Powered",
                                            fontFamily = PlusJakartaSans,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = SpeakEasePrimary
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = "Smart Bot Powers Your Lessons",
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 19.sp,
                                    lineHeight = 24.sp,
                                    color = SpeakEaseTextPrimary
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "So'zni bosing - talaffuzini tinglang va sun'iy intellekt tahlilini ko'ring.",
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp,
                                    color = SpeakEaseTextSecondary
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            // 3D Cute Robot Image
                            Box(
                                modifier = Modifier
                                    .size(110.dp)
                                    .clip(RoundedCornerShape(22.dp))
                                    .background(Color.White)
                                    .border(1.5.dp, SpeakEasePrimaryBorder, RoundedCornerShape(22.dp))
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_ai_coach_hero_1791736737351),
                                    contentDescription = "AI Tutor Bot",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Signature Dribbble Split Pill Button: "Start Learning" + Purple Circular Arrow
                        SpeakEaseHeroActionButton(
                            label = "Start Learning",
                            onClick = { lastActiveReading?.let { onOpenReading(it.id) } },
                            containerColor = Color.White,
                            textColor = SpeakEaseTextPrimary,
                            actionButtonColor = SpeakEasePrimary
                        )
                    }
                }
            }

            // =========================================================================
            // 6. "Your Progress" Section (Like Image 1 Customer journey map)
            // =========================================================================
            if (lastActiveReading != null) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 22.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Your Progress",
                                fontFamily = PlusJakartaSans,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = SpeakEaseTextPrimary
                            )

                            Text(
                                text = "Barchasi",
                                fontFamily = PlusJakartaSans,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = SpeakEasePrimary,
                                modifier = Modifier.clickable { onOpenHistory() }
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Mint Pastel Progress Card (from Image 1 right screen)
                        Surface(
                            shape = RoundedCornerShape(24.dp),
                            color = SpeakEaseMintSoft,
                            border = BorderStroke(1.dp, SpeakEaseMintBorder),
                            shadowElevation = 2.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOpenReading(lastActiveReading.id) }
                        ) {
                            Column(
                                modifier = Modifier.padding(18.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    SpeakEaseIconBadge(
                                        icon = Icons.Default.AutoStories,
                                        containerColor = Color.White,
                                        tintColor = SpeakEaseMint,
                                        borderColor = SpeakEaseMintBorder,
                                        size = 42.dp,
                                        iconSize = 20.dp
                                    )

                                    SpeakEaseArrowActionBadge(
                                        onClick = { onOpenReading(lastActiveReading.id) },
                                        size = 32.dp,
                                        containerColor = Color.White
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Text(
                                    text = lastActiveReading.title,
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 17.sp,
                                    color = SpeakEaseTextPrimary
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "${lastActiveReading.author} • ${lastActiveReading.estimatedReadMinutes} daqiqa",
                                    fontFamily = PlusJakartaSans,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp,
                                    color = SpeakEaseTextSecondary
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Progress",
                                        fontFamily = PlusJakartaSans,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        color = SpeakEaseTextSecondary
                                    )
                                    Text(
                                        text = "${(lastActiveReading.scrollProgress * 100).toInt()}%",
                                        fontFamily = PlusJakartaSans,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 13.sp,
                                        color = SpeakEaseMint
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                LinearProgressIndicator(
                                    progress = { lastActiveReading.scrollProgress.coerceIn(0.1f, 1f) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(7.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    color = SpeakEaseMint,
                                    trackColor = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // =========================================================================
            // 7. Library / Articles List
            // =========================================================================
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Kurslar va Matnlar",
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = SpeakEaseTextPrimary
                    )

                    Text(
                        text = "${filteredReadings.size} ta matn",
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = SpeakEaseTextSecondary
                    )
                }
            }

            items(filteredReadings, key = { it.id }) { reading ->
                SpeakEaseReadingItemCard(
                    reading = reading,
                    onOpen = { onOpenReading(reading.id) },
                    modifier = Modifier.padding(horizontal = 22.dp)
                )
            }
        }

        // Floating Action Button to add custom reading/article
        FloatingActionButton(
            onClick = { showAddDialog = true },
            containerColor = SpeakEasePrimary,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 22.dp, bottom = 80.dp)
                .shadow(10.dp, CircleShape, spotColor = SpeakEasePrimary.copy(alpha = 0.5f))
                .testTag("add_custom_reading_fab")
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Yangi matn qo'shish",
                modifier = Modifier.size(26.dp)
            )
        }

        if (showAddDialog) {
            AddReadingDialog(
                onDismiss = { showAddDialog = false },
                onSave = { title, author, category, content ->
                    onAddCustomReading(title, author, category, content)
                    showAddDialog = false
                }
            )
        }
    }
}

/**
 * Modern Article Card with clean white surface, squircle icon, difficulty tag, and start button.
 */
@Composable
fun SpeakEaseReadingItemCard(
    reading: ReadingItem,
    onOpen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        border = BorderStroke(1.dp, SpeakEaseCardBorder),
        shadowElevation = 2.dp,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onOpen)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Squircle Book / Topic Icon Badge
            SpeakEaseIconBadge(
                icon = Icons.Default.AutoStories,
                containerColor = SpeakEasePrimarySoft,
                tintColor = SpeakEasePrimary,
                size = 52.dp,
                iconSize = 24.dp,
                cornerRadius = 16.dp
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Category Tag
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SpeakEasePrimarySoft
                    ) {
                        Text(
                            text = reading.category,
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = SpeakEasePrimary,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                        )
                    }

                    // Level Tag
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SpeakEaseAmberSoft
                    ) {
                        Text(
                            text = reading.level,
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 10.sp,
                            color = SpeakEaseAmber,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = reading.title,
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = SpeakEaseTextPrimary
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = reading.author,
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = SpeakEaseTextSecondary
                    )

                    Text(
                        text = "•",
                        fontSize = 12.sp,
                        color = SpeakEaseTextMuted
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = SpeakEaseTextMuted,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${reading.estimatedReadMinutes} daq",
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Medium,
                            fontSize = 11.sp,
                            color = SpeakEaseTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Action Arrow Button
            SpeakEaseArrowActionBadge(
                onClick = onOpen,
                size = 36.dp
            )
        }
    }
}
