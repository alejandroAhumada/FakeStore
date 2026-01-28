package com.example.fakestore.ui.screen.productdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestore.core.result.Resource
import com.example.fakestore.domain.usecase.ObserveProductUseCase
import com.example.fakestore.domain.usecase.ToggleFavoriteUseCase
import com.example.fakestore.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val observeProduct: ObserveProductUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase
) : ViewModel() {

    private val productId: Long = checkNotNull(savedStateHandle[Routes.DETAIL_ARG_ID])

    val state = MutableStateFlow(ProductDetailState(isLoading = true))

    init {
        viewModelScope.launch {
            observeProduct.observe(productId).collect { res ->
                when (res) {
                    is Resource.Loading -> state.update { it.copy(isLoading = true) }
                    is Resource.Success -> state.update {
                        it.copy(isLoading = false, product = res.data, errorMessage = null)
                    }
                    is Resource.Error -> state.update {
                        it.copy(isLoading = false, errorMessage = "Producto no encontrado")
                    }
                }
            }
        }
    }

    fun onEvent(event: ProductDetailEvent) {
        when (event) {
            is ProductDetailEvent.ToggleFavorite -> {
                viewModelScope.launch { toggleFavorite.execute(event.productId) }
            }
        }
    }
}