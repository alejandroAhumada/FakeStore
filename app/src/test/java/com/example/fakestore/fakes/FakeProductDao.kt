package com.example.fakestore.fakes

import com.example.fakestore.data.local.db.ProductDao
import com.example.fakestore.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeProductDao : ProductDao {

    private val itemsFlow = MutableStateFlow<List<ProductEntity>>(emptyList())

    override fun observeProducts(): Flow<List<ProductEntity>> = itemsFlow

    override fun observeProduct(id: Long): Flow<ProductEntity?> =
        itemsFlow.map { list -> list.firstOrNull { it.id == id } }

    override suspend fun upsertAll(items: List<ProductEntity>) {
        // Replace by id
        val current = itemsFlow.value.associateBy { it.id }.toMutableMap()
        for (it in items) current[it.id] = it
        itemsFlow.value = current.values.sortedBy { it.id }
    }

    override suspend fun setFavorite(id: Long, isFavorite: Boolean) {
        itemsFlow.value = itemsFlow.value.map {
            if (it.id == id) it.copy(isFavorite = isFavorite) else it
        }
    }

    override suspend fun getFavorite(id: Long): Boolean? =
        itemsFlow.value.firstOrNull { it.id == id }?.isFavorite

    override suspend fun getLatestUpdateMillis(): Long? =
        itemsFlow.value.maxOfOrNull { it.lastUpdatedMillis }
}