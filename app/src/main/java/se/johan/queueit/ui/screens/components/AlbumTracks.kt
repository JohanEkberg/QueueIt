package se.johan.queueit.ui.screens.components

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.johan.queueit.mediastore.util.getFormattedDurationTime
import se.johan.queueit.model.database.SongWithArtist
import se.johan.queueit.viewmodel.BottomSheetViewModel

@Composable
fun AlbumTracks(
    albumArtWork: Bitmap,
    tracks: List<SongWithArtist>,
    onTrackClick: (SongWithArtist) -> Unit,
    bottomSheetViewModel: BottomSheetViewModel) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        items(tracks.size) { index ->
            tracks[index].let { TrackItem(
                songTitle = it.songEntity.songName ?: "[Unknown title]",
                albumGroup = it.artist?.artistName ?: "[Unknown artist]",
                playTime = getFormattedDurationTime(it.songEntity.duration ?: ""),
                modifier = Modifier
                    .clickable {
                    onTrackClick(it)

                    // Show player
                    bottomSheetViewModel.show {
                        Player(
                            albumArtWork,
                            it.artist?.artistName ?: "[Unknown artist]",
                            it.songEntity.songName ?: "[Unknown title]"
                        )
                    }
                }
            ) }
        }
    }
}