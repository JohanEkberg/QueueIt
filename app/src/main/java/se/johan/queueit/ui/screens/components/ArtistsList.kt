package se.johan.queueit.ui.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import se.johan.queueit.mediastore.util.getAlbumArtWork
import se.johan.queueit.ui.screens.ArtistScreenIdentifier
import se.johan.queueit.viewmodel.ArtistsViewModel

@Composable
fun ArtistsList(navController: NavController, artistsViewModel: ArtistsViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val artists = artistsViewModel.artists.collectAsLazyPagingItems()

    // Trigger data fetch when the composable is first launched
    LaunchedEffect(Unit) {
        artistsViewModel.getArtists()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp), // optional
        verticalArrangement = Arrangement.Top
    ) {
        LazyColumn {
            items(artists.itemCount) { index ->
                artists[index]?.let {
                    ArtistListItem(
                        artistArtWork = getAlbumArtWork(context, ""),
                        artist = it.artistEntity.artistName ?: "",
                        numberOfAlbums = artistsViewModel.getNumberOfAlbums(it.songList),
                        modifier = Modifier.clickable {
                            navController.navigate(ArtistScreenIdentifier(it.artistEntity.artistId))
                        },
                        itemSize = artistsViewModel.getItemSize(LocalConfiguration.current.screenWidthDp)
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))
            }
        }
    }
}