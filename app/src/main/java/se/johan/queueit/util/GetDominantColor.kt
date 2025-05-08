package se.johan.queueit.util

import android.graphics.Bitmap
import android.util.Log
import androidx.palette.graphics.Palette
import se.johan.queueit.TAG

fun getDominantColor(bitmap: Bitmap, onColorReady: (Int) -> Unit) {
    try {
        Palette.from(bitmap).generate { palette ->
            val dominantColor = palette?.getDominantColor(0xFFFFFF) ?: 0xFFFFFF
            onColorReady(dominantColor)
        }
    } catch(e: Exception) {
        Log.e(TAG, "Failed to get dominant color, exception: ${e.message}")
        onColorReady(-1)
    }
}