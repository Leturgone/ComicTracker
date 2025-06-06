package com.example.comictracker.presentation.ui.screens.searchScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.comictracker.R
import com.example.comictracker.domain.model.SeriesModel
import com.example.comictracker.presentation.mvi.ComicAppState
import com.example.comictracker.presentation.mvi.DataState
import com.example.comictracker.presentation.mvi.intents.SearchScreenIntent
import com.example.comictracker.presentation.viewmodel.SearchScreenViewModel

@Composable
fun SearchResultScreen(query: String,
                       navController: NavHostController,
                       viewModel: SearchScreenViewModel = hiltViewModel()){



    val uiState by viewModel.state.collectAsState()


    LaunchedEffect(key1 = query) {
        viewModel.processIntent(SearchScreenIntent.Search(query))
    }
    
    Column {
        Text(text = stringResource(id = R.string.search_result),
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp))

        uiState.let {state ->
            when(state) {
                is ComicAppState.SearchResultScreenSate -> {
                    Column {
                        when(state.character){
                            is DataState.Error ->{}
                            DataState.Loading -> CircularProgressIndicator()
                            is DataState.Success -> {
                                if(state.character.result.isNotEmpty()) {
                                    LazyRow {
                                        items(state.character.result.size) {
                                            val character = state.character.result[it]
                                            CharacterCard(
                                                imageUrl = character.image,
                                                characterName = character.name,
                                                cardSize = 120,
                                                imageSize = 50
                                            ) {
                                                navController.navigate("character/${character.characterId}")
                                            }
                                        }
                                    }
                                }
                                else{
                                    Column {
                                        Text(text = stringResource(id = R.string.char_no_found))
                                    }
                                }
                            }
                        }
                        when(state.series){
                            is DataState.Error -> {
                                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Column {
                                        Text(text = stringResource(id = R.string.not_found))
                                        Button(onClick = {
                                            navController.popBackStack()
                                            navController.navigate("search_result/$query")
                                        }) {
                                            Text(text = stringResource(id = R.string.update))
                                        }
                                    }
                                }

                            }
                            DataState.Loading -> CircularProgressIndicator()
                            is DataState.Success -> {
                                LazyColumn{
                                    items(state.series.result.size){
                                        val series = state.series.result[it]
                                        SearchSeriesCard(series){
                                            navController.navigate("series/${series.seriesId}")
                                        }

                                    }
                                }
                            }
                        }
                    }

                }
                else -> {CircularProgressIndicator()}
            }
        }


    }

}

@Composable
fun SearchSeriesCard(series: SeriesModel, clickFun:() -> Unit){
    Row(Modifier.clickable(onClick = clickFun)) {
        Column {
            Text(text = series.title!!,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp))

            Column(modifier = Modifier
                .padding(start = 16.dp)) {
                Card(modifier = Modifier
                    .width(127.dp)
                    .height(200.dp)) {
                    AsyncImage(model = series.image,
                        contentScale = ContentScale.FillBounds,
                        contentDescription = "${series.title}  current cover",modifier = Modifier
                            .width(145.dp)
                            .height(200.dp))
                }
            }

        }
        series.date?.let {
            Text(text = it,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(start = 200.dp, top = 56.dp))
        }
    }
}