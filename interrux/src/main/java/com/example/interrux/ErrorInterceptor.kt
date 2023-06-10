package com.example.interrux

import android.util.Log
import okhttp3.Interceptor

class ErrorInterceptor(private val errorHandler: ErrorHandler?): Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        val response = chain.proceed(request)

        when (response.code) {
            400 -> logError(response.code, "Bad Request")
            401 -> logError(response.code, "Unauthorized")
            403 -> logError(response.code, "Forbidden")
            404 -> logError(response.code, "Not Found")
            405 -> logError(response.code, "Method Not Allowed")
            408 -> logError(response.code, "Request Timeout")
            500 -> logError(response.code, "Internal Server Error")
            502 -> logError(response.code, "Bad Gateway")
            503 -> logError(response.code, "Service Unavailable")
            504 -> logError(response.code, "Gateway Timeout")

            else -> {
                val errorMessage = "Unknown Error"
                logError(response.code, errorMessage)
            }
        }

        return response
    }

    private fun logError(errorCode:Int, errorMessage: String) {
        errorHandler?.onError(errorCode, errorMessage)
        Log.e("ErrorInterceptor", "Error $errorCode $errorMessage")
    }

    interface ErrorHandler {
        fun onError(errorCode: Int,errorMessage: String)
    }
}