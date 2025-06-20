package com.example.comictracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comictracker.domain.model.SeriesModel
import com.example.comictracker.domain.repository.local.LocalReadRepository
import com.example.comictracker.domain.repository.local.LocalWriteRepository
import com.example.comictracker.domain.repository.remote.RemoteCharacterRepository
import com.example.comictracker.domain.repository.remote.RemoteComicsRepository
import com.example.comictracker.domain.repository.remote.RemoteCreatorsRepository
import com.example.comictracker.domain.repository.remote.RemoteSeriesRepository
import com.example.comictracker.presentation.mvi.AboutSeriesScreenData
import com.example.comictracker.presentation.mvi.ComicAppState
import com.example.comictracker.presentation.mvi.DataState
import com.example.comictracker.presentation.mvi.intents.AboutSeriesScreenIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AboutSeriesScreenViewModel @Inject constructor(
    private val remoteSeriesRepository: RemoteSeriesRepository,
    private val remoteComicsRepository:RemoteComicsRepository,
    private val remoteCharacterRepository: RemoteCharacterRepository,
    private val remoteCreatorsRepository: RemoteCreatorsRepository,
    private val localWriteRepository: LocalWriteRepository,
    private val localReadRepository: LocalReadRepository
): ViewModel(){

    private val _state = MutableStateFlow<ComicAppState>(ComicAppState.HomeScreenState())
    val state: StateFlow<ComicAppState> = _state

    fun processIntent(intent:AboutSeriesScreenIntent){
        when(intent){
            is AboutSeriesScreenIntent.AddSeriesToFavorite -> addSeriesToFavorite(intent.seriesId)
            is AboutSeriesScreenIntent.LoadSeriesScreen -> loadSeriesScreen(intent.seriesId)
            is AboutSeriesScreenIntent.MarkAsCurrentlyReadingSeries -> markAsCurrentlyReadingSeries(intent.seriesId,intent.firstIssueId)
            is AboutSeriesScreenIntent.MarkAsReadSeries -> markAsReadSeries(intent.seriesId)
            is AboutSeriesScreenIntent.MarkAsUnreadSeries -> markUnreadSeries(intent.seriesId)
            is AboutSeriesScreenIntent.MarkAsWillBeReadSeries -> markWillBeReadSeries(intent.seriesId)
            is AboutSeriesScreenIntent.RemoveSeriesFromFavorite -> removeSeriesFromFavorites(intent.seriesId)
            is AboutSeriesScreenIntent.MarkAsReadNextComic -> markAsReadNextComic(intent.comicId,intent.seriesId, intent.issueNumber)
            is AboutSeriesScreenIntent.MarkAsUnreadLastComic -> markAsUnreadNextComic(intent.comicId,intent.seriesId, intent.issueNumber)
        }
    }

    private fun loadSeriesScreen(seriesId: Int)  = viewModelScope.launch {
        _state.value = ComicAppState.AboutComicScreenState(DataState.Loading)
        val seriesDeferred = async{
            remoteSeriesRepository.getSeriesById(seriesId)
        }

        val comicListDeferred = async {
            remoteComicsRepository.getComicsFromSeries(seriesId)
        }

        val characterListDeferred = async {
            remoteCharacterRepository.getSeriesCharacters(seriesId)
        }

        val series = seriesDeferred.await().fold(
            onSuccess = {it},
            onFailure = { emptyList<SeriesModel>()}
        ) // Получение series до зависимых задач.

        val creatorListDeferred = async {
            if (series is SeriesModel){
                remoteCreatorsRepository.getSeriesCreators(series.creators?: emptyList()).getOrDefault(
                    emptyList()
                )
            } else {
                emptyList()
            }

        }

        val connectedSeriesListDeferred = async {
            if (series is SeriesModel){
                remoteSeriesRepository.getConnectedSeries(series.connectedSeries).fold(
                    onSuccess = {it},
                    onFailure = { emptyList() }
                )
            }else{
                emptyList()
            }

        }

        val comicList = comicListDeferred.await().fold(
            onSuccess = {it},
            onFailure = { emptyList() }
        )
        val characterList = characterListDeferred.await().fold(
            onSuccess = {it},
            onFailure = { emptyList() }
        )
        val creatorList = creatorListDeferred.await()
        val connectedSeriesList = connectedSeriesListDeferred.await()

        _state.value = ComicAppState.AboutSeriesScreenState(
            when(series){
                is SeriesModel -> {
                    val readMarkDef = async { localReadRepository.loadSeriesMark(series.seriesId)}
                    val favoriteMarkDef = async { localReadRepository.loadSeriesFavoriteMark(series.seriesId) }
                    val nextReadLocDef = async {  localReadRepository.loadNextRead(series.seriesId)}
                    val nextReadComicId = nextReadLocDef.await().fold(
                        onSuccess = {it},
                        onFailure = {null}
                    )
                    val nextRead = nextReadComicId?.let { nextComicId ->
                        remoteComicsRepository.getComicById(nextComicId).fold(
                            onSuccess = {it},
                            onFailure = {null}
                        )
                    }
                    val readMark = readMarkDef.await().fold(
                        onSuccess ={it},
                        onFailure = {null}
                    )
                    val favoriteMark = favoriteMarkDef.await().fold(
                        onSuccess ={it},
                        onFailure = {null}
                    )
                    if (readMark == null || favoriteMark == null){
                        DataState.Error("Error loading this series ")
                    }else{
                        val seriesWithMark = series.copy(readMark = readMark, favoriteMark = favoriteMark)
                        DataState.Success(
                            AboutSeriesScreenData(
                                series = seriesWithMark,
                                comicList = comicList,
                                creatorList = creatorList,
                                characterList = characterList,
                                connectedSeriesList = connectedSeriesList,
                                nextRead = nextRead?: if (comicList.isNotEmpty()) comicList[0] else null
                            )
                        )
                    }
                }
                else -> DataState.Error("Error loading this series ")
            }
        )
    }

    private fun markAsCurrentlyReadingSeries(apiId:Int,firstIssueId:Int?)  = viewModelScope.launch{
        localWriteRepository.addSeriesToCurrentlyRead(apiId,firstIssueId).onSuccess {
            loadSeriesScreen(apiId)
        }
    }

    private fun markAsReadSeries(apiId:Int) = viewModelScope.launch(Dispatchers.IO){
        localWriteRepository.markSeriesRead(apiId).onSuccess {
            loadSeriesScreen(apiId)
        }
    }

    private fun markUnreadSeries(apiId:Int)  = viewModelScope.launch{
        localWriteRepository.markSeriesUnread(apiId).onSuccess {
            loadSeriesScreen(apiId)
        }
    }

    private fun markWillBeReadSeries(apiId:Int)  = viewModelScope.launch{
        localWriteRepository.addSeriesToWillBeRead(apiId).onSuccess {
            loadSeriesScreen(apiId)
        }
    }

    private fun addSeriesToFavorite(apiId: Int) = viewModelScope.launch {
        localWriteRepository.addSeriesToFavorite(apiId).onSuccess {
            loadSeriesScreen(apiId)
        }
    }

    private fun removeSeriesFromFavorites(apiId: Int)  = viewModelScope.launch{
        localWriteRepository.removeSeriesFromFavorite(apiId).onSuccess {
            loadSeriesScreen(apiId)
        }

    }

    private fun markAsReadNextComic(comicApiId: Int, seriesApiId: Int, number: String) = viewModelScope.launch{
        async {
            remoteComicsRepository.getNextComicId(seriesApiId,number.toFloat().toInt())
        }.await().onSuccess { nextComicId ->
            localWriteRepository.markComicRead(comicApiId,seriesApiId,nextComicId).onSuccess {
                loadSeriesScreen(seriesApiId)
            }
        }
    }

    private fun markAsUnreadNextComic(comicApiId: Int, seriesApiId: Int, number: String) = viewModelScope.launch{
        async {
            remoteComicsRepository.getPreviousComicId(seriesApiId, number.toFloat().toInt())
        }.await().onSuccess {prevComicId ->
            localWriteRepository.markComicUnread(comicApiId,seriesApiId,prevComicId).onSuccess {
                loadSeriesScreen(seriesApiId)
            }
        }
    }
}