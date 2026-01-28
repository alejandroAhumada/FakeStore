package com.example.fakestore.domain.usecase

import com.example.fakestore.core.result.Resource
import com.example.fakestore.domain.model.Product
import com.example.fakestore.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class GetProductsUseCase(
    private val repository: ProductRepository
) {
    fun observe(): Flow<Resource<List<Product>>> = repository.observeProducts()
    suspend fun refresh(): Resource<Unit> = repository.refreshProducts()
}