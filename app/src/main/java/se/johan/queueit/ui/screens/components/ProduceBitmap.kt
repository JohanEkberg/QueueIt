package se.johan.queueit.ui.screens.components

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import se.johan.queueit.mediastore.util.getArtWork

@Composable
fun produceBitmap(context: Context, uri: String): Bitmap? {
    return produceState<Bitmap?>(initialValue = null, uri) {
        value = withContext(Dispatchers.IO) {
            getArtWork(context, uri) // Make sure this is efficient
        }
    }.value
}

@Composable
fun produceBitmap(context: Context, uri: Uri): Bitmap? {
    return produceState<Bitmap?>(initialValue = null, uri) {
        value = withContext(Dispatchers.IO) {
            getArtWork(context, uri) // Make sure this is efficient
        }
    }.value
}