package com.example.comictracker.presentation.ui.screens.aboutScreens.aboutSeries

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.comictracker.domain.model.SeriesModel
import com.example.comictracker.presentation.ui.screens.aboutScreens.ExpandableText

/**
 * About series sec
 *
 * @param series
 */
@Composable
fun AboutSeriesSec(series: SeriesModel){
    Column {
        Row(Modifier.fillMaxWidth()){
            Column {
                Text(
                    text = series.title?:"No title",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.width(200.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "DATE",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = series.date?:"Date not date",
                    fontSize = 14.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "DESCRIPTION",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                ExpandableText(text = series.desc?:"No description")

            }

            Card(modifier = Modifier
                .padding(start = 60.dp)
                .width(127.dp)
                .height(200.dp)) {
                Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.TopEnd){
                    AsyncImage(model = series.image,
                        contentScale = ContentScale.FillBounds,
                        contentDescription = " ${series.title} current cover",modifier = Modifier
                            .width(145.dp)
                            .height(200.dp))
                }

            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}