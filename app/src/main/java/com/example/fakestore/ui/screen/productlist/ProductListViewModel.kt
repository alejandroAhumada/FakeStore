package com.example.fakestore.ui.screen.productlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestore.core.result.Resource
import com.example.fakestore.domain.usecase.GetProductsUseCase
import com.example.fakestore.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getProducts: GetProductsUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProductListState(isLoading = true))
    val state: StateFlow<ProductListState> =
        _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), _state.value)

    init {
        observeLocal()
        onEvent(ProductListEvent.Load)
    }

    private fun observeLocal() {
        viewModelScope.launch {
            getProducts.observe().collect { res ->
                when (res) {
                    is Resource.Loading -> _state.update { it.copy(isLoading = true, errorMessage = null) }
                    is Resource.Success -> _state.update {
                        it.copy(isLoading = false, items = res.data, errorMessage = null)
                    }
                    is Resource.Error -> _state.update {
                        it.copy(isLoading = false, errorMessage = "Error cargando productos")
                    }
                }
            }
        }
    }

    fun onEvent(event: ProductListEvent) {
        when (event) {
            ProductListEvent.Load,
            ProductListEvent.Refresh,
            ProductListEvent.Retry -> refresh()

            is ProductListEvent.ToggleFavorite -> {
                viewModelScope.launch { toggleFavorite.execute(event.productId) }
            }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true, errorMessage = null) }
            val res = getProducts.refresh()
            _state.update { it.copy(isRefreshing = false) }
            if (res is Resource.Error) {
                _state.update { it.copy(errorMessage = "No se pudo actualizar (mostrando caché)") }
            }
        }
    }
}