package dev.abhaycloud.koderunner.model.snippet
import kotlinx.serialization.Serializable


@Serializable
data class SnippetsModelItem(
    val created: String,
    val filesHash: String,
    val id: String,
    val language: String,
    val modified: String,
    val owner: String,
    val `public`: Boolean,
    val title: String,
    val url: String
)