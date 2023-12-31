package dev.abhaycloud.koderunner

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dev.abhaycloud.koderunner.database.CodeRunnerDatabase

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun create(): SqlDriver {
        return AndroidSqliteDriver(
            CodeRunnerDatabase.Schema,
            context,
            "language.db"
        )
    }
}