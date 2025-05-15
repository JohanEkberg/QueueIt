package se.johan.queueit.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            verticalArrangement = Arrangement.Top
        ) {
            Text("Artists", color = Color.Black)
            LazyColumn {
                items(artists) { artist ->
                    Text(text = artist.artistName ?: "", color = Color.Black)
                    Spacer(modifier = Modifier.height(2.dp))
                }
            }
            Text("Albums", color = Color.Black)
            LazyColumn {
                items(albums) { album ->
                    Text(text = album.albumName ?: "", color = Color.Black)
                    Spacer(modifier = Modifier.height(2.dp))
                }
            }
            Text("Songs", color = Color.Black)
            LazyColumn {
                items(songs) { song ->
                    Text(text = song.songName ?: "", color = Color.Black)
                    Spacer(modifier = Modifier.height(2.dp))
                }
            }
        }
    }
}