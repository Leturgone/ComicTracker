package com.example.comictracker.presentation.ui.screens.searchScreen

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.comictracker.presentation.ui.screens.SeriesComicListCard
import com.example.comictracker.domain.model.SeriesModel
import com.example.comictracker.R

@Composable
fun DiscoverSec(discoverComicList: List<SeriesModel>, navController: NavHostController){


    Column {
        Text(text = stringResource(id = R.string.discover),
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp))

        Box(modifier = Modifier.fillMaxWidth(),contentAlignment = Alignment.TopEnd){
            Text(text = stringResource(id = R.string.see_all),
                color = MaterialTheme.colorScheme.primary, modifier = Modifier
                    .padding(end = 15.dp, bottom = 12.dp).testTag("seeAllDiscover")
                    .clickable {
                        navController.navigate("all_cs/0/discover/0")
                    })
        }
        LazyRow{
            items(discoverComicList.size){
                val newSeries  = discoverComicList[it]
                var lastPaddingEnd = 0
                if (it == discoverComicList.size - 1){
                    lastPaddingEnd = 16
                }
                SeriesComicListCard(
                    title = newSeries.title!!,
                    image = newSeries.image!!,
                    lastPaddingEnd = lastPaddingEnd) {
                    navController.navigate("series/${newSeries.seriesId}")
                }

            }
        }
    }

}
