package dev.abhaycloud.koderunner.utils

sealed class NetworkResponse<out T> {
    data class Success<out T>(val value: T) : NetworkResponse<T>()
    data class Failure(val message: String) : NetworkResponse<Nothing>()
    object Loading : NetworkResponse<Nothing>()
}