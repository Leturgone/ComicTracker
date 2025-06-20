package com.example.comictracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comictracker.domain.repository.local.LocalReadRepository
import com.example.comictracker.domain.repository.remote.RemoteComicsRepository
import com.example.comictracker.domain.repository.remote.RemoteSeriesRepository
import com.example.comictracker.presentation.mvi.ComicAppState
import com.example.comictracker.presentation.mvi.DataState
import com.example.comictracker.presentation.mvi.MyLibraryScreenData
import com.example.comictracker.presentation.mvi.intents.LibraryScreenIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LibraryScreenViewModel @Inject constructor(
    private val remoteSeriesRepository: RemoteSeriesRepository,
    private val remoteComicsRepository: RemoteComicsRepository,
    private val localReadRepository: LocalReadRepository
):ViewModel() {
    private val _state = MutableStateFlow<ComicAppState>(ComicAppState.HomeScreenState())

    val state: StateFlow<ComicAppState> = _state

    fun processIntent(intent:LibraryScreenIntent){
        when(intent){
            LibraryScreenIntent.LoadLibraryScreen -> loadLibraryScreen()
        }
    }

    private fun loadLibraryScreen() = viewModelScope.launch {
        _state.value = ComicAppState.MyLibraryScreenState(DataState.Loading)

        val loadedStatisticsFromBDDef = async {localReadRepository.loadStatistics()}
        val loadedFavoriteSeriesIdsFromBDDef = async {localReadRepository.loadFavoritesIds() }
        val loadedCurrentlyReadingSeriesIdsFromBDDef = async { localReadRepository.loadCurrentReadIds(0) }
        val loadedHistoryReadComicFromBDDef = async {localReadRepository.loadHistory(0) }

        val loadedStatisticsFromBD = loadedStatisticsFromBDDef.await().fold(
            onSuccess = {it},
            onFailure = {null}
        )
        val loadedFavoriteSeriesIdsFromBD = loadedFavoriteSeriesIdsFromBDDef.await().fold(
            onSuccess = {it},
            onFailure = {null}
        )
        val loadedCurrentlyReadingSeriesIdsFromBD = loadedCurrentlyReadingSeriesIdsFromBDDef.await().fold(
            onSuccess = {it},
            onFailure = {null}
        )
        val loadedHistoryReadComicFromBD = loadedHistoryReadComicFromBDDef.await().fold(
            onSuccess = {it},
            onFailure = {null}
        )
        val result = if (loadedStatisticsFromBD == null || loadedFavoriteSeriesIdsFromBD == null ||
            loadedCurrentlyReadingSeriesIdsFromBD ==null || loadedHistoryReadComicFromBD == null){
            DataState.Error("Error while loading library screen")
        }else{
            val favoriteSeriesDef = async { remoteSeriesRepository.fetchSeries(loadedFavoriteSeriesIdsFromBD) }
            val currentSeriesDef = async {remoteSeriesRepository.fetchSeries(loadedCurrentlyReadingSeriesIdsFromBD) }
            val lastComicsDef = async {remoteComicsRepository.fetchComics(loadedHistoryReadComicFromBD) }

            val favoriteSeries = favoriteSeriesDef.await().fold(
                onSuccess = {it},
                onFailure = { emptyList() }
            )
            val currentSeries = currentSeriesDef.await().fold(
                onSuccess = {it},
                onFailure = { emptyList() }
            )
            val lastComics = lastComicsDef.await().fold(
                onSuccess = {it},
                onFailure = { emptyList() }
            )

            DataState.Success(
                MyLibraryScreenData(
                    statistics = loadedStatisticsFromBD,
                    favoritesList = favoriteSeries,
                    currentlyReadingList = currentSeries,
                    lastUpdates = lastComics
                )
            )
        }

        _state.value = ComicAppState.MyLibraryScreenState(result)
    }
}