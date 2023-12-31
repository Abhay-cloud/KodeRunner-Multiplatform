package dev.abhaycloud.koderunner.data.network

import dev.abhaycloud.koderunner.data.network.BASE_URL
import dev.abhaycloud.koderunner.data.network.httpClient
import dev.abhaycloud.koderunner.model.snippet.SnippetsModelItem
import dev.abhaycloud.koderunner.model.code.RunCodeRequestModel
import dev.abhaycloud.koderunner.model.code.RunCodeResponseModel
import dev.abhaycloud.koderunner.model.language.LanguageModelItem
import dev.abhaycloud.koderunner.model.snippet.SnippetByIdModel
import dev.abhaycloud.koderunner.utils.NetworkResponse
import dev.abhaycloud.koderunner.utils.Utils.printLogs
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.takeFrom

class CodeRepository {

    suspend fun getAvailableLanguages(): NetworkResponse<List<LanguageModelItem>>{
        try {
            val response = httpClient.get {
                url {
                    takeFrom("$BASE_URL/run")
                }
            }
            printLogs(response.body())
            return if (response.status == HttpStatusCode.OK) NetworkResponse.Success(response.body()) else NetworkResponse.Failure(
                "Error status code: ${response.status}"
            )
        } catch (e: Exception){
            return NetworkResponse.Failure(e.message!!)
        }
    }

    suspend fun getSnippets(language: String, page: Int): List<SnippetsModelItem> {
        val response = httpClient.get {
            url {
                takeFrom("$BASE_URL/snippets")
                parameters.append("language", language)
                parameters.append("per_page", "50")
                parameters.append("page", page.toString())
            }
        }
        printLogs(response.body<List<SnippetsModelItem>>())
        return if (response.status == HttpStatusCode.OK) response.body() else emptyList()
    }

    suspend fun snippetById(id: String): NetworkResponse<SnippetByIdModel> {
        try {
            val response = httpClient.get {
                url {
                    takeFrom("$BASE_URL/snippets/$id")
                }
            }
            printLogs(response.body<SnippetByIdModel>())
            return if (response.status == HttpStatusCode.OK) NetworkResponse.Success(response.body()) else NetworkResponse.Failure(
                "Error status code: ${response.status}"
            )
        } catch (e: Exception) {
            return NetworkResponse.Failure(e.message!!)
        }

    }

    suspend fun runCode(
        codeRequestModel: RunCodeRequestModel,
        language: String
    ): NetworkResponse<RunCodeResponseModel> {
        try {
            val response = httpClient.post {
                headers {
                    append(HttpHeaders.Authorization, "Token 35df35a9-e0c4-4623-9f50-0fca586a2ca2")
                    append(HttpHeaders.ContentType, "application/json")
                }
                url {
                    takeFrom("$BASE_URL/run/$language/latest")
                }
                setBody(codeRequestModel)
            }
            printLogs(response.body<RunCodeResponseModel>())
            return if (response.status == HttpStatusCode.OK) NetworkResponse.Success(response.body()) else NetworkResponse.Failure(
                "Error status code: ${response.status}"
            )
        } catch (e: Exception) {
            return NetworkResponse.Failure(e.message!!)
        }
    }

    suspend fun fetchPreFilledCode(language: String): NetworkResponse<String> {
        return try {
            val response = httpClient.get {
                url {
                    takeFrom("https://glot.io/new/$language")
                }
            }
//            printLogs(response.body<String>())
            if (response.status == HttpStatusCode.OK) NetworkResponse.Success(response.body()) else NetworkResponse.Failure(
                "Error status code: ${response.status}"
            )
        } catch (e: Exception) {
            NetworkResponse.Failure(e.message!!)
        }
    }
}