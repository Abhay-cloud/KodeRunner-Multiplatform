package dev.abhaycloud.koderunner.data.network

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

const val BASE_URL: String = "https://glot.io/api"

val httpClient = HttpClient {
    expectSuccess = true
    install(HttpTimeout) {
        val timeout = 50000L
        connectTimeoutMillis = timeout
        requestTimeoutMillis = timeout
        socketTimeoutMillis = timeout
    }
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.HEADERS
    }
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
            useArrayPolymorphism = true
        })
    }
}