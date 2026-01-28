package com.example.fakestore.ui.screen.productlist

import app.cash.turbine.test
import com.example.fakestore.MainDispatcherRule
import com.example.fakestore.core.result.AppError
import com.example.fakestore.core.result.Resource
import com.example.fakestore.domain.model.Product
import com.example.fakestore.domain.model.Rating
import com.example.fakestore.domain.repository.ProductRepository
import com.example.fakestore.domain.usecase.GetProductsUseCase
import com.example.fakestore.domain.usecase.ToggleFavoriteUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

private class FakeProductRepository : ProductRepository {
    val flow = MutableStateFlow<Resource<List<Product>>>(Resource.Success(emptyList()))
    var refreshCalled = false
    var toggledId: Long? = null

    override fun observeProducts(): Flow<Resource<List<Product>>> = flow
    override fun observeProduct(id: Long) = flowOf(Resource.Error(AppError.NotFound))
    override suspend fun refreshProducts(): Resource<Unit> {
        refreshCalled = true
        return Resource.Success(Unit)
    }
    override suspend fun toggleFavorite(productId: Long): Resource<Unit> {
        toggledId = productId
        return Resource.Success(Unit)
    }
}

class ProductListViewModelTest {

    @get:Rule
    val mainRule = MainDispatcherRule()

    @Test
    fun `al init dispara refresh y expone items cuando llegan`() = runTest {
        val repo = FakeProductRepository()
        val vm = ProductListViewModel(
            getProducts = GetProductsUseCase(repo),
            toggleFavorite = ToggleFavoriteUseCase(repo)
        )

        // Se llama refresh (por init Load/Refresh)
        assertThat(repo.refreshCalled).isTrue()

        val sample = Product(
            id = 1,
            title = "A",
            price = 10.0,
            description = "desc",
            category = "cat",
            image = "img",
            rating = Rating(4.2, 100),
            isFavorite = false,
            isFromCache = true
        )

        vm.state.test {
            // estado inicial
            val first = awaitItem()
            // No afirmamos demasiado estricto aquí (depende del orden), pero debe existir State
            assertThat(first).isNotNull()

            // emite nuevos datos
            repo.flow.value = Resource.Success(listOf(sample))
            val next = awaitItem()
            assertThat(next.items).hasSize(1)
            assertThat(next.items.first().rating?.rate).isEqualTo(4.2)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `ToggleFavorite llama usecase con id`() = runTest {
        val repo = FakeProductRepository()
        val vm = ProductListViewModel(
            getProducts = GetProductsUseCase(repo),
            toggleFavorite = ToggleFavoriteUseCase(repo)
        )

        vm.onEvent(ProductListEvent.ToggleFavorite(99L))
        assertThat(repo.toggledId).isEqualTo(99L)
    }
}