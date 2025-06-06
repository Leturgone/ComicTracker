package com.example.comictracker.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comictracker.domain.repository.local.LocalReadRepository
import com.example.comictracker.domain.repository.local.LocalWriteRepository
import com.example.comictracker.domain.repository.remote.RemoteComicsRepository
import com.example.comictracker.presentation.mvi.ComicAppState
import com.example.comictracker.presentation.mvi.DataState
import com.example.comictracker.presentation.mvi.intents.ComicFromSeriesScreenIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ComicFromSeriesScreenViewModel @Inject constructor(
    private val remoteComicsRepository: RemoteComicsRepository,
    private val localWriteRepository: LocalWriteRepository,
    private val localReadRepository: LocalReadRepository
): ViewModel(){

    private val _state = MutableStateFlow<ComicAppState>(ComicAppState.HomeScreenState())
    val state: StateFlow<ComicAppState> = _state

    fun processIntent(intent:ComicFromSeriesScreenIntent){
        when(intent){
            is ComicFromSeriesScreenIntent.LoadComicFromSeriesScreen -> loadComicFromSeriesScreen(intent.seriesId,intent.loadCount)
            is ComicFromSeriesScreenIntent.MarkAsReadComicInList -> markAsReadComicInList(intent.comicId,intent.seriesId, intent.issueNumber,intent.loadedCount)
            is ComicFromSeriesScreenIntent.MarkAsUnreadComicInList -> markAsUnreadComicInList(intent.comicId,intent.seriesId, intent.issueNumber,intent.loadedCount)
        }
    }

    private fun loadComicFromSeriesScreen(seriesId: Int,loadedCount: Int)  = viewModelScope.launch{
        _state.value = ComicAppState.AllComicSeriesScreenState(DataState.Loading)
        try {
            withContext(Dispatchers.IO){
                _state.emit(
                    ComicAppState.AllComicSeriesScreenState(
                        DataState.Success(
                            remoteComicsRepository.getComicsFromSeries(seriesId,loadedCount).map {
                                val readMark = localReadRepository.loadComicMark(it.comicId)
                                it.copy(readMark = readMark)
                            })
                    )
                )
            }
        }catch (e:Exception){
            _state.value = ComicAppState.AllComicSeriesScreenState(
                DataState.Error("Error loading comic from this series : $e"))
        }

    }

    private fun markAsReadComicInList(comicApiId: Int, seriesApiId: Int, number: String, loadedCount: Int) = viewModelScope.launch{
        try {
            val nextComicId = async {
                remoteComicsRepository.getNextComicId(seriesApiId,number.toFloat().toInt())
            }.await()

            if (localWriteRepository.markComicRead(comicApiId,seriesApiId,nextComicId)){
                loadComicFromSeriesScreen(seriesApiId,loadedCount)
            }
        }catch (e:Exception){
            Log.e("markAsReadComicInList",e.toString())
        }

    }

    private fun markAsUnreadComicInList(comicApiId: Int, seriesApiId: Int, number: String, loadedCount: Int)  = viewModelScope.launch{
        try {
            val prevComicId = async {
                remoteComicsRepository.getPreviousComicId(seriesApiId, number.toFloat().toInt())
            }.await()

            if (localWriteRepository.markComicUnread(comicApiId,seriesApiId,prevComicId)){
                loadComicFromSeriesScreen(seriesApiId,loadedCount)
            }
        }catch (e:Exception){
            Log.e("markAsUnreadComicInList",e.toString())
        }
    }
}