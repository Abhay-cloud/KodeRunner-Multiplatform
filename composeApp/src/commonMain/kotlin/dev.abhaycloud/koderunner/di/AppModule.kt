package dev.abhaycloud.koderunner.di

import dev.abhaycloud.koderunner.data.local.LanguageDataSource

expect class AppModule {
    val languageDataSource: LanguageDataSource
}