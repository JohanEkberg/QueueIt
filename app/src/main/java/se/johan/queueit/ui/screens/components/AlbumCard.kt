package se.johan.queueit.ui.screens.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.johan.queueit.R
import se.johan.queueit.ui.theme.QueueItTheme
import se.johan.queueit.util.adjustForWhiteText
import se.johan.queueit.util.getDominantColor

@Composable
fun AlbumCard(
    albumTitle: String,
    albumGroup: String,
    nbrOfSongs: String,
    artWork: Bitmap,
    modifier: Modifier,
    itemSize: Dp
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .then(modifier) // Concatenate provided modifier with the new instance
    ) {

        var backgroundColor by remember { mutableStateOf(Color.Black) }

        LaunchedEffect(Unit) {
            getDominantColor(artWork) { dominantColor ->
                val adjustedColor = adjustForWhiteText(dominantColor)
                backgroundColor = adjustedColor
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            Image(
                painter = remember(artWork) { BitmapPainter(artWork.asImageBitmap()) },
                contentDescription = "Album cover",
                modifier = Modifier
                    .size(itemSize)
                    .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
            )
        }

        Column(
            modifier = Modifier
                .padding(0.dp)
                .width(itemSize)
                .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
                .background(backgroundColor),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = albumTitle,
                fontSize = 16.sp,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = albumGroup,
                fontSize = 14.sp,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "${stringResource(R.string.album_grid_card_number_of_songs)} ${nbrOfSongs}",
                fontSize = 14.sp,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AlbumGridPreview() {
    QueueItTheme {
        val context = LocalContext.current
        AlbumCard(
            "Final countdown",
            "Europe",
            "12",
            BitmapFactory.decodeResource(context.resources, R.drawable.default_music2),
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
            130.dp
        )
    }
}
