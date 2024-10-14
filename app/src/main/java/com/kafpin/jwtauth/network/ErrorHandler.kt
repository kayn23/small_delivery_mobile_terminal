package com.kafpin.jwtauth.network

import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ErrorHandler {

    fun handleError(response: Response<*>): String {
        return when (response.code()) {
            400 -> "Bad Request"
            401 -> "Unauthorized"
            403 -> "Forbidden"
            404 -> "Not Found"
            500 -> "Internal Server Error"
            else -> "Unknown Error"
        }
    }

    fun handleException(exception: Throwable): String {
        return when (exception) {
            is HttpException -> handleError(exception.response() ?: Response.error<Any>(500, null))
            is IOException -> "Network Error"
            else -> "Unexpected Error"
        }
    }
}