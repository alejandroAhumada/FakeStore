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

class ProductListViewModelTest {

    @get:Rule
    val rule = MainDispatcherRule()

    private val repo = object : ProductRepository {
        val flow = MutableStateFlow<Resource<List<Product>>>(Resource.Success(emptyList()))
        var toggleId: Long? = null

        override fun observeProducts() = flow
        override fun observeProduct(id: Long): Flow<Resource<Product>> =
            flowOf(Resource.Error(AppError.NotFound))
        override suspend fun refreshProducts() = Resource.Success(Unit)
        override suspend fun toggleFavorite(productId: Long): Resource<Unit> {
            toggleId = productId
            return Resource.Success(Unit)
        }
    }

    @Test
    fun `expone productos cuando repository emite`() = runTest {
        val vm = ProductListViewModel(
            GetProductsUseCase(repo),
            ToggleFavoriteUseCase(repo)
        )

        vm.state.test {
            repo.flow.value = Resource.Success(
                listOf(
                    Product(
                        id = 1,
                        title = "A",
                        price = 10.0,
                        description = "desc",
                        category = "cat",
                        image = "img",
                        rating = Rating(4.0, 10),
                        isFavorite = false,
                        isFromCache = true
                    )
                )
            )

            val state = awaitItem()
            assertThat(state.items).hasSize(1)
            cancelAndIgnoreRemainingEvents()
        }
    }
}