package dev.abhaycloud.koderunner.data.local

import dev.abhaycloud.koderunner.database.CodeRunnerDatabase
import dev.abhaycloud.koderunner.model.language.LanguageModelItem
import dev.abhaycloud.koderunner.data.local.LanguageDataSource

class LocalDataSource(db: CodeRunnerDatabase): LanguageDataSource {

    private val queries = db.languageQueries

    override fun getLanguages(): List<LanguageModelItem> {
        return queries.selectLanguages().executeAsList().map { it.toLanguage() }
    }

    override fun insertLanguage(language: LanguageModelItem, id: Long) {
        queries.insertLanguage(id, language.name, language.url, language.snippet, language.extension)
    }

    override fun getLanguageByName(language: String): LanguageModelItem {
        return queries.selectLanguageByName(language).executeAsOne().toLanguage()
    }

}