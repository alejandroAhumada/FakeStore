package com.example.fakestore.fakes

import com.example.fakestore.data.remote.api.FakeStoreApi
import com.example.fakestore.data.remote.dto.ProductDto
import kotlinx.coroutines.delay

class FakeStoreApiFake(
    private val products: List<ProductDto>,
    private val shouldFail: Boolean = false,
    private val artificialDelayMs: Long = 0L
) : FakeStoreApi {

    override suspend fun getProducts(): List<ProductDto> {
        if (artificialDelayMs > 0) delay(artificialDelayMs)
        if (shouldFail) error("network fail")
        return products
    }
}