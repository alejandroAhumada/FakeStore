package com.example.fakestore.data.remote.api

import com.example.fakestore.data.remote.dto.ProductDto
import retrofit2.http.GET

interface FakeStoreApi {
    @GET("products")
    suspend fun getProducts(): List<ProductDto>
}