package dev.abhaycloud.koderunner.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import dev.abhaycloud.koderunner.model.code.File
import dev.abhaycloud.koderunner.model.code.RunCodeRequestModel
import dev.abhaycloud.koderunner.model.language.LanguageModelItem
import dev.abhaycloud.koderunner.data.network.CodeRepository
import dev.abhaycloud.koderunner.screens.code_editor.RunCodeUiState
import dev.abhaycloud.koderunner.screens.code_editor.SnippetByIdUiState
import dev.abhaycloud.koderunner.utils.NetworkResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CodeViewModel(private val codeRepository: CodeRepository): ScreenModel {
    private val _runCodeResponse = MutableStateFlow<RunCodeUiState>(RunCodeUiState.Loading)
    val runCodeResponse: StateFlow<RunCodeUiState> = _runCodeResponse

    fun runCode(codeRequestModel: RunCodeRequestModel, language: String){
        coroutineScope.launch {
            kotlin.runCatching {
                _runCodeResponse.value = RunCodeUiState.Loading
                val result = codeRepository.runCode(codeRequestModel, language)
                when(result){
                    is NetworkResponse.Loading -> {
                        _runCodeResponse.value = RunCodeUiState.Loading
                    }
                    is NetworkResponse.Success -> {
                        _runCodeResponse.value = RunCodeUiState.Success(result.value)
                    }
                    is NetworkResponse.Failure -> {
                        _runCodeResponse.value = RunCodeUiState.Error(result.message)
                    }
                }
            }.onFailure {
                _runCodeResponse.value = RunCodeUiState.Error(it.message.toString())
            }
        }
    }

    private val _snippetById = MutableStateFlow<SnippetByIdUiState>(SnippetByIdUiState.Loading)
    val snippetById: StateFlow<SnippetByIdUiState> = _snippetById
    private val _fileList = MutableStateFlow<List<File>>(arrayListOf(File("", "fileName.ext")))
    val fileList: StateFlow<List<File>> = _fileList

    fun snippetById(id: String){
        coroutineScope.launch {
            kotlin.runCatching {
                _snippetById.value = SnippetByIdUiState.Loading
                val result = codeRepository.snippetById(id)
                when(result){
                    is NetworkResponse.Loading -> {
                        _snippetById.value = SnippetByIdUiState.Loading
                    }
                    is NetworkResponse.Success -> {
                        _fileList.value = result.value.files
                        _snippetById.value = SnippetByIdUiState.Success(result.value)
                    }
                    is NetworkResponse.Failure -> {
                        _snippetById.value = SnippetByIdUiState.Error(result.message)
                    }
                }
            }.onFailure {
                _snippetById.value = SnippetByIdUiState.Error(it.message.toString())
            }
        }
    }

    fun setPreFilledCode(languageModelItem: LanguageModelItem){
        _fileList.value = arrayListOf(File(
            content = languageModelItem.snippet!!,
            name = languageModelItem.extension!!
        ))
    }

}