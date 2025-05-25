package se.johan.queueit.ui.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import se.johan.queueit.mediastore.util.getDefaultArtWork
import se.johan.queueit.viewmodel.QueuePageViewModel

@Composable
fun QueuePage(navController: NavController, listState: LazyListState, queuePageViewModel: QueuePageViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val queueItems by queuePageViewModel.queueItems.collectAsState()

    // Trigger data fetch when the composable is first launched
    // TODO: Remove this and use Flow and observe changes to the queue instead.
    LaunchedEffect(Unit) {
        queuePageViewModel.getQueueItems()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
        verticalArrangement = Arrangement.Top
    ) {
        LazyColumn(state = listState) {
            items(queueItems) { queueItem ->
                val artWork = queueItem.albumUri?.let {
                    produceBitmap(context, queueItem.albumUri)
                } ?: run { getDefaultArtWork(context) }

                QueueItem(
                    artWork = artWork,
                    song = queueItem,
                    itemSize = queuePageViewModel.getItemSize(LocalConfiguration.current.screenWidthDp),
                    onRemoveClick = queuePageViewModel.removeQueueItem
                )

                Spacer(modifier = Modifier.height(2.dp))
            }
        }
    }
}