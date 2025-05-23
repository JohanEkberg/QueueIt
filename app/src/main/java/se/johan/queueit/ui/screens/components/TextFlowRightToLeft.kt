package se.johan.queueit.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import se.johan.queueit.ui.theme.Gray800

@Composable
fun TextFlowRightToLeft(messages: List<String>) {
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Box (
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        LazyRow(
            state = listState,
            reverseLayout = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            items(messages) { msg ->

                Box(
                    modifier = Modifier
                        .padding(horizontal = 0.dp)
                        .background(Color.Transparent)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${msg.trim()} :",
                        color = Gray800,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }

        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            val screenWidth = constraints.maxWidth.toFloat()
            val fadeWidth = screenWidth * 0.2f // 20% of screen width

            // Left edge gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color.White, Color.Transparent),
                            startX = 0f,
                            endX = fadeWidth
                        )
                    )
            )

            // Right edge gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color.Transparent, Color.White),
                            startX = screenWidth - fadeWidth,
                            endX = screenWidth
                        )
                    )
            )
        }
    }
}