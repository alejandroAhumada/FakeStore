package com.example.fakestore.ui.screen.productdetail

import com.example.fakestore.domain.model.Product

data class ProductDetailState(
    val isLoading: Boolean = false,
    val product: Product? = null,
    val errorMessage: String? = null
)

sealed class ProductDetailEvent {
    data class ToggleFavorite(val productId: Long) : ProductDetailEvent()
}