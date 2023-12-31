package dev.abhaycloud.koderunner.model.code

import kotlinx.serialization.Serializable

@Serializable
data class RunCodeResponseModel(
    val error: String,
    val stderr: String,
    val stdout: String
)