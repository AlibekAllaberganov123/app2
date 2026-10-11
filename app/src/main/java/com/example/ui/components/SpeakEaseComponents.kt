package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PlusJakartaSans
import com.example.ui.theme.SpeakEaseAmber
import com.example.ui.theme.SpeakEaseAmberBorder
import com.example.ui.theme.SpeakEaseAmberSoft
import com.example.ui.theme.SpeakEaseCardBorder
import com.example.ui.theme.SpeakEaseCoral
import com.example.ui.theme.SpeakEaseCoralBorder
import com.example.ui.theme.SpeakEaseCoralDark
import com.example.ui.theme.SpeakEaseCoralGradientEnd
import com.example.ui.theme.SpeakEaseCoralGradientStart
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
import com.example.ui.theme.SpeakEaseSky
import com.example.ui.theme.SpeakEaseSkyBorder
import com.example.ui.theme.SpeakEaseSkySoft
import com.example.ui.theme.SpeakEaseTextMuted
import com.example.ui.theme.SpeakEaseTextPrimary
import com.example.ui.theme.SpeakEaseTextSecondary

// =========================================================================
// Bespoke SpeakEase Badges and Custom Visual Icons
// Eliminates standard desktop OS flat icons with Dribbble-grade layered UI
// =========================================================================

/**
 * Reusable Squircle Icon Badge with pastel tinted container and vibrant icon.
 */
@Composable
fun SpeakEaseIconBadge(
    icon: ImageVector,
    containerColor: Color = SpeakEasePrimarySoft,
    tintColor: Color = SpeakEasePrimary,
    borderColor: Color = containerColor,
    size: Dp = 46.dp,
    iconSize: Dp = 22.dp,
    cornerRadius: Dp = 15.dp,
    contentDescription: String? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(cornerRadius))
            .background(containerColor)
            .border(1.dp, borderColor, RoundedCornerShape(cornerRadius)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tintColor,
            modifier = Modifier.size(iconSize)
        )
    }
}

/**
 * The signature Up-Right Arrow Action Badge from Dribbble Image 2 `↗`.
 * Appears on feature cards to indicate interactive progress.
 */
@Composable
fun SpeakEaseArrowActionBadge(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 34.dp,
    containerColor: Color = Color(0xFFF8FAFC),
    tintColor: Color = SpeakEaseTextPrimary
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(containerColor)
            .border(1.dp, Color(0xFFE2E8F0), CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "↗",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = tintColor
        )
    }
}

/**
 * The signature Black Circular Play Button from Dribbble Image 1 `[ ▶ ]`.
 */
@Composable
fun SpeakEasePlayCircleBadge(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
    backgroundColor: Color = Color(0xFF0F172A),
    iconColor: Color = Color.White
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Boshlash",
            tint = iconColor,
            modifier = Modifier.size((size.value * 0.55f).dp)
        )
    }
}

/**
 * Stylized interactive IconButton inside a rounded squircle.
 */
@Composable
fun SpeakEaseIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    containerColor: Color = Color.White,
    tintColor: Color = SpeakEaseTextPrimary,
    borderColor: Color = SpeakEaseCardBorder,
    size: Dp = 44.dp,
    iconSize: Dp = 20.dp,
    contentDescription: String? = null,
    testTag: String? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .shadow(2.dp, RoundedCornerShape(14.dp), spotColor = Color(0x10000000))
            .clip(RoundedCornerShape(14.dp))
            .background(containerColor)
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .then(if (testTag != null) Modifier.testTag(testTag) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tintColor,
            modifier = Modifier.size(iconSize)
        )
    }
}

/**
 * Flame Streak Badge (SpeakEase signature daily streak badge from shots).
 */
@Composable
fun SpeakEaseStreakBadge(
    streakDays: Int,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = SpeakEaseCoralSoft,
        border = BorderStroke(1.dp, SpeakEaseCoralBorder),
        modifier = modifier.then(
            if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(SpeakEaseCoralGradientStart, SpeakEaseCoralGradientEnd)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = "Streak",
                    tint = Color.White,
                    modifier = Modifier.size(15.dp)
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "$streakDays kun",
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 13.sp,
                color = SpeakEaseCoral
            )
        }
    }
}

/**
 * Audio Pronounce Badge with speaker and tactile feedback.
 */
@Composable
fun SpeakEaseAudioBadge(
    onClick: () -> Unit,
    size: Dp = 40.dp,
    containerColor: Color = SpeakEasePrimarySoft,
    tintColor: Color = SpeakEasePrimary,
    testTag: String? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(containerColor)
            .border(1.dp, SpeakEasePrimaryBorder, CircleShape)
            .clickable(onClick = onClick)
            .then(if (testTag != null) Modifier.testTag(testTag) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
            contentDescription = "Talaffuzni tinglash",
            tint = tintColor,
            modifier = Modifier.size((size.value * 0.52f).dp)
        )
    }
}

/**
 * XP Points Star Badge
 */
@Composable
fun SpeakEaseXpBadge(
    xp: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = SpeakEaseAmberSoft,
        border = BorderStroke(1.dp, SpeakEaseAmberBorder),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Stars,
                contentDescription = null,
                tint = SpeakEaseAmber,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "+$xp XP",
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp,
                color = Color(0xFFB45309)
            )
        }
    }
}

// =========================================================================
// SpeakEase Feature Grid Card (Exact replica of Image 2 2x2 grid!)
// =========================================================================

