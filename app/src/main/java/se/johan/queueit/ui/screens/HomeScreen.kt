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
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import se.johan.queueit.ui.screens.components.AlbumGrid

@Composable
fun HomeScreen(navController: NavController) {
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
//        Column(
//            modifier = Modifier
//                .padding(contentPadding)
//                .background(MaterialTheme.colorScheme.background)
//        ) {
//            TabRow(selectedTabIndex = pagerState.currentPage) {
//                tabTitles.forEachIndexed { index, title ->
//                    Tab(
//                        selected = pagerState.currentPage == index,
//                        onClick = {
//                            coroutineScope.launch {
//                                pagerState.animateScrollToPage(index)
//                            }
//                        },
//                        text = { Text(title) }
//                    )
//                }
//            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.padding(contentPadding)
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
    //}
}

//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    QueueItTheme {
//        HomeScreen()
//    }
//}