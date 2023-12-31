package dev.abhaycloud.koderunner.screens.snippet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.abhaycloud.koderunner.data.network.CodeRepository
import dev.abhaycloud.koderunner.di.AppModule
import dev.abhaycloud.koderunner.model.language.LanguageModelItem
import dev.abhaycloud.koderunner.platform.platformName
import dev.abhaycloud.koderunner.screens.code_editor.CodeEditorScreen
import dev.abhaycloud.koderunner.utils.Utils
import dev.abhaycloud.koderunner.viewmodel.SnippetsViewModel

data class SnippetsScreen(
    val selectedPosition: Int,
    val languageList: List<LanguageModelItem>,
    private val appModule: AppModule
) : Screen {
    @OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel {
            SnippetsViewModel(
                CodeRepository(),
                appModule.languageDataSource
            )
        }
        val navigator = LocalNavigator.current
        val snippetsState = viewModel.snippetsResponse.collectAsState()
        var isAnyChipSelected by remember { mutableStateOf(false) }
        var chipSelectedPosition by remember { mutableStateOf(selectedPosition) }
        val languageModelItem = viewModel.languageModelItem.collectAsState()
        val listState = rememberLazyListState()
        val chipListState = rememberLazyListState()


        LaunchedEffect(Unit) {
            if (viewModel.isFirstTime) {
                viewModel.isFirstTime = false
                viewModel.changeLanguage(languageList[selectedPosition], selectedPosition)
            }
            chipSelectedPosition = viewModel.currentSelectedChip.value
            chipListState.animateScrollToItem(chipSelectedPosition)
        }

        when (snippetsState.value) {
            is SnippetUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            is SnippetUiState.Success -> {
//                    Text("${(snippetsState.value as SnippetUiState.Success).data.size} size!!", style = TextStyle(color = Color.Black, fontSize = 24.sp))
                LazyColumn(state = listState, modifier = Modifier.fillMaxSize().padding(bottom = 15.dp)) {

                    items(1) {
                        Spacer(modifier = Modifier.height(15.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                IconButton(
                                    onClick = {
                                        navigator?.pop()
                                    },
                                    modifier = Modifier.padding(horizontal = 16.dp).size(20.dp)
                                ) {
                                    Icon(Icons.Rounded.ArrowBack, "back_arrow")
                                }
                                Text(
                                    "Public Snippets", style = TextStyle(
                                        fontSize = 20.sp
                                    )
                                )
                            }
                            AnimatedVisibility(languageModelItem.value.snippet!!.isNotEmpty()) {
                                IconButton(
                                    onClick = {
                                        navigator?.push(
                                            CodeEditorScreen(
                                                isNew = true,
                                                languageModelItem = languageModelItem.value
                                            )
                                        )
                                    },
                                    modifier = Modifier.padding(horizontal = 16.dp).size(24.dp)
                                ) {
                                    Icon(Icons.Rounded.Add, "add_new")
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                    }
                    stickyHeader {
                        Column {
                            LazyRow(state = chipListState) {
                                items(languageList.size) {
                                    LanguageChip(
                                        it,
                                        10,
                                        chipSelectedPosition,
                                        languageList[it]
                                    ) { position, isChipSelected ->
                                        chipSelectedPosition = position
                                        isAnyChipSelected = isChipSelected
                                        viewModel.changeLanguage(
                                            languageList[it],
                                            position
                                        )
                                    }
                                }
                            }
//                                if(platformName == Utils.platformNameDesktop)
//                                HorizontalScrollbar( modifier = Modifier.fillMaxWidth(), adapter = rememberScrollbarAdapter(chipListState))
                        }
                    }

                    if ((snippetsState.value as SnippetUiState.Success).data.isEmpty()) {
                        item {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Text(
                                    "Snippets not found",
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    } else {

                        if (platformName == Utils.platformNameAndroid || platformName == Utils.platformNameIOS)
                            items((snippetsState.value as SnippetUiState.Success).data) {
//                            Box(modifier = Modifier.fillMaxWidth().onGloballyPositioned {
//                                val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
//                                if (lastVisibleItemIndex != null && lastVisibleItemIndex == currentState.data.size - 1) {
//                                    // Call getSnippetList for pagination when reaching the end of the list
//                                    viewModel.getSnippetList("java")
//                                }
//                            }){
                                SnippetItem(it, false, Modifier) {
                                    navigator?.push(
                                        CodeEditorScreen(
                                            isNew = false,
                                            snippetsItem = it
                                        )
                                    )
                                }
//                            }
                            }

                        if (platformName == Utils.platformNameDesktop)
                            item {
                                FlowRow(
                                    maxItemsInEachRow = 2,
                                    horizontalArrangement = Arrangement.spacedBy(space = 24.dp),
                                    verticalArrangement = Arrangement.spacedBy(space = 24.dp),
                                    modifier = Modifier.padding(24.dp)
                                ) {
                                    repeat((snippetsState.value as SnippetUiState.Success).data.size) {
                                        SnippetItem(
                                            (snippetsState.value as SnippetUiState.Success).data[it],
                                            platformName == Utils.platformNameDesktop,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            navigator?.push(
                                                CodeEditorScreen(
                                                    isNew = false,
                                                    snippetsItem = it
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                    }
//                        items((snippetsState.value as SnippetUiState.Success).data.size) {
//                            SnippetItem((snippetsState.value as SnippetUiState.Success).data[it])
//                        }

                }
            }

            is SnippetUiState.Error -> {
                Text(
                    "Some error occurred: ${(snippetsState.value as SnippetUiState.Error).message}",
                    style = TextStyle(color = Color.Black, fontSize = 24.sp)
                )
            }
        }


//        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
//            when(snippetsState.value){
//                is SnippetUiState.Loading -> {
//                    CircularProgressIndicator()
//                }
//                is SnippetUiState.Success -> {
//                    Text("${(snippetsState.value as SnippetUiState.Success).data.size} size!!", style = TextStyle(color = Color.Black, fontSize = 24.sp))
//                }
//                is SnippetUiState.Error -> {
//                    Text("Some error occurred: ${(snippetsState.value as SnippetUiState.Error).message}", style = TextStyle(color = Color.Black, fontSize = 24.sp))
//                }
//            }
//        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageChip(
    position: Int,
    size: Int,
    selectedPosition: Int,
    languageModelItem: LanguageModelItem,
    onClick: (Int, Boolean) -> Unit
) {
    var isClicked by remember { mutableStateOf(false) }
    Card(
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selectedPosition == position) Color(0xffE8E2F6) else Color.White
        ),
        border = BorderStroke(1.dp, color = Color(0xffD8E0E9)),
        modifier = Modifier.padding(
            start = if (position == 0) 16.dp else 4.dp,
            end = if (size - 1 <= position) 16.dp else 4.dp
        ),
        onClick = {
            isClicked = !isClicked
            onClick.invoke(position, isClicked)
        }
    ) {
        Text(
            languageModelItem.name, style = TextStyle(
                fontSize = 16.sp,
                color = Color.Black
            ),
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)
        )
    }
}