package se.johan.queueit.ui.screens.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.johan.queueit.R
import se.johan.queueit.audio.data.AudioFileMetaData
import se.johan.queueit.mediastore.util.getFormattedDurationTime
import se.johan.queueit.ui.theme.blue200
import se.johan.queueit.ui.theme.blue300
import se.johan.queueit.util.adjustForWhiteText
import se.johan.queueit.util.getDominantColor

@Composable
fun QueueItem(
    artWork: Bitmap?,
    song: AudioFileMetaData,
    itemSize: Dp,
    modifier: Modifier = Modifier,
    onRemoveClick: (AudioFileMetaData) -> Unit = {}
) {
    var backgroundColor by remember { mutableStateOf(Color.Black) }

    LaunchedEffect(artWork) {
        artWork?.let {
            getDominantColor(it) { dominantColor ->
                val adjustedColor = adjustForWhiteText(dominantColor)
                backgroundColor = adjustedColor
            }
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor),
        verticalAlignment = Alignment.CenterVertically
    ) {
        artWork?.let {
            Image(
                painter = remember(it) { BitmapPainter(it.asImageBitmap()) },
                contentDescription = "Album",
                modifier = Modifier
                    .size(itemSize)
            )
        } ?: run {
            Image(
                painter = painterResource(id = R.drawable.default_music2),
                contentDescription = "Album",
                modifier = Modifier
                    .size(itemSize)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight()
        ) {
            Text(
                text = song.title,
                fontSize = 16.sp,
                color = Color.White,
                maxLines = 1,
                modifier = Modifier
                    .padding(start = 25.dp)
                    .widthIn(max = 250.dp)
            )
            Text(
                text = song.artist,
                fontSize = 14.sp,
                color = Color.White,
                maxLines = 1,
                modifier = Modifier
                    .padding(start = 25.dp)
                    .widthIn(max = 250.dp)
            )
            Text(
                text = song.album,
                fontSize = 14.sp,
                color = Color.White,
                maxLines = 1,
                modifier = Modifier
                    .padding(start = 25.dp)
                    .widthIn(max = 250.dp)
            )
            Text(
                text = getFormattedDurationTime(song.duration),
                fontSize = 14.sp,
                color = Color.White,
                maxLines = 1,
                modifier = Modifier
                    .padding(start = 25.dp)
                    .widthIn(max = 250.dp)
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_remove_from_queue),
            contentDescription = "Remove",
            modifier = Modifier
                .padding(top = 10.dp, start = 10.dp, end = 20.dp)
                .size(24.dp)
                .clickable { onRemoveClick(song) },
            tint = Color.Unspecified // or use tint if you want to apply color
        )
    }
}