package se.johan.queueit.ui.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import se.johan.queueit.R
import se.johan.queueit.ui.screens.AppScreens
import se.johan.queueit.ui.theme.blue50
import se.johan.queueit.viewmodel.BottomSheetViewModel

@Composable
fun Player(
    navController: NavController,
    bottomSheetViewModel: BottomSheetViewModel = hiltViewModel(LocalContext.current as ViewModelStoreOwner)
) {
    val context = LocalContext.current
    val isPlaying = bottomSheetViewModel.isPlaying
    val currentSong = bottomSheetViewModel.currentSong
    val progress = bottomSheetViewModel.progress
    val shouldUpdate = bottomSheetViewModel.shouldUpdateSong

    // Dynamically get player height for the bottom sheet to adopt to
    val density = LocalDensity.current

    LaunchedEffect(shouldUpdate) {
        if (shouldUpdate) {
            bottomSheetViewModel.updateSong(context)
        }
    }

    currentSong?.let { song ->
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        val heightPx = coordinates.size.height
                        val heightDp = with(density) { heightPx.toDp() }
                        bottomSheetViewModel.updatePlayerHeight(heightDp)
                    },
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Album Image
                Image(
                    painter = remember(song.artwork) { BitmapPainter(song.artwork.asImageBitmap()) } ,
                    contentDescription = "Album Art",
                    modifier = Modifier.size(50.dp)
                )

                Column(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .weight(1f),
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = song.artist,
                        fontSize = 20.sp,
                        color = Color.White,
                        modifier = Modifier.padding(top = 5.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = song.title,
                        fontSize = 20.sp,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 10.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Lyrics Button
                IconButton(
                    onClick = {
                        navController.navigate(
                            AppScreens.SongOverviewScreenIdentifier(artist = song.artist, title = song.title)
                        )
                    },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.lyrics),
                        contentDescription = stringResource(id = R.string.player_play),
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                // Play Button with Progress
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                ) {
                    CircularProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.size(45.dp),
                        color = blue50,
                        trackColor = Color.Transparent,
                        strokeWidth = 4.dp
                    )

                    IconButton(
                        onClick = {bottomSheetViewModel.togglePlayPause(context)},
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play),
                            contentDescription = stringResource(id = if (isPlaying) R.string.player_pause else R.string.player_play),
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                // Next Button
                IconButton(
                    onClick = {bottomSheetViewModel.onSkip(context)},
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_skip_next),
                        contentDescription = stringResource(id = R.string.player_play),
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
    }
}