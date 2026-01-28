package com.example.fakestore.core.result

sealed class AppError {
    data object Network : AppError()
    data object NotFound : AppError()
    data class Unknown(val message: String? = null) : AppError()
}