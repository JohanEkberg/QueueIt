package se.johan.queueit.mediastore.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.util.Size
import se.johan.queueit.R
import se.johan.queueit.TAG

fun getArtWork(context: Context, artWork: String) : Bitmap {
    return try {
        if (artWork.isNotEmpty()) {
            val imageUri = Uri.parse(artWork)
            context.contentResolver.loadThumbnail(imageUri, Size(640, 480), null)
        } else {
            BitmapFactory.decodeResource(context.resources, R.drawable.default_music2)
        }
    } catch(e: Exception) {
        Log.e(TAG, "Failed to get album artwork, exception: ${e.message}")
        BitmapFactory.decodeResource(context.resources, R.drawable.default_music2)
    }
}

fun getArtWork(context: Context, artWork: Uri?) : Bitmap {
    return try {
        if (artWork != null) {
            context.contentResolver.loadThumbnail(artWork, Size(640, 480), null)
        } else {
            BitmapFactory.decodeResource(context.resources, R.drawable.default_music2)
        }
    } catch(e: Exception) {
        Log.e(TAG, "Failed to get album artwork, exception: ${e.message}")
        BitmapFactory.decodeResource(context.resources, R.drawable.default_music2)
    }
}

fun getDefaultArtWork(context: Context): Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.default_music2)