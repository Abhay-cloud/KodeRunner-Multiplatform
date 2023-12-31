package dev.abhaycloud.koderunner.model.code

import dev.abhaycloud.koderunner.model.code.File
import kotlinx.serialization.Serializable

@Serializable
data class RunCodeRequestModel(
    val files: List<File>,
    val stdin: String
)