package se.johan.queueit.ui.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.johan.queueit.R

@Composable
fun TrackItem(
    songTitle: String,
    albumGroup: String,
    playTime: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight()
        ) {
            Text(
                text = songTitle,
                fontSize = 16.sp,
                color = Color.White,
                maxLines = 1,
                modifier = Modifier
                    .padding(start = 25.dp)
                    .widthIn(max = 250.dp)
            )
            Text(
                text = albumGroup,
                fontSize = 14.sp,
                color = Color.White, // gray_900
                maxLines = 1,
                modifier = Modifier
                    .padding(start = 25.dp)
                    .widthIn(max = 250.dp)
            )
            Text(
                text = playTime,
                fontSize = 14.sp,
                color = Color.White, // gray_900
                modifier = Modifier.padding(start = 25.dp)
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_add_to_queue),
            contentDescription = "Add to Queue",
            modifier = Modifier
                .padding(top = 10.dp, start = 10.dp, end = 20.dp).size(24.dp),
            tint = Color.Unspecified // show original icon colors
        )
    }
}