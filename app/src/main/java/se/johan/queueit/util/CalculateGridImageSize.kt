package se.johan.queueit.util

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun calculateGridImageSize(screenWidth: Int): Dp {
    val spacing = 4.dp
    val columns = 3

    val screenWidthDp = screenWidth.dp
    val totalSpacing = spacing * (columns + 1)
    val itemSize = (screenWidthDp - totalSpacing) / columns

    return itemSize
}