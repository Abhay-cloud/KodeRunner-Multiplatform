package dev.abhaycloud.koderunner.screens.code_editor

import dev.abhaycloud.koderunner.model.code.RunCodeResponseModel

sealed class RunCodeUiState {
    object Loading: RunCodeUiState()
    data class Success(val data: RunCodeResponseModel): RunCodeUiState()
    data class Error(val message: String): RunCodeUiState()
}