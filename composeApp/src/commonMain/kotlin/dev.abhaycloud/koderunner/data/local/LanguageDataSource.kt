package dev.abhaycloud.koderunner.data.local

import dev.abhaycloud.koderunner.model.language.LanguageModelItem

interface LanguageDataSource {
    fun getLanguages(): List<LanguageModelItem>
    fun insertLanguage(language: LanguageModelItem, id: Long)
    fun getLanguageByName(language: String): LanguageModelItem
}