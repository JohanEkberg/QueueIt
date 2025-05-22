package se.johan.queueit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import se.johan.queueit.viewmodel.SharedSearchViewModel

@Composable
fun SearchScreen(
    navController: NavController,
    sharedSearchViewModel: SharedSearchViewModel
) {
    val artists by sharedSearchViewModel.artists.collectAsState()
    val albums by sharedSearchViewModel.albums.collectAsState()
    val songs by sharedSearchViewModel.songs.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = contentPadding.calculateBottomPadding()),
            verticalArrangement = Arrangement.Top
        ) {
            item {
                Row(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.Transparent),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (artists.isNotEmpty()) {
                        Text(
                            "Artists",
                            color = Color.Black,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                        )
                    }
                }
            }
            items(artists) { artist ->
                Row(modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(AppScreens.ArtistScreenIdentifier(artistId = artist.artistId))
                    }
                ) {
                    Text(text = artist.artistName ?: "", color = Color.Black)
                    Spacer(modifier = Modifier.weight(1f)) // Pushes the icon to the right
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
            item {
                Row(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.Transparent),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (albums.isNotEmpty()) {
                        Text(
                            "Albums",
                            color = Color.Black,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                        )
                    }
                }
            }
            items(albums) { album ->
                Row(modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(AppScreens.AlbumScreenIdentifier(albumId = album.albumId))
                    }
                ) {
                    Text(text = album.albumName ?: "", color = Color.Black)
                    Spacer(modifier = Modifier.weight(1f)) // Pushes the icon to the right
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
            item { Row(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Transparent),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (songs.isNotEmpty()) {
                    Text(
                        "Songs",
                        color = Color.Black,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                    )
                }
            } }
            items(songs) { song ->
                Text(text = song.songName ?: "", color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}