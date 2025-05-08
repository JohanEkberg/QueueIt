package se.johan.queueit.ui.screens.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AlbumCard(
    albumTitle: String,
    albumGroup: String,
    nbrOfSongs: String,
    artWork: Bitmap,
    modifier: Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .then(modifier) // Concatenate provided modifier with the new instance
            //.background(color = Color(0xFFBDBDBD)) // Equivalent to @color/gray_300
    ) {
        // Optional Card wrapper (you had it commented out)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            elevation = CardDefaults.cardElevation(10.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Black)
        ) {
            Column(
                modifier = Modifier
                    .padding(0.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = remember(artWork) { BitmapPainter(artWork.asImageBitmap()) },
                        contentDescription = "Album cover",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                }

                Text(
                    text = albumTitle.take(20),
                    fontSize = 16.sp,
                    color = Color.White
                )

                Text(
                    text = albumGroup,
                    fontSize = 14.sp,
                    color = Color.White
                )

                Text(
                    text = nbrOfSongs, // e.g. "12 songs"
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }
    }
}
