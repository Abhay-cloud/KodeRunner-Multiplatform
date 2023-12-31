package dev.abhaycloud.koderunner.di

import android.content.Context
import dev.abhaycloud.koderunner.DatabaseDriverFactory
import dev.abhaycloud.koderunner.data.local.LanguageDataSource
import dev.abhaycloud.koderunner.data.local.LocalDataSource
import dev.abhaycloud.koderunner.database.CodeRunnerDatabase

actual class AppModule(private val context: Context) {
    actual val languageDataSource: LanguageDataSource by lazy {
        LocalDataSource(
            db = CodeRunnerDatabase(
                driver = DatabaseDriverFactory(context).create()
            )
        )
    }
}