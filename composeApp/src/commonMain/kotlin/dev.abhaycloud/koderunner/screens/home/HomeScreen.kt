package dev.abhaycloud.koderunner.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.abhaycloud.koderunner.data.network.CodeRepository
import dev.abhaycloud.koderunner.di.AppModule
import dev.abhaycloud.koderunner.platform.platformName
import dev.abhaycloud.koderunner.screens.snippet.SnippetsScreen
import dev.abhaycloud.koderunner.utils.Utils
import dev.abhaycloud.koderunner.utils.Utils.printLogs
import dev.abhaycloud.koderunner.utils.screenSize
import dev.abhaycloud.koderunner.viewmodel.HomeScreenViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource


class HomeScreen(private val appModule: AppModule) : Screen {
    @OptIn(
        ExperimentalResourceApi::class, ExperimentalLayoutApi::class,
        ExperimentalFoundationApi::class
    )
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewModel = rememberScreenModel {
            HomeScreenViewModel(
                CodeRepository(),
                appModule.languageDataSource
            )
        }
        val languageUiState = viewModel.languageListResponse.collectAsState()
        val availableLanguagesList = viewModel.availableLanguagesList.collectAsState()
        val allLanguagesList = viewModel.allLanguagesList.collectAsState()
        val searchText = remember { mutableStateOf("") }
        val searchTextFocusManager = LocalFocusManager.current
        val width = if (platformName == Utils.platformNameDesktop) {
            screenSize().width / 5 - 36
        } else {
            screenSize().width / 2 - 32
        }
        printLogs("${screenSize().width} ${screenSize().height}")


        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = if (platformName == Utils.platformNameDesktop) 24.dp else 16.dp)
        ) {
            items(1) {
                Spacer(modifier = Modifier.height(30.dp))
                Image(
                    painterResource("app_logo.png"),
                    modifier = Modifier.fillMaxWidth().size(width = 100.dp, height = 32.dp),
                    contentScale = ContentScale.Fit,
                    contentDescription = "app_logo",
                )
                Spacer(modifier = Modifier.height(30.dp))
            }
            stickyHeader {
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = searchText.value,
                    onValueChange = {
                        searchText.value = it
                        viewModel.searchLanguage(searchText.value.trim())
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                    keyboardActions = KeyboardActions {
                        searchTextFocusManager.clearFocus()
                    },
                    modifier = Modifier.padding(horizontal = 3.dp).fillMaxWidth()
                        .shadow(elevation = 5.dp, shape = RoundedCornerShape(10.dp)),
                    placeholder = {
                        Text(
                            "Search languages", style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W400,
                                color = Color(0x80000000)
                            )
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color(0x80000000)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                    ),
                    shape = RoundedCornerShape(10.dp),
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            item {
                Text(
                    "Coding Simplified: Choose a Language, Run Your Code.",
                    style = TextStyle(
                        fontSize = if (platformName == Utils.platformNameDesktop) 18.sp else 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.W500
                    ),
                    modifier = Modifier.padding(horizontal = 3.dp).padding(top = 24.dp),
                )

                when (languageUiState.value) {
                    is LanguageUiState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    }

                    is LanguageUiState.Success -> {
                        FlowRow(
                            maxItemsInEachRow = if (platformName == Utils.platformNameDesktop) 5 else 2,
                            horizontalArrangement = Arrangement.spacedBy(space = if (platformName == Utils.platformNameDesktop) 24.dp else 16.dp),
                            verticalArrangement = Arrangement.spacedBy(space = if (platformName == Utils.platformNameDesktop) 24.dp else 16.dp),
                            modifier = Modifier.padding(if (platformName == Utils.platformNameDesktop) 12.dp else 8.dp)
                        ) {
                            repeat(availableLanguagesList.value.size) {
                                Box(
                                    modifier = Modifier.height(150.dp)
                                        .width(width.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.White)
                                        .border(
                                            border = BorderStroke(1.dp, color = Color(0xffD8E0E9)),
                                            shape = RoundedCornerShape(12.dp)
                                        ).clickable {
                                            navigator?.push(
                                                SnippetsScreen(
                                                    allLanguagesList.value.indexOfFirst { element -> element.name == availableLanguagesList.value[it].name },
                                                    allLanguagesList.value,
                                                    appModule
                                                )
                                            )
                                        }
                                ) {
                                    Image(
                                        painterResource("languages/${availableLanguagesList.value[it].name.lowercase()}.png"),
                                        modifier = Modifier.size(80.dp)
                                            .align(Alignment.Center),
                                        contentDescription = "language"
                                    )
//                                    Text(availableLanguagesList.value[it].name, modifier = Modifier.padding(top = 4.dp))
                                }
                            }
                        }
                    }

                    is LanguageUiState.Error -> {
                        Text(
                            "Some error occurred: ${(languageUiState.value as LanguageUiState.Error).message}",
                            style = TextStyle(color = Color.Black, fontSize = 24.sp)
                        )
                    }
                }
            }
        }
    }

//        Column(
//            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp).padding(top = 30.dp)
//                .verticalScroll(
//                    rememberScrollState()
//                ),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Image(
//                painterResource("app_logo.png"),
//                modifier = Modifier.fillMaxWidth().size(width = 100.dp, height = 32.dp),
//                contentScale = ContentScale.Fit,
//                contentDescription = "app_logo"
//            )
//
//            OutlinedTextField(
//                value = searchText.value,
//                onValueChange = {
//                    searchText.value = it
//                },
//                singleLine = true,
//                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
//                keyboardActions = KeyboardActions {
//                    searchTextFocusManager.clearFocus()
//                },
//                modifier = Modifier.padding(horizontal = 3.dp).fillMaxWidth().padding(top = 34.dp)
//                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(10.dp)),
//                placeholder = {
//                    Text(
//                        "Search languages", style = TextStyle(
//                            fontSize = 14.sp,
//                            fontWeight = FontWeight.W400,
//                            color = Color(0x80000000)
//                        )
//                    )
//                },
//                trailingIcon = {
//                    Icon(
//                        imageVector = Icons.Default.Search,
//                        contentDescription = null,
//                        tint = Color(0x80000000)
//                    )
//                },
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedBorderColor = Color.White,
//                    unfocusedBorderColor = Color.White,
//                    focusedContainerColor = Color.White,
//                    unfocusedContainerColor = Color.White,
//                ),
//                shape = RoundedCornerShape(10.dp),
//            )
//            Text(
//                "Coding Simplified: Choose a Language, Run Your Code.",
//                style = TextStyle(
//                    fontSize = 16.sp,
//                    color = Color.Black,
//                    fontWeight = FontWeight.W500
//                ),
//                modifier = Modifier.padding(horizontal = 3.dp).padding(top = 24.dp),
//            )
//            FlowRow(
//                maxItemsInEachRow = 2,
//                horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
//                verticalArrangement = Arrangement.spacedBy(space = 16.dp),
//                modifier = Modifier.padding(vertical = 16.dp)
//            ) {
//                repeat(10) {
//                    Box(
//                        modifier = Modifier.size(150.dp).clip(RoundedCornerShape(12.dp))
//                            .background(Color.White)
//                            .border(border = BorderStroke(1.dp, color = Color(0xffD8E0E9)), shape = RoundedCornerShape(12.dp))
//                    ) {
//                        Image(
//                            painterResource("languages/kotlin.png"),
//                            modifier = Modifier.fillMaxWidth().size(80.dp).align(Alignment.Center),
//                            contentDescription = "language"
//                        )
//                    }
//                }
//            }
//        }

}