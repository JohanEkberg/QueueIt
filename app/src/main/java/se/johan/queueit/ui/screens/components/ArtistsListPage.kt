package se.johan.queueit.ui.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import se.johan.queueit.ui.screens.AppScreens
import se.johan.queueit.viewmodel.ArtistsPageViewModel

@Composable
fun ArtistsListPage(navController: NavController, listState: LazyListState, artistsViewModel: ArtistsPageViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val artists = artistsViewModel.artists.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp), // optional
        verticalArrangement = Arrangement.Top
    ) {
        LazyColumn(state = listState) {
            items(artists.itemCount) { index ->
                val artistItem =  artists[index]

                if (artistItem != null && artistItem.songList.isNotEmpty()) {
                    val albumArt = produceBitmap(context, artistItem.songList[0].albumUri ?: "")
                    ArtistListItem(
                        artistArtWork = albumArt,
                        artist = artistItem.artistEntity.artistName ?: "",
                        numberOfAlbums = artistsViewModel.getNumberOfAlbums(artistItem.songList),
                        modifier = Modifier.clickable {
                            navController.navigate(AppScreens.ArtistScreenIdentifier(artistItem.artistEntity.artistId))
                        },
                        itemSize = artistsViewModel.getItemSize(LocalConfiguration.current.screenWidthDp)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
            }
        }
    }
}

