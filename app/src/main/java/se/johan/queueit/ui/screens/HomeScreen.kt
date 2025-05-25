package se.johan.queueit.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import se.johan.queueit.ui.screens.components.AlbumGridPage
import se.johan.queueit.ui.screens.components.ArtistsListPage
import se.johan.queueit.ui.screens.components.QueuePage
import se.johan.queueit.ui.theme.Gray400
import se.johan.queueit.viewmodel.BottomSheetViewModel
import se.johan.queueit.viewmodel.dynamicBottomPadding

@Composable
fun HomeScreen(
    navController: NavController,
    bottomSheetViewModel: BottomSheetViewModel = hiltViewModel(LocalContext.current as ViewModelStoreOwner)
) {
    val selectedColor = Color.White
    val unselectedColor = Gray400
    val indicatorColor = Color.White

    val coroutineScope = rememberCoroutineScope()

    val pageCount = 3
    val pagerState = rememberPagerState(pageCount = { pageCount })
    val listStateArtists = rememberSaveable(saver = LazyListState.Saver) {
        LazyListState()
    }
    val gridStateAlbums = rememberSaveable(saver = LazyGridState.Saver) {
        LazyGridState()
    }
    val listStateQueue = rememberSaveable(saver = LazyListState.Saver) {
        LazyListState()
    }
    val pageAlbums = "albums"
    val pageArtists = "artists"
    val pageQueue = "queue"
    val pageRoutes = listOf(pageAlbums, pageArtists, pageQueue) // map page indices to routes
    val tabTitles = listOf("Albums", "Artist", "Queue")

    Scaffold (
        topBar = {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                        color = indicatorColor,
                        height = 3.dp
                    )
                }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Text(title, color = if (pagerState.currentPage == index) selectedColor else unselectedColor
                        ) }
                    )
                }
            }
        }
    ) { contentPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(
                top = contentPadding.calculateTopPadding(),
                bottom = bottomSheetViewModel.dynamicBottomPadding())
        ) { page ->
            when (pageRoutes[page]) {
                pageAlbums -> {
                    AlbumGridPage(navController, gridStateAlbums)
                }
                pageArtists -> {
                    ArtistsListPage(navController, listStateArtists)
                }
                pageQueue -> {
                    QueuePage(navController, listStateQueue)
                }
                else -> {
                    Text("Test page: $page", Modifier.fillMaxSize())
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    QueueItTheme {
//        HomeScreen()
//    }
//}