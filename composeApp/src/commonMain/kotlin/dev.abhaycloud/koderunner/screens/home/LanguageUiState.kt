package dev.abhaycloud.koderunner.screens.home

import dev.abhaycloud.koderunner.model.language.LanguageModelItem

sealed class LanguageUiState {
    object Loading: LanguageUiState()
    data class Success(val data: List<LanguageModelItem>): LanguageUiState()
    data class Error(val message: String): LanguageUiState()
}