package com.example.fakestore.ui.screen.productdetail

import androidx.lifecycle.SavedStateHandle
import com.example.fakestore.MainDispatcherRule
import com.example.fakestore.core.result.Resource
import com.example.fakestore.domain.model.Product
import com.example.fakestore.domain.model.Rating
import com.example.fakestore.domain.repository.ProductRepository
import com.example.fakestore.domain.usecase.ObserveProductUseCase
import com.example.fakestore.domain.usecase.ToggleFavoriteUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class ProductDetailViewModelTest {

    @get:Rule
    val rule = MainDispatcherRule()

    @Test
    fun `carga producto correctamente`() = runTest {
        val product = Product(
            id = 1,
            title = "A",
            price = 10.0,
            description = "desc",
            category = "cat",
            image = "img",
            rating = Rating(4.5, 100),
            isFavorite = false,
            isFromCache = true
        )

        val repo = object : ProductRepository {
            override fun observeProduct(id: Long) =
                flowOf(Resource.Success(product))
            override fun observeProducts() = flowOf()
            override suspend fun refreshProducts() = Resource.Success(Unit)
            override suspend fun toggleFavorite(productId: Long) = Resource.Success(Unit)
        }

        val vm = ProductDetailViewModel(
            savedStateHandle = SavedStateHandle(mapOf("id" to 1L)),
            observeProduct = ObserveProductUseCase(repo),
            toggleFavorite = ToggleFavoriteUseCase(repo)
        )

        val state = vm.state.value
        assertThat(state.product?.id).isEqualTo(1L)
    }
}