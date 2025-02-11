package com.example.comictracker

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.comictracker.homeScreen.HomeScreen
import com.example.comictracker.libaryScreen.LibraryScreen
import com.example.comictracker.searchScreen.SearchScreen


@Composable
fun AppNavigation(innerPadding: PaddingValues, navController: NavHostController){

    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.padding(innerPadding)
    ) {
        composable("home"){ HomeScreen()}
        composable("search"){ SearchScreen()}
        composable("library"){ LibraryScreen()}
    }
}