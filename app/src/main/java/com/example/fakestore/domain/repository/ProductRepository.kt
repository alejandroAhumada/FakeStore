package com.example.fakestore.domain.repository

import com.example.fakestore.core.result.Resource
import com.example.fakestore.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun observeProducts(): Flow<Resource<List<Product>>>
    fun observeProduct(id: Long): Flow<Resource<Product>>
    suspend fun refreshProducts(): Resource<Unit>
    suspend fun toggleFavorite(productId: Long): Resource<Unit>
}