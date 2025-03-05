package com.example.comictracker.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comictracker.domain.model.CharacterModel
import com.example.comictracker.domain.model.SeriesModel
import com.example.comictracker.domain.repository.RemoteComicRepository
import com.example.comictracker.mvi.AboutComicScreenData
import com.example.comictracker.mvi.AboutSeriesScreenData
import com.example.comictracker.mvi.ComicAppIntent
import com.example.comictracker.mvi.ComicAppState
import com.example.comictracker.mvi.DataState
import com.example.comictracker.mvi.SearchResultScreenData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class ComicViewModel @Inject constructor(
    private val remoteComicRepository: RemoteComicRepository):ViewModel(){

    // Начальное состояние
    private val _state = MutableStateFlow<ComicAppState>(ComicAppState.HomeScreenState())

    val state: StateFlow<ComicAppState> = _state

    fun processIntent(intent: ComicAppIntent){
        when(intent){
            is ComicAppIntent.LoadCharacterScreen -> loadCharacterScreen(intent.characterId)
            is ComicAppIntent.LoadComicScreen -> loadComicScreen(intent.comicId)
            ComicAppIntent.LoadHomeScreen -> loadHomeScreen()
            ComicAppIntent.LoadProfileScreen -> loadProfileScreen()
            ComicAppIntent.LoadSearchScreen -> loadSearchScreen()
            is ComicAppIntent.LoadSeriesScreen -> loadSeriesScreen(intent.seriesId)
            is ComicAppIntent.MarkAsCurrentlyReadingSeries -> TODO()
            is ComicAppIntent.MarkAsReadComic -> TODO()
            is ComicAppIntent.MarkAsReadSeries -> TODO()
            is ComicAppIntent.MarkAsUnreadComic -> TODO()
            is ComicAppIntent.MarkAsUnreadSeries -> TODO()
            is ComicAppIntent.MarkAsWillBeReadSeries -> TODO()
            is ComicAppIntent.Search -> loadSearchResultsScreen(intent.query)
            is ComicAppIntent.LoadComicFromSeriesScreen -> loadComicFromSeriesScreen(intent.seriesId)
        }

    }

    private fun loadSearchResultsScreen(query: String) = viewModelScope.launch{
        _state.value = ComicAppState.SearchResultScreenSate(DataState.Loading)
        val searchSeriesListDeferred = async(Dispatchers.IO) {
            try {
                DataState.Success(remoteComicRepository.getSeriesByTitle(query))
            }catch (e:Exception){
                DataState.Error("$e")
            }

        }

        val searchCharacterListDeferred = async(Dispatchers.IO) {
            try {
                DataState.Success(remoteComicRepository.getCharactersByName(query))
            }catch (e:Exception){
                DataState.Error("$e")
            }

        }

        val seriesList = searchSeriesListDeferred.await()
        val characterList = searchCharacterListDeferred.await()

        _state.value = ComicAppState.SearchResultScreenSate(characterList,seriesList)
    }

    private fun loadComicFromSeriesScreen(seriesId: Int)  = viewModelScope.launch{
        _state.value = ComicAppState.AllComicSeriesScreenState(DataState.Loading)
        try {
            withContext(Dispatchers.IO){
                _state.emit(
                    ComicAppState.AllComicSeriesScreenState(
                        DataState.Success(remoteComicRepository.getComicsFromSeries(seriesId))
                    )
                )
            }
        }catch (e:Exception){
            _state.value = ComicAppState.AllComicSeriesScreenState(
                DataState.Error("Error loading comic from this series : $e"))
        }

    }

    private fun loadHomeScreen() {
        TODO("Not yet implemented")
    }

    private fun loadProfileScreen() {
        TODO("Not yet implemented")
    }

    private fun loadSearchScreen() = viewModelScope.launch {
        _state.value = ComicAppState.SearchScreenState(DataState.Loading)
        try {
            val discoverSeriesListDef  = async(Dispatchers.IO) {
                remoteComicRepository.getAllSeries()
            }
            val mayLikeSeriesListDef  = async(Dispatchers.IO) {
                //Получение из базы id серий и id персонажей
                remoteComicRepository.getAllSeries()
            }
            val characterListDef = async(Dispatchers.IO) {
                remoteComicRepository.getAllCharacters()
            }
            val dseries = discoverSeriesListDef.await()
            val mlsreis = mayLikeSeriesListDef.await()
            val characters = characterListDef.await()

            _state.value = ComicAppState.SearchScreenState(
                DataState.Success(mlsreis),DataState.Success(dseries),DataState.Success(characters)
            )
        }catch (e:Exception){
            withContext(Dispatchers.Main){
                _state.value = ComicAppState.SearchScreenState(
                    DataState.Error("Error loading : $e"),
                    DataState.Error("Error loading  : $e"),
                    DataState.Error("Error loading  : $e")
                )
            }
        }
    }

    private fun loadSeriesScreen(seriesId: Int)  = viewModelScope.launch {
        _state.value = ComicAppState.AboutComicScreenState(DataState.Loading)

        try {
            val seriesDeferred = async(Dispatchers.IO){
                remoteComicRepository.getSeriesById(seriesId.toString())
            }

            val comicListDeferred = async(Dispatchers.IO) {
                remoteComicRepository.getComicsFromSeries(seriesId)
            }

            val characterListDeferred = async(Dispatchers.IO) {
                remoteComicRepository.getSeriesCharacters(seriesId)
            }

            val series = seriesDeferred.await() // Получение series до зависимых задач.

            val creatorListDeferred = async(Dispatchers.IO) {
                remoteComicRepository.getSeriesCreators(series.creators ?: emptyList())
            }

            val connectedSeriesListDeferred = async(Dispatchers.IO) {
                remoteComicRepository.getConnectedSeries(series.connectedSeries)
            }

            val comicList = comicListDeferred.await()
            val characterList = characterListDeferred.await()
            val creatorList = creatorListDeferred.await()
            val connectedSeriesList = connectedSeriesListDeferred.await()

            _state.value = (ComicAppState.AboutSeriesScreenState(
                DataState.Success(AboutSeriesScreenData(
                    series = series,
                    comicList = comicList,
                    creatorList = creatorList,
                    characterList = characterList,
                    connectedSeriesList = connectedSeriesList
                ))
            ))
        }catch (e:Exception){
            withContext(Dispatchers.Main){
                _state.value = ComicAppState.AboutCharacterScreenState(
                    DataState.Error("Error loading this series : $e")
                )
            }
        }
    }

    private fun loadComicScreen(comicId: Int) = viewModelScope.launch {
        _state.value = ComicAppState.AboutComicScreenState(DataState.Loading)
        try {
            val comicDeferred = async(Dispatchers.IO){
                remoteComicRepository.getComicById(comicId)
            }


            val characterListDeferred = async(Dispatchers.IO) {
                remoteComicRepository.getComicCharacters(comicId)
            }

            val comic = comicDeferred.await() // Получение series до зависимых задач.

            val creatorListDeferred = async(Dispatchers.IO) {
                remoteComicRepository.getComicCreators(comic.creators ?: emptyList())
            }

            val characterList = characterListDeferred.await()
            val creatorList = creatorListDeferred.await()

            _state.value = (ComicAppState.AboutComicScreenState(
                DataState.Success(AboutComicScreenData(
                    comic = comic, creatorList = creatorList, characterList = characterList
                ))
            ))
        }catch (e:Exception){
            withContext(Dispatchers.Main){
                _state.value = ComicAppState.AboutCharacterScreenState(
                    DataState.Error("Error loading this series : $e")
                )
            }
        }
    }

    private fun loadCharacterScreen(characterId:Int) = viewModelScope.launch {
        _state.value = ComicAppState.AboutCharacterScreenState(
            character = DataState.Loading,
            series = DataState.Loading
        )
        try {

            val characterDef = async(Dispatchers.IO) {
                DataState.Success(remoteComicRepository.getCharacterById(characterId))
            }

            val seriesDef = async(Dispatchers.IO) {
                DataState.Success(remoteComicRepository.getCharacterSeries(characterId))
            }

            val character = characterDef.await()

            _state.value = ComicAppState.AboutCharacterScreenState(
                character =  character,
                series = DataState.Loading
            )

            _state.value = ComicAppState.AboutCharacterScreenState(
                character =  character,
                series = seriesDef.await()
            )
        }catch (e:Exception){
            withContext(Dispatchers.Main){
                _state.value = ComicAppState.AboutCharacterScreenState(
                    character = DataState.Error("Error loading character"),
                    series = DataState.Error("Error loading comics with this character")
                )
            }
        }

    }
}