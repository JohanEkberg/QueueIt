package se.johan.queueit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import se.johan.queueit.ui.screens.components.ExpandableAlbumList
import se.johan.queueit.viewmodel.ArtistViewModel
import se.johan.queueit.viewmodel.BottomSheetViewModel

@Composable
fun ArtistScreen(
    navController: NavController,
    artistViewModel: ArtistViewModel = hiltViewModel(),
    bottomSheetViewModel: BottomSheetViewModel = hiltViewModel(LocalContext.current as ViewModelStoreOwner)
) {
    val albumsFromArtist = artistViewModel.albumsFromArtist.value

    // Setup swipe back properties
    val swipeThreshold = 100f
    var offsetX by remember { mutableStateOf(0f) }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    offsetX += dragAmount
                    if (offsetX < -swipeThreshold) {
                        navController.navigate(HomeScreenIdentifier) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            }
    ) {
        ExpandableAlbumList(
            navController,
            albumsFromArtist =  albumsFromArtist,
            artistViewModel.addTrackToQueue,
            bottomSheetViewModel = bottomSheetViewModel
        )
    }

}