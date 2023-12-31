package dev.abhaycloud.koderunner.screens.code_editor

import dev.abhaycloud.koderunner.model.snippet.SnippetByIdModel

sealed class SnippetByIdUiState {
    object Loading: SnippetByIdUiState()
    data class Success(val data: SnippetByIdModel): SnippetByIdUiState()
    data class Error(val message: String): SnippetByIdUiState()
}