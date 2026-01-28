package com.example.fakestore.data.repository

import com.example.fakestore.core.dispatcher.DispatcherProvider
import com.example.fakestore.core.result.AppError
import com.example.fakestore.core.result.Resource
import com.example.fakestore.data.local.db.ProductDao
import com.example.fakestore.data.mapper.toDomain
import com.example.fakestore.data.mapper.toEntity
import com.example.fakestore.data.remote.api.FakeStoreApi
import com.example.fakestore.domain.model.Product
import com.example.fakestore.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class ProductRepositoryImpl(
    private val api: FakeStoreApi,
    private val dao: ProductDao,
    private val dispatchers: DispatcherProvider
) : ProductRepository {

    override fun observeProducts(): Flow<Resource<List<Product>>> {
        return dao.observeProducts()
            .map { entities ->
                val isFromCache = true
                Resource.Success(entities.map { it.toDomain(isFromCache = isFromCache) })
            }
            .flowOn(dispatchers.io)
    }

    override fun observeProduct(id: Long): Flow<Resource<Product>> {
        return dao.observeProduct(id)
            .map { entity ->
                if (entity == null) Resource.Error(AppError.NotFound)
                else Resource.Success(entity.toDomain(isFromCache = true))
            }
            .flowOn(dispatchers.io)
    }

    override suspend fun refreshProducts(): Resource<Unit> = withContext(dispatchers.io) {
        try {
            val remote = api.getProducts()
            val now = System.currentTimeMillis()

            val mapped = remote.map { dto ->
                val favorite = dao.getFavorite(dto.id) ?: false
                dto.toEntity(isFavorite = favorite, lastUpdatedMillis = now)
            }

            dao.upsertAll(mapped)
            Resource.Success(Unit)
        } catch (e: IOException) {
            Resource.Error(AppError.Network)
        } catch (e: HttpException) {
            Resource.Error(AppError.Unknown(e.message()))
        } catch (e: Exception) {
            Resource.Error(AppError.Unknown(e.message))
        }
    }

    override suspend fun toggleFavorite(productId: Long): Resource<Unit> = withContext(dispatchers.io) {
        try {
            val current = dao.observeProduct(productId).first()
                ?: return@withContext Resource.Error(AppError.NotFound)

            dao.setFavorite(productId, !current.isFavorite)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(AppError.Unknown(e.message))
        }
    }
}