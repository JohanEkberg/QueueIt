package se.johan.queueit.ui.screens.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.johan.queueit.mediastore.util.getAlbumArtWork
import se.johan.queueit.mediastore.util.getDefaultArtWork
import se.johan.queueit.mediastore.util.getFormattedDurationTime
import se.johan.queueit.model.database.AlbumEntity
import se.johan.queueit.util.adjustForWhiteText
import se.johan.queueit.util.getDominantColor
import se.johan.queueit.viewmodel.AlbumUiModel

@Composable
fun ExpandableAlbumList(albumsFromArtist: List<AlbumUiModel>) {
    val context = LocalContext.current

    // Holds the background color for each album
    val dominantColors = remember { mutableStateMapOf<AlbumEntity, Pair<Bitmap,Color>>() }

    // Compute dominant colors once when albums are first available and also get the album bitmap
    LaunchedEffect(albumsFromArtist) {
        albumsFromArtist.forEach { albumUiModel ->
            val bitmap = getAlbumArtWork(context, albumUiModel.album.albumUri ?: "")
            getDominantColor(bitmap) { color ->
                dominantColors[albumUiModel.album] = Pair(bitmap,adjustForWhiteText(color))
            }
        }
    }

    // Use remember to manage expanded state per album
    var expandedStates by remember { mutableStateOf(albumsFromArtist.associate { it.album to false }) }

    LazyColumn {
        items(albumsFromArtist.size) { index ->
            val albumUiModel = albumsFromArtist[index]
            val color = dominantColors[albumUiModel.album]?.second ?: MaterialTheme.colorScheme.primary
            val artWork = dominantColors[albumUiModel.album]?.first ?: getDefaultArtWork(context)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                // Album Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(color)
                        .clickable {
                            expandedStates = expandedStates.toMutableMap().apply {
                                this[albumUiModel.album] = !(this[albumUiModel.album] ?: false)
                            }
                        }
                        .padding(0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = remember(artWork) { BitmapPainter(artWork.asImageBitmap()) },
                        //painter = painterResource(R.drawable.default_music2),
                        contentDescription = null,
                        modifier = Modifier.size(60.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(albumUiModel.album.albumName ?: "", color = Color.White, fontSize = 16.sp)
                        Row {
                            Text(albumUiModel.album.year ?: "", color = Color.White, fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("${albumUiModel.songs.size} songs", color = Color.White, fontSize = 14.sp)
                        }
                    }

                    Icon(
                        imageVector = if (expandedStates[albumUiModel.album] == true)
                            Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                // Expanded Songs List
                if (expandedStates[albumUiModel.album] == true) {
                    albumUiModel.songs.forEach { song ->
                        TrackItem(
                            songTitle = song.songName ?: "[Unknown title]",
                            albumGroup = albumUiModel.artist ?: "[Unknown artist]",
                            playTime = getFormattedDurationTime(song.duration ?: ""),
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.primary)
                                .clickable {
                                    //onTrackClick(it)

//                                    // Show player
//                                    bottomSheetViewModel.show {
//                                        Player(
//                                            albumArtWork,
//                                            it.artist?.artistName ?: "[Unknown artist]",
//                                            it.songEntity.songName ?: "[Unknown title]"
//                                        )
//                                    }
                                }
                            )
                    }
                }
            }
        }
    }
}