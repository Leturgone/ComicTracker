package com.example.comictracker.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.LibraryBooks
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.comictracker.presentation.ui.components.BottomNavigation
import com.example.comictracker.presentation.ui.theme.LightGreen

@Composable
fun BottomNavigationBar(navController: NavHostController){

    val items = listOf(
        BottomNavigation(
            route = "home",
            icon = Icons.Rounded.Home
        ),
        BottomNavigation(
            route = "search",
            icon = Icons.Rounded.Search
        ),
        BottomNavigation(
            route = "library" ,
            icon = Icons.AutoMirrored.Rounded.LibraryBooks
        ),
    )
    Column {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = LightGreen
        )
        NavigationBar {

            //Отслеживание текушего маршрута
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route

            Row(modifier = Modifier.background((MaterialTheme.colorScheme.background)))
            {
                items.forEach{ item->
                    NavigationBarItem(selected = currentRoute == item.route, modifier = Modifier.semantics
                    { contentDescription = item.route },
                        onClick = {
                            navController.popBackStack()
                            navController.navigate(item.route)
                        },
                        icon = {
                            Icon(imageVector = item.icon,
                                contentDescription =item.route,
                                tint = MaterialTheme.colorScheme.primary)
                        },)
                }
            }
        }
    }

}