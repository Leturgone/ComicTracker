package com.example.comictracker.ui.screens.libaryScreen

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
import com.example.comictracker.domain.model.SeriesModel
import com.example.comictracker.ui.screens.SeriesComicListCard

@Composable
fun CurrentReadingSec(currentReadingList: List<SeriesModel>, navController: NavHostController){

    Column {
        Text(text = "Currently reading",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp))

        Box(modifier = Modifier.fillMaxWidth(),contentAlignment = Alignment.TopEnd){
            Text(text = "See all",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    navController.navigate("all_cs/0/currentReading/0")
                }.padding(end = 15.dp, bottom = 12.dp))
        }
        LazyRow{
            items(currentReadingList.size){
                val currentRead  = currentReadingList[it]
                var lastPaddingEnd = 0
                if (it == currentReadingList.size - 1){
                    lastPaddingEnd = 16
                }
                SeriesComicListCard(title =currentRead.title!!,
                    image = currentRead.image!!,
                    lastPaddingEnd =lastPaddingEnd ) {
                    navController.navigate("series/${currentRead.seriesId}")

                }

            }
        }
    }
}