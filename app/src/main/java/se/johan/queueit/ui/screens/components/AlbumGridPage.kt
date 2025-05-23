package se.johan.queueit.ui.screens.components

import androidx.compose.foundation.background
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
import se.johan.queueit.ui.screens.AppScreens
import se.johan.queueit.ui.theme.blue50
import se.johan.queueit.viewmodel.AlbumsPageViewModel

@Composable
fun AlbumGridPage(navController: NavController, albumsViewModel: AlbumsPageViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val albums = albumsViewModel.albums.collectAsLazyPagingItems()

    // Trigger data fetch when the composable is first launched
    LaunchedEffect(Unit) {
        albumsViewModel.getAlbums()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
        verticalArrangement = Arrangement.Top
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(bottom = 0.dp)
        ) {
            items(albums.itemCount) { index ->
                val albumItem =  albums[index]
                if (albumItem != null && albumItem.songList.isNotEmpty()) {
                    val albumArt = produceBitmap(context, albumItem.songList[0].songEntity.albumUri ?: "")
                    AlbumCard(
                        albumTitle = albumItem.albumEntity.albumName ?: "",
                        albumGroup = albumItem.songList[0].artist?.artistName ?: "",
                        nbrOfSongs = albumItem.songList.size.toString(),
                        artWork = albumArt,
                        modifier = Modifier.clickable {
                            navController.navigate(AppScreens.AlbumScreenIdentifier(albumItem.albumEntity.albumId))
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