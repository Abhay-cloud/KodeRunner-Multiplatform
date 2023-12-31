package dev.abhaycloud.koderunner.di

import dev.abhaycloud.koderunner.DatabaseDriverFactory
import dev.abhaycloud.koderunner.data.local.LanguageDataSource
import dev.abhaycloud.koderunner.data.local.LocalDataSource
import dev.abhaycloud.koderunner.database.CodeRunnerDatabase

actual class AppModule() {
    actual val languageDataSource: LanguageDataSource by lazy {
        LocalDataSource(
            db = CodeRunnerDatabase(
                driver = DatabaseDriverFactory().create()
            )
        )
    }
}