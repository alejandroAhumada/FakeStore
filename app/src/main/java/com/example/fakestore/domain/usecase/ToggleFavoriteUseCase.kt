package com.example.fakestore.domain.usecase

import com.example.fakestore.core.result.Resource
import com.example.fakestore.domain.repository.ProductRepository

class ToggleFavoriteUseCase(
    private val repository: ProductRepository
) {
    suspend fun execute(productId: Long): Resource<Unit> = repository.toggleFavorite(productId)
}