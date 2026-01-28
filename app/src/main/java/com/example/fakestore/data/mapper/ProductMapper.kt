package com.example.fakestore.data.mapper

import com.example.fakestore.data.local.entity.ProductEntity
import com.example.fakestore.data.remote.dto.ProductDto
import com.example.fakestore.domain.model.Product
import com.example.fakestore.domain.model.Rating

fun ProductDto.toEntity(
    isFavorite: Boolean,
    lastUpdatedMillis: Long
): ProductEntity = ProductEntity(
    id = id,
    title = title,
    price = price,
    description = description,
    category = category,
    image = image,
    ratingRate = rating?.rate,
    ratingCount = rating?.count,
    isFavorite = isFavorite,
    lastUpdatedMillis = lastUpdatedMillis
)

fun ProductEntity.toDomain(
    isFromCache: Boolean
): Product = Product(
    id = id,
    title = title,
    price = price,
    description = description,
    category = category,
    image = image,
    rating = if (ratingRate != null && ratingCount != null) {
        Rating(rate = ratingRate, count = ratingCount)
    } else null,
    isFavorite = isFavorite,
    isFromCache = isFromCache
)