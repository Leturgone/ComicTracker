package com.example.comictracker.data.api

import com.example.comictracker.data.api.dto.charactersDTO.CharactersDTO
import com.example.comictracker.data.api.dto.comicsDTO.ComicsDTO
import com.example.comictracker.data.api.dto.creatorsDTO.CreatorsDTO
import com.example.comictracker.data.api.dto.seriesDTO.SeriesDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelComicApi {

    @GET("/v1/public/comics")
    suspend fun getSeriesLastReleasesById(
        @Query("format")format:String = "comic",
        @Query("formatType")formatType:String = "comic",
        @Query("noVariants")noVariants:String = "true",
        @Query("dateDescriptor")dateDescriptor:String = "thisMonth",
        @Query("series")series:String,
        @Query("limit")limit:String = "9",

        @Query("apikey")apikey:String=API_KEY,
        @Query("ts")ts:String=timeStamp,
        @Query("hash")hash:String=hash()
    ):ComicsDTO

    @GET("/v1/public/series/{seriesId}/comics")
    suspend fun getSpecificComicsFromSeries(
        @Path("seriesId")seriesId:String,
        @Query("format")format:String = "comic",
        @Query("formatType")formatType:String = "comic",
        @Query("noVariants")noVariants:String = "true",
        @Query("issueNumber")issueNumber:String,
        @Query("orderBy")orderBy:String = "issueNumber",
        @Query("limit")limit:String = "1",
        @Query("offset")offset:String,
        @Query("apikey")apikey:String=API_KEY,
        @Query("ts")ts:String=timeStamp,
        @Query("hash")hash:String=hash()
    ):ComicsDTO

    @GET("/v1/public/characters/{characterId}/series")
    suspend fun getCharacterSeries(
        @Path("characterId")characterId:String,
        @Query("contains")contains:String = "comic",
        @Query("orderBy")orderBy:String = "startYear",
        @Query("limit")limit:String = "9",
        @Query("offset")offset:String,
        @Query("apikey")apikey:String=API_KEY,
        @Query("ts")ts:String=timeStamp,
        @Query("hash")hash:String=hash()
    ):SeriesDTO

    @GET("/v1/public/series")
    suspend fun getAllSeries(
        @Query("orderBy")orderBy:String = "startYear",
        @Query("limit")limit:String = "9",
        @Query("offset")offset:String,
        @Query("apikey")apikey:String=API_KEY,
        @Query("ts")ts:String=timeStamp,
        @Query("hash")hash:String=hash()

    ):SeriesDTO

    @GET("/v1/public/characters")
    suspend fun getAllCharacters(
        @Query("limit")limit:String = "9",
        @Query("offset")offset:String,
        @Query("apikey")apikey:String=API_KEY,
        @Query("ts")ts:String=timeStamp,
        @Query("hash")hash:String=hash(),
    ):CharactersDTO

    @GET("/v1/public/series")
    suspend fun getSeriesByTitle(
        @Query("titleStartsWith")titleStartsWith:String,
        @Query("contains")contains:String = "comic",
        @Query("orderBy")orderBy:String = "startYear",
        @Query("limit")limit:String = "10",
        @Query("apikey")apikey:String=API_KEY,
        @Query("ts")ts:String=timeStamp,
        @Query("hash")hash:String=hash()
    ):SeriesDTO

    @GET("/v1/public/characters")
    suspend fun getCharactersByName(
        @Query("nameStartsWith")nameStartsWith:String,
        @Query("orderBy")orderBy:String = "name",
        @Query("limit")limit:String = "10",

        @Query("apikey")apikey:String=API_KEY,
        @Query("ts")ts:String=timeStamp,
        @Query("hash")hash:String=hash()
    ):CharactersDTO


    @GET("/v1/public/series/{seriesId}")
    suspend fun getSeriesById(
        @Path("seriesId")seriesId:String,
        @Query("apikey")apikey:String=API_KEY,
        @Query("ts")ts:String=timeStamp,
        @Query("hash")hash:String=hash()

    ):SeriesDTO

    @GET("/v1/public/series/{seriesId}/characters")
    suspend fun getSeriesCharacters(
        @Path("seriesId")seriesId:String,
        @Query("limit")limit:String = "20",
        @Query("apikey")apikey:String=API_KEY,
        @Query("ts")ts:String=timeStamp,
        @Query("hash")hash:String=hash()
    ):CharactersDTO

    @GET("/v1/public/series/{seriesId}/comics")
    suspend fun getComicsFromSeries(
        @Path("seriesId")seriesId:String,
        @Query("format")format:String = "comic",
        @Query("formatType")formatType:String = "comic",
        @Query("noVariants")noVariants:String = "true",
        @Query("orderBy")orderBy:String = "issueNumber",
        @Query("limit")limit:String = "50",
        @Query("offset")offset:String,
        @Query("apikey")apikey:String=API_KEY,
        @Query("ts")ts:String=timeStamp,
        @Query("hash")hash:String=hash()
    ):ComicsDTO


    @GET("/v1/public/comics/{comicId}")
    suspend fun getComicById(
        @Path("comicId")comicId:String,
        @Query("apikey")apikey:String=API_KEY,
        @Query("ts")ts:String=timeStamp,
        @Query("hash")hash:String=hash()
    ):ComicsDTO


    @GET("/v1/public/comics/{comicId}/characters")
    suspend fun getComicCharacters(
        @Path("comicId")comicId:String,
        @Query("limit")limit:String = "20",
        @Query("apikey")apikey:String=API_KEY,
        @Query("ts")ts:String=timeStamp,
        @Query("hash")hash:String=hash()
    ):CharactersDTO


    @GET("/v1/public/characters/{characterId}")
    suspend fun getCharacterById(
        @Path("characterId")characterId:String,
        @Query("apikey")apikey:String=API_KEY,
        @Query("ts")ts:String=timeStamp,
        @Query("hash")hash:String=hash()
    ):CharactersDTO

    @GET("/v1/public/creators/{creatorId}")
    suspend fun getCreatorById(
        @Path("creatorId")creatorId:String,
        @Query("apikey")apikey:String=API_KEY,
        @Query("ts")ts:String=timeStamp,
        @Query("hash")hash:String=hash()
    ):CreatorsDTO


}