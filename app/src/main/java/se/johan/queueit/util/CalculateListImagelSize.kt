package se.johan.queueit.util

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun calculateListImageSize(screenWidthDp: Int): Dp {
    return (screenWidthDp * 0.15f).dp.coerceIn(48.dp, 72.dp)
}