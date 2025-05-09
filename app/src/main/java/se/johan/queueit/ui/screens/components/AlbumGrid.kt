package se.johan.queueit.ui.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import se.johan.queueit.ui.screens.AlbumScreenIdentifier
import se.johan.queueit.viewmodel.AlbumsViewModel

@Composable
fun AlbumGrid(navController: NavController, albumsViewModel: AlbumsViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val albums = albumsViewModel.albums.collectAsLazyPagingItems()

    // Trigger data fetch when the composable is first launched
    LaunchedEffect(Unit) {
        albumsViewModel.getAlbums()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp), // optional
        verticalArrangement = Arrangement.Top
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(bottom = 0.dp)
        ) {
            items(albums.itemCount) { index ->
                albums[index]?.let {
                    AlbumCard(
                        albumTitle = it.albumEntity.albumName ?: "",
                        albumGroup = it.songList[0].artist?.artistName ?: "",
                        nbrOfSongs = it.songList.size.toString(),
                        artWork = getAlbumArtWork(context, it.albumEntity.albumUri ?: ""),
                        modifier = Modifier.clickable {
                            navController.navigate(AlbumScreenIdentifier(it.albumEntity.albumId))
                        },
                        itemSize = albumsViewModel.getItemSize(LocalConfiguration.current.screenWidthDp)
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun AlbumGridPreview() {
//    QueueItTheme {
//        AlbumGrid()
//    }
//}