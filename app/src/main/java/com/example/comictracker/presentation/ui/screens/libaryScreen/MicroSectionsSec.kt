package com.example.comictracker.presentation.ui.screens.libaryScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.comictracker.domain.model.StatisticsforAll
import com.example.comictracker.R

@Composable
fun MicroSectionsSec(statistics: StatisticsforAll, navController: NavHostController){
    val stats = listOf(
        Pair(stringResource(id = R.string.comics),"comics") to "${statistics.comicCount} / ${statistics.comicCountThisYear} ${stringResource(id = R.string.this_year)}",
        Pair(stringResource(id = R.string.series_m),"series") to "${statistics.seriesCount} / ${statistics.seriesCountThisYear} ${stringResource(id = R.string.this_year)}",
        Pair(stringResource(id = R.string.readlist),"readlist") to "${statistics.readlistCount}",
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        stats.forEach { (label, value) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .clickable {
                        when (label.second) {
                            "comics" -> navController.navigate("all_cs/0/allLibComic/0")
                            "series" -> navController.navigate("all_cs/0/allLibSeries/0")
                            "readlist" -> navController.navigate("all_cs/0/readlist/0")
                        }
                    },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = label.first,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
                Text(
                    text = value,
                    textAlign = TextAlign.End
                )
            }
            HorizontalDivider()
        }
    }
}