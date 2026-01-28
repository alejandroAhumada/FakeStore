package com.example.fakestore.domain.usecase

import com.example.fakestore.core.result.Resource
import com.example.fakestore.domain.model.Product
import com.example.fakestore.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class ObserveProductUseCase(
    private val repository: ProductRepository
) {
    fun observe(id: Long): Flow<Resource<Product>> = repository.observeProduct(id)
}