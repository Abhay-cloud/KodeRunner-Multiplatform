package dev.abhaycloud.koderunner.model.language

import kotlinx.serialization.Serializable

@Serializable
data class LanguageModelItem(
    var id: Long? = -1,
    val name: String,
    val url: String,
    var snippet: String? = "",
    var extension: String? = ""
)