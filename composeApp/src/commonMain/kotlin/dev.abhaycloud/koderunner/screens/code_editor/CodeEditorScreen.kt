package dev.abhaycloud.koderunner.screens.code_editor

import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.abhaycloud.koderunner.components.CodeEditText
import dev.abhaycloud.koderunner.model.code.File
import dev.abhaycloud.koderunner.model.code.RunCodeRequestModel
import dev.abhaycloud.koderunner.model.language.LanguageModelItem
import dev.abhaycloud.koderunner.model.snippet.SnippetsModelItem
import dev.abhaycloud.koderunner.data.network.CodeRepository
import dev.abhaycloud.koderunner.screens.code_editor.RunCodeUiState
import dev.abhaycloud.koderunner.screens.code_editor.SnippetByIdUiState
import dev.abhaycloud.koderunner.utils.Utils.printLogs
import dev.abhaycloud.koderunner.viewmodel.CodeViewModel
import dev.snipme.highlights.Highlights
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

data class CodeEditorScreen(
    val isNew: Boolean = false,
    val snippetsItem: SnippetsModelItem? = null,
    val languageModelItem: LanguageModelItem? = null
) : Screen {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { CodeViewModel(CodeRepository()) }
        val codeResponseState = viewModel.runCodeResponse.collectAsState()
        val snippetResponseState = viewModel.snippetById.collectAsState()
        val navigator = LocalNavigator.current
        var isOutputWindowExpanded by remember { mutableStateOf(false) }
        val outputWindowWidth by animateFloatAsState(
            targetValue = if (isOutputWindowExpanded) 1f else 0.3f,
            animationSpec = TweenSpec(500)
        )
        val outputWindowHeight by animateFloatAsState(
            targetValue = if (isOutputWindowExpanded) 0.5f else 0.04f,
            animationSpec = TweenSpec(500)
        )
        val arrowRotationAngle by animateFloatAsState(
            targetValue = if (isOutputWindowExpanded) 180f else 0f,
            animationSpec = TweenSpec(500)
        )

        val fileList = viewModel.fileList.collectAsState()
        val currentFileIndex = remember { mutableStateOf(0) }

        val code = remember {
            mutableStateOf(
                Highlights.Builder(
                    fileList.value[currentFileIndex.value].content,
                ).build()
            )
        }


        LaunchedEffect(Unit) {
            if (!isNew) {
                viewModel.snippetById(snippetsItem!!.id)
            } else {
                viewModel.setPreFilledCode(languageModelItem!!)
            }
        }

        LaunchedEffect(snippetResponseState.value){
            if (!isNew) {
                when (snippetResponseState.value) {
                    is SnippetByIdUiState.Loading -> {

                    }

                    is SnippetByIdUiState.Success -> {
                        code.value =
                            Highlights.Builder(fileList.value[currentFileIndex.value].content)
                                .build()
                    }

                    is SnippetByIdUiState.Error -> {

                    }
                }
            }
        }

        LaunchedEffect(fileList.value){
            if (isNew) {
                code.value =
                    Highlights.Builder(
                        fileList.value[currentFileIndex.value].content,
                    ).build()
            }
        }

        LaunchedEffect(currentFileIndex.value){
            if (!isNew) {
                code.value =
                    Highlights.Builder(
                        fileList.value[currentFileIndex.value].content,
                    ).build()
            }
        }


        Box(modifier = Modifier.fillMaxSize()) {
            Column {
//            Button(onClick = {
//                viewModel.runCode(
//                    RunCodeRequestModel(
//                        stdin = "",
//                        files = arrayListOf(
//                            File(code.value.getCode(), fileName.value)
//                        ),
//                    ),
//                    language = "java"
//                )
//            }) {
//                Text("run code")
//                when(codeResponseState.value){
//                    is RunCodeUiState.Loading -> {
//                        CircularProgressIndicator()
//                    }
//                    is RunCodeUiState.Success -> {
//                        Text(text = (codeResponseState.value as RunCodeUiState.Success).data.stdout)
//                        printLogs((codeResponseState.value as RunCodeUiState.Success).data.stdout)
//                    }
//                    is RunCodeUiState.Error -> {
//                        Text(text = (codeResponseState.value as RunCodeUiState.Error).message)
//                    }
//                }
//            }

                IconButton(onClick = {
                    navigator?.pop()
                }, modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp).size(20.dp)) {
                    Icon(Icons.Rounded.ArrowBack, "back_arrow")
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    LazyRow(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        itemsIndexed(fileList.value) { index, item ->
                            FileNameChip(
                                index,
                                currentFileIndex.value,
                                fileList.value.size,
                                item,
                                onClick = {
                                    currentFileIndex.value = it
                                })
                        }
                    }
                    Image(
                        painter = painterResource("play.png"),
                        contentDescription = "run_button",
                        modifier = Modifier.padding(horizontal = 16.dp).size(24.dp)
                            .clickable {
                                viewModel.runCode(
                                    RunCodeRequestModel(
                                        stdin = "",
                                        files = fileList.value
//                                        arrayListOf(
//                                            File(
//                                                fileList.value[currentFileIndex.value].content,
//                                                fileList.value[currentFileIndex.value].name
//                                            )
//                                        ),
                                    ),
                                    language = if (isNew) languageModelItem!!.name else snippetsItem!!.language
                                )
                                isOutputWindowExpanded = true
                            }
                    )
                }


                CodeEditText(
//                    highlights = Highlights.Builder(fileList.value[currentFileIndex.value].content)
//                        .build(),
                    highlights = code.value,
                    onValueChange = { textValue ->
                        code.value = code.value.getBuilder()
                            .code(textValue)
                            .build()
                        fileList.value[currentFileIndex.value].content = textValue
                    },
                    // Customize view's style
                    colors = TextFieldDefaults.textFieldColors(),
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(outputWindowWidth)
                    .fillMaxHeight(outputWindowHeight)
                    .background(Color.Black)
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = {
                    isOutputWindowExpanded = !isOutputWindowExpanded
                }, modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp).size(30.dp)) {
                    Icon(
                        Icons.Rounded.KeyboardArrowUp,
                        "up_arrow",
                        tint = Color.White,
                        modifier = Modifier.graphicsLayer { rotationZ = arrowRotationAngle })
                }

                if (isOutputWindowExpanded)
                    OutputWindow(
                        codeResponseState.value
                    )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileNameChip(
    position: Int,
    selectedPosition: Int,
    size: Int,
    fileItem: File,
    onClick: (Int) -> Unit
) {
    var isClicked by remember { mutableStateOf(false) }
    Card(
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (position == selectedPosition) Color(0xffE8E2F6) else Color.White
        ),
        border = BorderStroke(1.dp, color = Color(0xffD8E0E9)),
        modifier = Modifier.padding(
            start = if (position == 0) 16.dp else 4.dp,
            end = if (size - 1 <= position) 16.dp else 4.dp
        ),
        onClick = {
            onClick.invoke(position)
            isClicked = !isClicked
        }
    ) {
        Text(
            fileItem.name, style = TextStyle(
                fontSize = 16.sp,
                color = Color.Black
            ),
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun OutputWindow(state: RunCodeUiState) {
    Box(modifier = Modifier.fillMaxSize().padding(10.dp)) {
        when (state) {
            is RunCodeUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is RunCodeUiState.Success -> {
                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(state = rememberScrollState())
                ) {
                    if (state.data.error.isEmpty()) {
                        Text(
                            text = "Output:\n${state.data.stdout}", style = TextStyle(
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W500
                            )
                        )
                        printLogs(state.data.stdout)
                    } else {
                        Text(
                            text = "Error:\n${state.data.stderr}", style = TextStyle(
                                color = Color.Red,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W500
                            )
                        )
                    }
                }
            }

            is RunCodeUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(state = rememberScrollState())
                ) {
                    Text(
                        text = "Error:\n${state.message}", style = TextStyle(
                            color = Color.Red,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W500
                        )
                    )
                }
            }
        }
    }
}

