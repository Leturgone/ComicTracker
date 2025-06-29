package com.example.comictracker.presentation.ui.screens.aboutScreens.aboutSeries

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.comictracker.R
import com.example.comictracker.presentation.mvi.ComicAppState
import com.example.comictracker.presentation.mvi.DataState
import com.example.comictracker.presentation.mvi.intents.ComicFromSeriesScreenIntent
import com.example.comictracker.presentation.ui.screens.CustomToastMessage
import com.example.comictracker.presentation.viewmodel.ComicFromSeriesScreenViewModel

@Composable
fun AllComicSeriesSec(seriesId:Int,
                      loadCount:Int,
                      viewModel: ComicFromSeriesScreenViewModel = hiltViewModel(),
                      navController: NavHostController){

    val uiState by viewModel.state.collectAsState()
    var showToast by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(key1 = seriesId) {
        viewModel.processIntent(ComicFromSeriesScreenIntent.LoadComicFromSeriesScreen(seriesId,loadCount))
    }
    Box(){
        Column {
            Text(text = stringResource(id = R.string.all_comics),
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp))

            uiState.let { state ->
                when(state){
                    is ComicAppState.AllComicSeriesScreenState -> {
                        when(state.comicFromSeriesList){
                            is DataState.Error -> LaunchedEffect(Unit){
                                errorMessage = state.comicFromSeriesList.errorMessage
                                showToast = true
                            }
                            DataState.Loading -> CircularProgressIndicator()
                            is DataState.Success -> {
                                val comics = state.comicFromSeriesList.result
                                LazyColumn(modifier = Modifier.testTag("all_comics_from_series")){
                                    items(comics.size){
                                        val comic = comics[it]
                                        ComicFromSeriesCard(comic,navController){
                                            when (comic.readMark) {
                                                "read" -> viewModel.processIntent(
                                                    ComicFromSeriesScreenIntent.MarkAsUnreadComicInList(
                                                        comic.comicId,
                                                        comic.seriesId,
                                                        comic.number,
                                                        loadCount
                                                    )
                                                )

                                                else -> viewModel.processIntent(
                                                    ComicFromSeriesScreenIntent.MarkAsReadComicInList(
                                                        comic.comicId,
                                                        comic.seriesId,
                                                        comic.number,
                                                        loadCount
                                                    )
                                                )
                                            }
                                        }
                                        if((it == comics.size-1) and (comics.size == loadCount+50)){
                                            Box(modifier = Modifier
                                                .fillMaxWidth()
                                                .testTag("load_more"), contentAlignment = Alignment.Center){
                                                Button(onClick = {
                                                    navController.popBackStack()
                                                    navController.navigate("comics_from_series/$seriesId/${loadCount+50}")
                                                }) {
                                                    Text(text = stringResource(id = R.string.load_more))
                                                }
                                            }

                                        }

                                    }

                                }

                            }
                        }
                    }

                    else -> {
                        CircularProgressIndicator()
                    }
                }
            }

        }
        CustomToastMessage(
            message = errorMessage,
            isVisible = showToast,
            onDismiss = { showToast = false })
    }

}