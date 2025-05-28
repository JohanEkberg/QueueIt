package se.johan.queueit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import se.johan.queueit.ui.handleUiEvent
import se.johan.queueit.viewmodel.BottomSheetViewModel
import se.johan.queueit.viewmodel.SongOverviewViewModel
import se.johan.queueit.viewmodel.dynamicBottomPadding

@Composable
fun SongOverviewScreen(
    navController: NavController,
    songOverviewViewModel: SongOverviewViewModel = hiltViewModel(),
    bottomSheetViewModel: BottomSheetViewModel = hiltViewModel(LocalContext.current as ViewModelStoreOwner)
) {
//    val overview by songInformationViewModel.overview.collectAsState()
    val lyric by songOverviewViewModel.lyric.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        songOverviewViewModel.uiEvent.collect { event ->
            handleUiEvent(event) {
                snackbarHostState.showSnackbar(it)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(
            snackbarHostState,
            modifier = Modifier.padding(bottom = bottomSheetViewModel.dynamicBottomPadding())
        ) },
        modifier = Modifier.fillMaxSize()
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = contentPadding.calculateTopPadding(),
                    bottom = bottomSheetViewModel.dynamicBottomPadding()
                ),
            verticalArrangement = Arrangement.Top
        ) {
            item {
                //if (overview.isNotEmpty()) {
                    Text(
                        "Overview",
                        color = Color.Black,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp, end = 4.dp),
                        textAlign = TextAlign.Center
                    )
                //}
            }
            item {
                Text(
                    text = "overview" ?: "",
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.widthIn(max = 250.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            item {
                if (lyric.isNotEmpty()) {
                    Text(
                        text = "Lyric",
                        color = Color.Black,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp, end = 4.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
            item {
                Text(
                    text = lyric,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}