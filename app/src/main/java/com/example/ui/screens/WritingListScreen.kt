package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WritingNote
import com.example.ui.components.SpeakEaseIconBadge
import com.example.ui.components.SpeakEaseIconButton
import com.example.ui.theme.PlusJakartaSans
import com.example.ui.theme.SpeakEaseCardBorder
import com.example.ui.theme.SpeakEaseCoral
import com.example.ui.theme.SpeakEaseCoralBorder
import com.example.ui.theme.SpeakEaseCoralSoft
import com.example.ui.theme.SpeakEaseMint
import com.example.ui.theme.SpeakEaseMintBorder
import com.example.ui.theme.SpeakEaseMintSoft
import com.example.ui.theme.SpeakEasePrimary
import com.example.ui.theme.SpeakEasePrimaryBorder
import com.example.ui.theme.SpeakEasePrimaryDark
import com.example.ui.theme.SpeakEasePrimaryGradientEnd
import com.example.ui.theme.SpeakEasePrimaryGradientStart
import com.example.ui.theme.SpeakEasePrimarySoft
import com.example.ui.theme.SpeakEasePurple
import com.example.ui.theme.SpeakEasePurpleSoft
import com.example.ui.theme.SpeakEaseTextMuted
import com.example.ui.theme.SpeakEaseTextPrimary
import com.example.ui.theme.SpeakEaseTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WritingListScreen(
    notes: List<WritingNote>,
    searchQuery: String,
    selectedCategory: String,
    onSearchQueryChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onOpenNote: (Long?) -> Unit,
    onDeleteNote: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("Barchasi", "Eslatmalar", "Insho", "Kundalik", "Lug'at mashqi", "Hikoya")
    val totalWordsWritten = remember(notes) { notes.sumOf { it.wordCount } }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Banner Card
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
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    SpeakEaseIconBadge(
                                        icon = Icons.Default.EditNote,
                                        containerColor = Color.White,
                                        tintColor = SpeakEasePrimary,
                                        size = 46.dp,
                                        iconSize = 24.dp,
                                        cornerRadius = 14.dp
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Yozuv Studiyasi ✍️",
                                            fontFamily = PlusJakartaSans,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = SpeakEaseTextPrimary
                                        )
                                        Text(
                                            text = "Fikrlar, insholar va lug'at mashqlari",
                                            fontFamily = PlusJakartaSans,
                                            fontSize = 12.sp,
                                            color = SpeakEaseTextSecondary
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Metric pills
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = SpeakEasePrimarySoft,
                                    border = BorderStroke(1.dp, SpeakEasePrimaryBorder),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            "Jami yozuvlar",
                                            fontFamily = PlusJakartaSans,
                                            fontSize = 11.sp,
                                            color = SpeakEasePrimary,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            "${notes.size} ta",
                                            fontFamily = PlusJakartaSans,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = SpeakEasePrimary
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = SpeakEasePurpleSoft,
                                    border = BorderStroke(1.dp, Color(0xFFDDD6FE)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            "Yozilgan so'zlar",
                                            fontFamily = PlusJakartaSans,
                                            fontSize = 11.sp,
                                            color = SpeakEasePurple,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            "$totalWordsWritten ta",
                                            fontFamily = PlusJakartaSans,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = SpeakEasePurple
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = {
                        Text(
                            "Yozuvlarni qidirish...",
                            fontFamily = PlusJakartaSans,
                            fontSize = 14.sp
                        )
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Qidirish", tint = SpeakEasePrimary)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
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
                        .testTag("search_note_field")
                )
            }

            // Category Chips
            item {
                LazyRow(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { cat ->
                        val isSelected = selectedCategory == cat
                        val count = if (cat == "Barchasi") notes.size else notes.count { it.category == cat }

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
                                .clickable { onCategoryChange(cat) }
                                .padding(horizontal = 16.dp, vertical = 9.dp)
                        ) {
                            Text(
                                text = "$cat ($count)",
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
            if (notes.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SpeakEaseIconBadge(
                            icon = Icons.Default.EditNote,
                            containerColor = SpeakEasePrimarySoft,
                            tintColor = SpeakEasePrimary,
                            size = 64.dp,
                            iconSize = 32.dp,
                            cornerRadius = 22.dp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = if (searchQuery.isNotBlank()) "Qidiruv bo'yicha yozuv topilmadi" else "Hali hech qanday yozuv yo'q",
                            fontFamily = PlusJakartaSans,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SpeakEaseTextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Pastdagi \"+\" tugmasini bosib yangi eslatma yoki insho yarating!",
                            fontFamily = PlusJakartaSans,
                            fontSize = 13.sp,
                            color = SpeakEaseTextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }

            // Note items
            items(notes, key = { it.id }) { note ->
                SpeakEaseNoteCard(
                    note = note,
                    onClick = { onOpenNote(note.id) },
                    onDelete = { onDeleteNote(note.id) }
                )
            }
        }

        // FAB
        FloatingActionButton(
            onClick = { onOpenNote(null) },
            containerColor = SpeakEasePrimary,
            contentColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 90.dp, end = 20.dp)
                .shadow(8.dp, RoundedCornerShape(20.dp), spotColor = SpeakEasePrimary.copy(alpha = 0.4f))
                .testTag("add_note_fab")
        ) {
            Icon(Icons.Default.Add, contentDescription = "Yangi yozuv", modifier = Modifier.size(26.dp))
        }
    }
}

@Composable
fun SpeakEaseNoteCard(
    note: WritingNote,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val formattedDate = remember(note.updatedAt) {
        val sdf = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())
        sdf.format(Date(note.updatedAt))
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, SpeakEaseCardBorder),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(9.dp),
                        color = SpeakEasePrimarySoft,
                        border = BorderStroke(1.dp, SpeakEasePrimaryBorder)
                    ) {
                        Text(
                            text = note.category,
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.sp,
                            color = SpeakEasePrimary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    if (note.isPinned) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(9.dp),
                            color = SpeakEaseCoralSoft,
                            border = BorderStroke(1.dp, SpeakEaseCoralBorder)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PushPin,
                                    contentDescription = "Biriktirilgan",
                                    tint = SpeakEaseCoral,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "Muhim",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 10.sp,
                                    color = SpeakEaseCoral,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                SpeakEaseIconButton(
                    icon = Icons.Default.DeleteOutline,
                    onClick = onDelete,
                    containerColor = Color(0xFFF8F9FE),
                    tintColor = SpeakEaseTextMuted,
                    size = 34.dp,
                    iconSize = 18.dp,
                    contentDescription = "O'chirish"
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = note.title.ifBlank { "Nomsiz yozuv" },
                fontFamily = PlusJakartaSans,
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold,
                color = SpeakEaseTextPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = note.content.ifBlank { "Matn kiritilmagan..." },
                fontFamily = PlusJakartaSans,
                fontSize = 13.sp,
                color = SpeakEaseTextSecondary,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${note.wordCount} ta so'z",
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    color = SpeakEasePrimary,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = formattedDate,
                    fontFamily = PlusJakartaSans,
                    fontSize = 12.sp,
                    color = SpeakEaseTextMuted
                )
            }
        }
    }
}
