package se.johan.queueit.ui.screens.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.johan.queueit.R
import se.johan.queueit.model.database.AlbumWithSongs

@Composable
fun AlbumHeader(albumWithSongs: AlbumWithSongs?, albumArtWork: Bitmap) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Album Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
        ) {
            Image(
                painter = remember(albumArtWork) { BitmapPainter(albumArtWork.asImageBitmap()) } ,
                contentDescription = "Album Art",
                modifier = Modifier.size(100.dp)
            )

            Column(
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Text(
                    text = albumWithSongs?.albumEntity?.albumName ?: "",
                    fontSize = 16.sp,
                    color = Color.White
                )
                Text(
                    text = if (albumWithSongs?.songList.isNullOrEmpty()) {
                        ""
                    } else {
                        albumWithSongs?.songList?.get(0)?.artist?.artistName ?: ""
                    },
                    fontSize = 14.sp,
                    color = Color.White
                )
                Text(
                    text = "${stringResource(R.string.album_grid_card_number_of_songs)} " +
                            albumWithSongs?.songList?.size.toString(),
                    fontSize = 14.sp,
                    color = Color.White
                )
                Text(
                    text = "",
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun AlbumHeaderPreview() {
//    QueueItTheme {
//        AlbumHeader()
//    }
//}