@Composable
fun SpeakEaseFeatureGridCard(
    title: String,
    progressText: String,
    progressFraction: Float,
    icon: ImageVector,
    iconBgColor: Color,
    iconTintColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = BorderStroke(1.dp, SpeakEaseCardBorder),
        shadowElevation = 2.dp,
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top Row: Custom Squircle Icon on left + Arrow Button on right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SpeakEaseIconBadge(
                    icon = icon,
                    containerColor = iconBgColor,
                    tintColor = iconTintColor,
                    borderColor = iconBgColor,
                    size = 46.dp,
                    iconSize = 22.dp,
                    cornerRadius = 14.dp
                )

                SpeakEaseArrowActionBadge(
                    onClick = onClick,
                    size = 32.dp
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Title
            Text(
                text = title,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = SpeakEaseTextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Progress text
            Text(
                text = progressText,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                color = SpeakEaseTextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { progressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = iconTintColor,
                trackColor = Color(0xFFF1F5F9)
            )
        }
    }
}

// =========================================================================
// Category Filter Pills with count capsules (Image 1 & 2 Dribbble style)
// =========================================================================

@Composable
fun SpeakEaseCategoryPill(
    label: String,
    count: Int? = null,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = if (isSelected) SpeakEasePrimary else Color.White
    val contentColor = if (isSelected) Color.White else SpeakEaseTextSecondary
    val borderColor = if (isSelected) SpeakEasePrimary else Color(0xFFE2E8F0)

    Surface(
        shape = RoundedCornerShape(22.dp),
        color = containerColor,
        border = BorderStroke(1.dp, borderColor),
        shadowElevation = if (isSelected) 3.dp else 0.dp,
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontFamily = PlusJakartaSans,
                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                fontSize = 13.sp,
                color = contentColor
            )

            if (count != null && count > 0) {
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) Color.White.copy(alpha = 0.25f)
                            else Color(0xFFF1F5F9)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = count.toString(),
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = if (isSelected) Color.White else SpeakEaseTextMuted
                    )
                }
            }
        }
    }
}

// =========================================================================
// SpeakEase Button Components
// =========================================================================

/**
 * Signature Hero Split-Pill Action Button (Image 1 & Image 2 Dribbble style)
 * Has pill text on left + circular action button on right!
 */
@Composable
fun SpeakEaseHeroActionButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.White,
    textColor: Color = SpeakEaseTextPrimary,
    actionButtonColor: Color = SpeakEasePrimary,
    actionIconColor: Color = Color.White
) {
    Surface(
        shape = RoundedCornerShape(32.dp),
        color = containerColor,
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        shadowElevation = 3.dp,
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(start = 22.dp, top = 6.dp, bottom = 6.dp, end = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                color = textColor,
                modifier = Modifier.weight(1f)
            )

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(actionButtonColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = actionIconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/**
 * Primary SpeakEase Action Button with gradient and shadow.
 */
@Composable
fun SpeakEasePrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    height: Dp = 54.dp,
    gradientColors: List<Color> = listOf(SpeakEasePrimaryGradientStart, SpeakEasePrimaryGradientEnd),
    testTag: String? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .graphicsLayer {
                this.alpha = if (enabled) 1f else 0.5f
            }
            .shadow(
                elevation = if (enabled) 6.dp else 0.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = gradientColors.first().copy(alpha = 0.35f)
            )
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.horizontalGradient(gradientColors))
            .clickable(enabled = enabled, onClick = onClick)
            .then(if (testTag != null) Modifier.testTag(testTag) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 18.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                color = Color.White
            )
        }
    }
}

/**
 * Secondary SpeakEase Action Button.
 */
@Composable
fun SpeakEaseSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    containerColor: Color = SpeakEasePrimarySoft,
    contentColor: Color = SpeakEasePrimary,
    borderColor: Color = SpeakEasePrimaryBorder,
    height: Dp = 50.dp,
    testTag: String? = null
) {
    Box(
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(18.dp))
            .background(containerColor)
            .border(1.dp, borderColor, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .then(if (testTag != null) Modifier.testTag(testTag) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = contentColor
            )
        }
    }
}

/**
 * Quiz and Multiple Choice Card Button.
 */
@Composable
fun SpeakEaseQuizOptionButton(
    letter: String,
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (bgColor, borderColor, contentColor, letterBg) = when {
        isCorrect == true -> Quadruple(
            SpeakEaseMintSoft,
            SpeakEaseMint,
            SpeakEaseMintDark,
            SpeakEaseMint
        )
        isCorrect == false -> Quadruple(
            SpeakEaseCoralSoft,
            SpeakEaseCoral,
            SpeakEaseCoralDark,
            SpeakEaseCoral
        )
        isSelected -> Quadruple(
            SpeakEasePrimarySoft,
            SpeakEasePrimary,
            SpeakEasePrimaryDark,
            SpeakEasePrimary
        )
        else -> Quadruple(
            Color.White,
            Color(0xFFE2E8F0),
            SpeakEaseTextPrimary,
            Color(0xFFF1F5F9)
        )
    }

    val animatedBorderWidth by animateFloatAsState(
        targetValue = if (isSelected || isCorrect != null) 2f else 1f,
        label = "border_width"
    )

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = bgColor,
        border = BorderStroke(animatedBorderWidth.dp, borderColor),
        shadowElevation = if (isSelected) 3.dp else 1.dp,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Letter Badge: [A], [B], [C]
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(letterBg),
                contentAlignment = Alignment.Center
            ) {
                if (isCorrect == true) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                } else {
                    Text(
                        text = letter,
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        color = if (isSelected || isCorrect != null) Color.White else Color(0xFF64748B)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = text,
                fontFamily = PlusJakartaSans,
                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                fontSize = 15.sp,
                color = contentColor,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
