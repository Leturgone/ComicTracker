package com.example.comictracker.aboutScreens.aboutSeries

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.comictracker.SeriesComicListCard
import com.example.comictracker.data.ComicCover


@Composable
fun RelatedSec(navController: NavHostController){
    var connectedComicList: List<ComicCover> = listOf(
        ComicCover("Spider-Man Noir","http://i.annihil.us/u/prod/marvel/i/mg/5/e0/5bc77a942112a.jpg",null),
        ComicCover("Spider-Man Noir","http://i.annihil.us/u/prod/marvel/i/mg/5/e0/5bc77a942112a.jpg",null),
        ComicCover("Spider-Man Noir","http://i.annihil.us/u/prod/marvel/i/mg/5/e0/5bc77a942112a.jpg",null),

    )
    Column {
        Text(text = "Connected",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp))
        Box(modifier = Modifier.fillMaxWidth(),contentAlignment = Alignment.TopEnd){
            Text(text = "See all",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 15.dp, bottom = 12.dp).clickable {
                    navController.navigate("all_cs")
                })
        }
        LazyRow{
            items(connectedComicList.size){
                val currentComicCover  = connectedComicList[it]
                var lastPaddingEnd = 0
                if (it == connectedComicList.size - 1){
                    lastPaddingEnd = 16
                }
                SeriesComicListCard(title = currentComicCover.title,
                    image = currentComicCover.imageUrl, lastPaddingEnd = lastPaddingEnd ) {
                    navController.navigate("series")
                }
            }
        }
    }
}