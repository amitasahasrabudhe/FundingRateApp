package com.crypto.fundingrate.util

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Loading<T>(val isLoading: Boolean = true): Resource<T>()
    class Success<T>(data:T): Resource<T>(data)
    class Error<T>(message: String, data: T? = null): Resource<T>(data, message)
}
