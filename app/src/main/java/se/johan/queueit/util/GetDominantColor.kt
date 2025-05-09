package se.johan.queueit.util

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.luminance
import androidx.palette.graphics.Palette
import se.johan.queueit.TAG

/**
 * Get the most dominant color in the bitmap
 *
 * @param bitmap
 * @param onColorReady
 */
fun getDominantColor(bitmap: Bitmap, onColorReady: (Int) -> Unit) {
    try {
        Palette.from(bitmap).generate { palette ->
            val dominantColor = palette?.getDominantColor(0x000000) ?: 0x000000
            onColorReady(dominantColor)
        }
    } catch(e: Exception) {
        Log.e(TAG, "Failed to get dominant color, exception: ${e.message}")
        onColorReady(-1)
    }
}

/**
 * Adjust the bitmap to the white text by add a dark overlay.
 *
 * @param bgColor
 * @return Color
 */
fun adjustForWhiteText(bgColor: Int): Color {
    val color = Color(bgColor)
    val luminance = color.luminance() // from androidx.compose.ui.graphics

    return if (luminance > 0.7f) {
        // Too bright, darken it
        color.copy(alpha = 1f).compositeOver(Color.Black.copy(alpha = 0.3f))
    } else {
        // Dark enough, use as is or brighten slightly if too dark
        color
    }
}