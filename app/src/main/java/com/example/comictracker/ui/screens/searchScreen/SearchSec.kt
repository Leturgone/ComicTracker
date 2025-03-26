package com.example.comictracker.ui.screens.searchScreen

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


fun getSharedPreferences(context: Context) = context.getSharedPreferences("search_history", Context.MODE_PRIVATE)

fun saveSearchHistory(context: Context, history: List<String>) {
    val preferences = getSharedPreferences(context)
    preferences.edit().putStringSet("search_history", history.toSet()).apply()
}

fun getSearchHistory(context: Context): List<String> {
    val preferences = getSharedPreferences(context)
    return preferences.getStringSet("search_history", emptySet())?.toList() ?: emptyList()
}

fun clearSearchHistory(context: Context) {
    val preferences = getSharedPreferences(context)
    preferences.edit().remove("search_history").apply()
}

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchSec(navController: NavHostController){
    val context = LocalContext.current
    var textFieldState = rememberTextFieldState()
    var searchHistory by rememberSaveable { mutableStateOf<MutableList<String>>(mutableListOf()) }
    var expanded by rememberSaveable { mutableStateOf(false)}
    var debounceJob by remember { mutableStateOf<Job?>(null)}

        LaunchedEffect(Unit) {
        searchHistory = getSearchHistory(context).toMutableStateList()
    }

    // Debounce logic
    LaunchedEffect(textFieldState.text) {
        debounceJob?.cancel()
        debounceJob = launch {
            delay(2000)
            if (textFieldState.text.isNotEmpty()) {
                searchHistory = (listOf(textFieldState.text)+ searchHistory).distinct().take(10).map {it.toString() }.toMutableList()
                saveSearchHistory(context, searchHistory)
                navController.navigate("search_result/${textFieldState.text}")
            }
        }
    }



    searchHistory = remember { getSearchHistory(context) }.toMutableList()

        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Search comics",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold)
            SearchBar(
                inputField = { SearchBarDefaults.InputField(
                    state = textFieldState,
                    shape = RoundedCornerShape(12.dp),
                    onSearch = {expanded = false },
                    expanded = expanded,
                    onExpandedChange = {expanded = it},
                    placeholder = { Text("Search") },
                    leadingIcon = { Icon(Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            searchHistory = (listOf(textFieldState.text)+ searchHistory).distinct().take(10).map {it.toString() }.toMutableList()
                           saveSearchHistory(context,searchHistory)
                            navController.navigate("search_result/${textFieldState.text}")
                        }
                        ) },
                    trailingIcon = { if (textFieldState.text.isNotEmpty()){
                            Icon(Icons.Filled.Cancel,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.clickable {
                                    textFieldState.clearText()
                                    expanded = false
                                }
                            )
                        }
                    }
                )},
                expanded = expanded,
                onExpandedChange = {expanded = it}, modifier = Modifier.padding(bottom = 16.dp)
            ) {
                if (searchHistory.isNotEmpty()) {
                    Text(
                        text = "Search History",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    searchHistory.forEach { historyItem ->
                        Text(
                            text = historyItem,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .clickable {
                                    textFieldState = TextFieldState(historyItem)
                                    navController.navigate("search_result/$historyItem")
                                }
                                .padding(vertical = 4.dp)
                        )
                    }
                    Button(
                        onClick = {
                            clearSearchHistory(context)
                            searchHistory = mutableListOf()
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Clear History")
                    }
                }
            }
    }


}