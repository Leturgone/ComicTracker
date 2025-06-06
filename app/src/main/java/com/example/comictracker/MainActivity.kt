package com.example.comictracker

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.comictracker.presentation.ui.screens.AppNavigation
import com.example.comictracker.presentation.ui.screens.BottomNavigationBar
import com.example.comictracker.presentation.ui.theme.ComicTrackerTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Панель прозрачная
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        //ЧТобы приложение могло рисовать фон системных панелей
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        //Делаем цвет иконок нав пнели на светлые
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

        //Чтобы контент рисовался за пределами основного экрана
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        WindowCompat.setDecorFitsSystemWindows(window, false)


        setContent {
            ComicTrackerTheme {
                SetBarColor(color = MaterialTheme.colorScheme.background)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen(){
    val navController = rememberNavController()
    //Получение текущего состояния экрана
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val excludedRoutes = setOf("reg","login", "dialog_cans", "dialog_money")
    Scaffold(
        bottomBar = {
            if (currentRoute !in excludedRoutes) {
                BottomNavigationBar(navController)
            }
        }
    ) {
            innerPadding ->
        AppNavigation(innerPadding = innerPadding, navController = navController)

    }
}

@Composable
private fun SetBarColor(color : Color){
    //Функция для изменения цвета статусбара
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(color = color)
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComicTrackerTheme {
    }
}