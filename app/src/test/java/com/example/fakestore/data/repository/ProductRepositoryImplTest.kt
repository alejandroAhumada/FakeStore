package com.example.fakestore.data.repository

import app.cash.turbine.test
import com.example.fakestore.TestDispatcherProvider
import com.example.fakestore.core.result.Resource
import com.example.fakestore.data.remote.dto.ProductDto
import com.example.fakestore.data.remote.dto.RatingDto
import com.example.fakestore.fakes.FakeProductDao
import com.example.fakestore.fakes.FakeStoreApiFake
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductRepositoryImplTest {

    private val dispatcher = StandardTestDispatcher()
    private val dispatchers = TestDispatcherProvider(
        io = dispatcher,
        main = dispatcher,
        default = dispatcher
    )

    @Test
    fun `refreshProducts guarda productos en cache`() = runTest(dispatcher) {
        val dao = FakeProductDao()
        val api = FakeStoreApiFake(
            products = listOf(
                ProductDto(
                    id = 1,
                    title = "A",
                    price = 10.0,
                    description = "desc",
                    category = "cat",
                    image = "img",
                    rating = RatingDto(4.0, 20)
                )
            )
        )

        val repo = ProductRepositoryImpl(api, dao, dispatchers)

        val result = repo.refreshProducts()
        assertThat(result).isInstanceOf(Resource.Success::class.java)

        repo.observeProducts().test {
            val item = awaitItem() as Resource.Success
            assertThat(item.data).hasSize(1)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggleFavorite cambia favorito`() = runTest(dispatcher) {
        val dao = FakeProductDao()
        val repo = ProductRepositoryImpl(
            api = FakeStoreApiFake(emptyList()),
            dao = dao,
            dispatchers = dispatchers
        )

        dao.upsertAll(
            listOf(
                com.example.fakestore.data.local.entity.ProductEntity(
                    id = 1,
                    title = "A",
                    price = 10.0,
                    description = "desc",
                    category = "cat",
                    image = "img"
                )
            )
        )

        repo.toggleFavorite(1)
        assertThat(dao.getFavorite(1)).isTrue()
    }
}