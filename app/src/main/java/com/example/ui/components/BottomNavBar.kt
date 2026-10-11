package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.Screen
import com.example.ui.theme.PlusJakartaSans
import com.example.ui.theme.SpeakEaseCardBorder
import com.example.ui.theme.SpeakEasePrimary
import com.example.ui.theme.SpeakEasePrimarySoft
import com.example.ui.theme.SpeakEaseTextSecondary

enum class MainTab(
    val title: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector,
    val testTag: String
) {
    READING("O'qish", Icons.Filled.Home, Icons.Outlined.Home, "tab_reading"),
    WRITING("Yozuv", Icons.Filled.EditNote, Icons.Outlined.EditNote, "tab_writing"),
    VOCABULARY("Lug'at", Icons.Filled.Bookmark, Icons.Outlined.BookmarkBorder, "tab_vocabulary")
}

@Composable
fun AppBottomNavBar(
    currentScreen: Screen,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeTab = when (currentScreen) {
        is Screen.ReadingList, is Screen.Reader, is Screen.ReadingHistory -> MainTab.READING
        is Screen.WritingList, is Screen.WritingEditor -> MainTab.WRITING
        is Screen.VocabularyList, is Screen.VocabularyQuiz -> MainTab.VOCABULARY
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 24.dp, vertical = 10.dp)
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(36.dp),
                spotColor = SpeakEasePrimary.copy(alpha = 0.28f)
            ),
        shape = RoundedCornerShape(36.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, SpeakEaseCardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MainTab.entries.forEach { tab ->
                val isSelected = activeTab == tab

                if (isSelected) {
                    // Active Pill dock item: Purple circle with icon + title
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(26.dp))
                            .background(SpeakEasePrimarySoft)
                            .clickable { onTabSelected(tab) }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                            .testTag(tab.testTag)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(SpeakEasePrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = tab.filledIcon,
                                contentDescription = tab.title,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = tab.title,
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp,
                            color = SpeakEasePrimary
                        )
                    }
                } else {
                    // Inactive Icon with gentle ripple
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .clickable { onTabSelected(tab) }
                            .testTag(tab.testTag),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = tab.outlinedIcon,
                            contentDescription = tab.title,
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(23.dp)
                        )
                    }
                }
            }
        }
    }
}
