package dev.abhaycloud.koderunner

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import dev.abhaycloud.koderunner.database.CodeRunnerDatabase

actual class DatabaseDriverFactory {
    actual fun create(): SqlDriver {
        return NativeSqliteDriver(
             CodeRunnerDatabase.Schema,
            "language.db")
    }
}