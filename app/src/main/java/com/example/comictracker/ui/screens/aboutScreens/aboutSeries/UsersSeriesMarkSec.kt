package com.example.comictracker.ui.screens.aboutScreens.aboutSeries

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.comictracker.mvi.ComicAppIntent
import com.example.comictracker.ui.components.MarkCategory
import com.example.comictracker.viewmodel.ComicViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersSeriesMarkSec(seriesId: Int, mark:String,favoriteMark:Boolean,viewModel: ComicViewModel = hiltViewModel()) {
    val markCategories = listOf(
        MarkCategory(Icons.Filled.AccessTime,"Will be read","will"),
        MarkCategory(Icons.Filled.BookmarkAdded,"Read","read"),
        MarkCategory(Icons.Filled.Bookmark,"Currently reading","currently"),
        MarkCategory(Icons.Filled.BookmarkRemove,"Unread","unread"),

    )
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    Box {

        Card(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = Color.Gray,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContentColor = Color.Gray
            )) {
            Box(
                Modifier
                    .padding(10.dp)
                    .clickable {
                        showBottomSheet = true
                    }) {
                Text(
                    text = when(mark){
                        markCategories[0].markBD -> markCategories[0].title
                        markCategories[1].markBD -> markCategories[1].title
                        markCategories[2].markBD -> markCategories[2].title
                        markCategories[3].markBD -> markCategories[3].title
                        else -> {"error"}
                    },
                    fontSize = 17.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Normal,
                )
                Icon(imageVector = Icons.Filled.NavigateNext,
                    contentDescription = "NextIcon",
                    Modifier.padding(start = 310.dp))
            }
        }
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.CenterEnd) {
            IconButton(onClick = {
                when(favoriteMark){
                    true -> viewModel.processIntent(ComicAppIntent.RemoveSeriesFromFavorite(seriesId))
                    false -> viewModel.processIntent(ComicAppIntent.AddSeriesToFavorite(seriesId))
                }
            }) {
                Icon(imageVector = Icons.Filled.Star,
                    tint = when(favoriteMark){
                        true-> Color.Yellow
                        false -> Color.LightGray},
                    contentDescription = "favoriteButton")
            }
        }
    }


    if (showBottomSheet) {
        ModalBottomSheet(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            Column(Modifier.fillMaxWidth(),Arrangement.SpaceBetween,Alignment.CenterHorizontally) {
                Text(
                    text = "Choose Category",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(200.dp)
                )
                LazyRow(Modifier.fillMaxWidth(), userScrollEnabled = true, horizontalArrangement = Arrangement.SpaceAround){
                    items(markCategories.size){
                        val markCategory = markCategories[it]
                        Column(
                            Modifier
                                .weight(1f)
                                .clickable {
                                    when (markCategory.markBD) {
                                        "read" -> viewModel.processIntent(
                                            ComicAppIntent.MarkAsReadSeries(
                                                seriesId
                                            )
                                        )

                                        "will" -> viewModel.processIntent(
                                            ComicAppIntent.MarkAsWillBeReadSeries(
                                                seriesId
                                            )
                                        )

                                        "currently" -> viewModel.processIntent(
                                            ComicAppIntent.MarkAsCurrentlyReadingSeries(
                                                seriesId
                                            )
                                        )

                                        "unread" -> viewModel.processIntent(
                                            ComicAppIntent.MarkAsUnreadSeries(
                                                seriesId
                                            )
                                        )
                                    }

                                    scope
                                        .launch { sheetState.hide() }
                                        .invokeOnCompletion {
                                            if (!sheetState.isVisible) {
                                                showBottomSheet = false
                                            }
                                        }
                                }, horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(imageVector = markCategory.icon,
                                contentDescription ="${markCategory.title} icon",
                                modifier = Modifier.size(40.dp),
                                tint = MaterialTheme.colorScheme.primary)
                            Text(
                                text = markCategory.title,
                                fontSize =10.sp,
                                modifier = Modifier.width(50.dp),
                                color = Color.White,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(100.dp))
            }

        }
    }
}
