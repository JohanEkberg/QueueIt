package se.johan.queueit.ui.screens.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.johan.queueit.R
import se.johan.queueit.util.adjustForWhiteText
import se.johan.queueit.util.getDominantColor

@Composable
fun ArtistListItem(
    artistArtWork: Bitmap,
    artist: String,
    numberOfAlbums: String,
    itemSize: Dp,
    modifier: Modifier = Modifier
) {
    var backgroundColor by remember { mutableStateOf(Color.Black) }

    LaunchedEffect(Unit) {
        getDominantColor(artistArtWork) { dominantColor ->
            val adjustedColor = adjustForWhiteText(dominantColor)
            backgroundColor = adjustedColor
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = remember(artistArtWork) { BitmapPainter(artistArtWork.asImageBitmap()) },
            contentDescription = "Artist",
            modifier = Modifier
                .size(itemSize)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight()
        ) {
            Text(
                text = artist,
                fontSize = 16.sp,
                color = Color.White,
                maxLines = 1,
                modifier = Modifier
                    .padding(start = 25.dp)
                    .widthIn(max = 250.dp)
            )
            Text(
                text = stringResource(R.string.artist_list_item_number_of_albums) + numberOfAlbums,
                fontSize = 14.sp,
                color = Color.White, // gray_900
                maxLines = 1,
                modifier = Modifier
                    .padding(start = 25.dp)
                    .widthIn(max = 250.dp)
            )
        }
    }
}