package se.johan.queueit.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import se.johan.queueit.ui.screens.components.AlbumGrid
import se.johan.queueit.viewmodel.BottomSheetViewModel
import se.johan.queueit.viewmodel.dynamicBottomPadding

@Composable
fun HomeScreen(
    navController: NavController,
    bottomSheetViewModel: BottomSheetViewModel = hiltViewModel(LocalContext.current as ViewModelStoreOwner)
) {
    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(pageCount = { 3 })
    val tabTitles = listOf("Albums", "Artist", "Queue")

    Scaffold (
        topBar = {
            TabRow(selectedTabIndex = pagerState.currentPage) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(title) }
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
            when (page) {
                0 -> {
                    AlbumGrid(navController)
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