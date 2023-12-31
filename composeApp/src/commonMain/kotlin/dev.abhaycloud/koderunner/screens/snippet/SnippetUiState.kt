package dev.abhaycloud.koderunner.screens.snippet

import dev.abhaycloud.koderunner.model.snippet.SnippetsModelItem

sealed class SnippetUiState {
    object Loading: SnippetUiState()
    data class Success(val data: List<SnippetsModelItem>): SnippetUiState()
    data class Error(val message: String): SnippetUiState()
}