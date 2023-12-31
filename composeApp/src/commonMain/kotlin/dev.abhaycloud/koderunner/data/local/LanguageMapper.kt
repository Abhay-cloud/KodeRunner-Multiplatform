package dev.abhaycloud.koderunner.data.local

import database.LanguageTable
import dev.abhaycloud.koderunner.model.language.LanguageModelItem

fun LanguageTable.toLanguage(): LanguageModelItem {
    return LanguageModelItem(
        id = id,
        name = name,
        url = url,
        snippet = snippet,
        extension = extension
    )
}