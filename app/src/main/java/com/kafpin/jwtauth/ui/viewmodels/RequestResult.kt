package com.kafpin.jwtauth.ui.viewmodels

sealed class RequestResult<out T> {
    data object Init: RequestResult<Nothing>()
    data object Loading : RequestResult<Nothing>()
    data object ServerNotAvailable : RequestResult<Nothing>()
    data class Success<T>(val result: T) : RequestResult<T>()
    data class Error(val message: String) : RequestResult<Nothing>()
    data class NetworkError(val message: String) : RequestResult<Nothing>()
}