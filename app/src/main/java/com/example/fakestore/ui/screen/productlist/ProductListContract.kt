package com.example.fakestore.ui.screen.productlist

import com.example.fakestore.domain.model.Product

data class ProductListState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val items: List<Product> = emptyList(),
    val errorMessage: String? = null
)

sealed class ProductListEvent {
    data object Load : ProductListEvent()
    data object Retry : ProductListEvent()
    data object Refresh : ProductListEvent()
    data class ToggleFavorite(val productId: Long) : ProductListEvent()
}