package com.example.comictracker

import com.example.comictracker.domain.model.CharacterModel
import com.example.comictracker.domain.model.ComicModel
import com.example.comictracker.domain.model.SeriesModel
import com.example.comictracker.domain.repository.local.LocalReadRepository
import com.example.comictracker.domain.repository.remote.RemoteCharacterRepository
import com.example.comictracker.domain.repository.remote.RemoteComicsRepository
import com.example.comictracker.domain.repository.remote.RemoteCreatorsRepository
import com.example.comictracker.domain.repository.remote.RemoteSeriesRepository
import org.mockito.Mockito

class MockHelper(
    private val remoteSeriesRepository: RemoteSeriesRepository? = null,
    private val remoteComicsRepository: RemoteComicsRepository? = null,
    private val remoteCharacterRepository: RemoteCharacterRepository? = null,
    private val remoteCreatorsRepository: RemoteCreatorsRepository? = null,
    private val localReadRepository: LocalReadRepository? = null
){
    suspend fun mockSeriesSetUp(series: SeriesModel){
        remoteSeriesRepository?.let {
            Mockito.`when`(
                it.getSeriesById(series.seriesId)
            ).thenReturn(Result.success(series))

            Mockito.`when`(
                it.getConnectedSeries(series.connectedSeries)
            ).thenReturn(Result.failure(Exception()))
        }

        remoteComicsRepository?.let {
            Mockito.`when`(
                it.getComicsFromSeries(series.seriesId)
            ).thenReturn(Result.failure(Exception()))
        }

        remoteCharacterRepository?.let {
            Mockito.`when`(
                it.getSeriesCharacters(series.seriesId)
            ).thenReturn(Result.failure(Exception()))
        }

        remoteCreatorsRepository?.let {
            Mockito.`when`(
                it.getSeriesCreators(series.creators!!)
            ).thenReturn(Result.success(emptyList()))
        }

        localReadRepository?.let {
            Mockito.`when`(
                it.loadSeriesMark(series.seriesId)
            ).thenReturn(Result.success("unread"))

            Mockito.`when`(
                it.loadSeriesFavoriteMark(series.seriesId)
            ).thenReturn(Result.success(false))

            Mockito.`when`(
                it.loadNextRead(series.seriesId)
            ).thenReturn(Result.success(null))
        }
    }


    suspend fun mockHomeScreenSetUp(){
        localReadRepository?.let {
            Mockito.`when`(
                localReadRepository.loadCurrentReadIds(0)
            ).thenReturn(Result.success(listOf(1, 2, 3)))

            Mockito.`when`(
                localReadRepository.loadNextReadComicIds(0)
            ).thenReturn(Result.success(listOf(1, 2, 3)))
        }

        remoteComicsRepository?.let {
            Mockito.`when`(
                remoteComicsRepository.fetchUpdatesForSeries(listOf(1, 2, 3))
            ).thenReturn(Result.success(listOf(comicExample)))

            Mockito.`when`(
                remoteComicsRepository.fetchComics(listOf(1, 2, 3))
            ).thenReturn(Result.success(listOf(secondComicExample)))
        }

    }


    suspend fun mockCharacterScreen(characterExample:CharacterModel,list: List<SeriesModel>){

        remoteCharacterRepository?.let {
            Mockito.`when`(
                it.getCharacterById(characterExample.characterId)
            ).thenReturn(Result.success(characterExample))
        }

        remoteSeriesRepository?.let {
            Mockito.`when`(
                it.getCharacterSeries(characterExample.characterId)
            ).thenReturn(Result.success(list))
        }


    }


    suspend fun mockComicScreenSetup(comic: ComicModel,mark:String){

        remoteComicsRepository?.let {
            Mockito.`when`(
                it.getComicById(comic.comicId)
            ).thenReturn(Result.success(comic))
        }

        remoteCharacterRepository?.let {
            Mockito.`when`(
                it.getComicCharacters(comic.comicId)
            ).thenReturn(Result.success(listOf(characterExample)))
        }

        remoteCreatorsRepository?.let {
            Mockito.`when`(
                it.getComicCreators(comic.creators)
            ).thenReturn(Result.success(listOf(creatorExample)))
        }

        localReadRepository?.let {
            Mockito.`when`(
                localReadRepository.loadComicMark(comic.comicId)
            ).thenReturn(Result.success(mark))
        }

    }


    suspend fun mockSearchScreenSetup(){


        val mayLikeSeriesList = listOf(secondSeriesExample)

        val discoverSeriesList = listOf(seriesExample)

        val characterList = listOf(
            characterExample,
            characterExample.copy(name = "ch1"),
            characterExample.copy(name = "ch2"),
            characterExample.copy(name = "ch3")
        )


        remoteSeriesRepository?.let {
            //Discover list mock
            Mockito.`when`(
                it.getAllSeries()
            ).thenReturn(Result.success(discoverSeriesList))

            Mockito.`when`(
                it.loadMayLikeSeriesIds(listOf(1,2,3))
            ).thenReturn(Result.success(listOf(1)))

            Mockito.`when`(
                it.fetchSeries(listOf(1))
            ).thenReturn(Result.success(mayLikeSeriesList))

        }

        localReadRepository?.let {
            //MayLike list mock
            Mockito.`when`(
                it.loadAllReadSeriesIds(0)
            ).thenReturn(Result.success(listOf(1,2,3)))
        }


        remoteCharacterRepository?.let {
            //Character list mock
            Mockito.`when`(
                it.getAllCharacters()
            ).thenReturn(Result.success(characterList))
        }

    }


    suspend fun mockSeriesSetUpForSeriesTest(series: SeriesModel,mark: String,favorite:Boolean,nextRead:ComicModel){
        remoteSeriesRepository?.let {
            Mockito.`when`(
                it.getSeriesById(series.seriesId)
            ).thenReturn(Result.success(series))

            Mockito.`when`(
                it.getConnectedSeries(series.connectedSeries)
            ).thenReturn(Result.success(listOf(secondSeriesExample)))
        }

        remoteComicsRepository?.let {
            Mockito.`when`(
                it.getComicsFromSeries(series.seriesId)
            ).thenReturn(Result.success(listOf(comicExample, secondComicExample)))
            Mockito.`when`(
                it.getComicById(comicExample.comicId)
            ).thenReturn(Result.success(nextRead))
        }

        remoteCharacterRepository?.let {
            Mockito.`when`(
                it.getSeriesCharacters(series.seriesId)
            ).thenReturn(Result.success(listOf(characterExample)))
        }

        remoteCreatorsRepository?.let {
            Mockito.`when`(
                it.getSeriesCreators(series.creators!!)
            ).thenReturn(Result.success(listOf(creatorExample)))
        }

        localReadRepository?.let {
            Mockito.`when`(
                it.loadSeriesMark(series.seriesId)
            ).thenReturn(Result.success(mark))

            Mockito.`when`(
                it.loadSeriesFavoriteMark(series.seriesId)
            ).thenReturn(Result.success(favorite))

            Mockito.`when`(
                it.loadNextRead(series.seriesId)
            ).thenReturn(Result.success(comicExample.comicId))
        }
    }


    suspend fun mockAllComicsFromSeriesScreenSetup(seriesExample: SeriesModel, comicList:List<ComicModel>,mark: String = "read"){
        remoteComicsRepository?.let {
            Mockito.`when`(
                it.getComicsFromSeries(seriesExample.seriesId,0)
            ).thenReturn(
                Result.success(comicList)
            )
        }

        localReadRepository?.let { rep ->
            comicList.forEach { comic ->
                Mockito.`when`(
                    rep.loadComicMark(comic.comicId)
                ).thenReturn(Result.success(mark))
            }
        }
    }
}