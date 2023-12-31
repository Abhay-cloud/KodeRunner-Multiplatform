package dev.abhaycloud.koderunner.model.snippet

import dev.abhaycloud.koderunner.model.code.File
import kotlinx.serialization.Serializable

@Serializable
data class SnippetByIdModel(
    val created: String,
    val files: List<File>,
    val filesHash: String,
    val id: String,
    val language: String,
    val modified: String,
    val owner: String,
    val `public`: Boolean,
    val title: String,
    val url: String
)