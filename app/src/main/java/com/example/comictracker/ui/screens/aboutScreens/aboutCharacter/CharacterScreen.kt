package com.example.comictracker.ui.screens.aboutScreens.aboutCharacter

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.comictracker.mvi.ComicAppIntent
import com.example.comictracker.mvi.ComicAppState
import com.example.comictracker.mvi.DataState
import com.example.comictracker.ui.screens.AllComicScreen
import com.example.comictracker.viewmodel.ComicViewModel


@Composable
fun CharacterScreen(
    characterId: Int,
    navController: NavHostController,
    viewModel: ComicViewModel = hiltViewModel()
    ){
    val uiState by viewModel.state.collectAsState()


    LaunchedEffect(key1 = characterId) {
        viewModel.processIntent(ComicAppIntent.LoadCharacterScreen(characterId))
    }





    Column() {
        uiState.let {state ->
            when(state){
                is ComicAppState.AboutCharacterScreenState ->{
                    when(state.character){
                        is DataState.Error -> TODO()
                        DataState.Loading -> CircularProgressIndicator()
                        is DataState.Success -> CharacterSec(state.character.result)
                    }
                    when(state.series){
                        is DataState.Error -> TODO()
                        DataState.Loading -> CircularProgressIndicator()
                        is DataState.Success -> AllComicScreen(
                            state.series.result,
                            navController)
                    }
                }
                else -> {CircularProgressIndicator()}
            }
        }


    }
}