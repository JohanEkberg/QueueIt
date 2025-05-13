package se.johan.queueit.util

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun calculateListImageSize(screenWidthDp: Int, min: Int = 48, max: Int = 72): Dp {
    return (screenWidthDp * 0.15f).dp.coerceIn(min.dp, max.dp)
//    return (screenWidthDp * 0.15f).dp.coerceIn(48.dp, 72.dp)
}