package dev.abhaycloud.koderunner

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import dev.abhaycloud.koderunner.database.CodeRunnerDatabase

actual class DatabaseDriverFactory {
    actual fun create(): SqlDriver {
        return JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply{
            CodeRunnerDatabase.Schema.create(this)
        }
    }
}