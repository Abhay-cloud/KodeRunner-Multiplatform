package dev.abhaycloud.koderunner.model.code

import kotlinx.serialization.Serializable

@Serializable
data class File(
    var content: String,
    val name: String
)