package com.example.fakestore.di

import com.example.fakestore.core.dispatcher.DefaultDispatcherProvider
import com.example.fakestore.core.dispatcher.DispatcherProvider
import com.example.fakestore.data.local.db.ProductDao
import com.example.fakestore.data.remote.api.FakeStoreApi
import com.example.fakestore.data.repository.ProductRepositoryImpl
import com.example.fakestore.domain.repository.ProductRepository
import com.example.fakestore.domain.usecase.GetProductsUseCase
import com.example.fakestore.domain.usecase.ObserveProductUseCase
import com.example.fakestore.domain.usecase.ToggleFavoriteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides @Singleton
    fun provideDispatchers(): DispatcherProvider = DefaultDispatcherProvider()

    @Provides @Singleton
    fun provideRepository(
        api: FakeStoreApi,
        dao: ProductDao,
        dispatchers: DispatcherProvider
    ): ProductRepository = ProductRepositoryImpl(api, dao, dispatchers)

    @Provides
    fun provideGetProductsUseCase(repo: ProductRepository) = GetProductsUseCase(repo)

    @Provides
    fun provideObserveProductUseCase(repo: ProductRepository) = ObserveProductUseCase(repo)

    @Provides
    fun provideToggleFavoriteUseCase(repo: ProductRepository) = ToggleFavoriteUseCase(repo)
}