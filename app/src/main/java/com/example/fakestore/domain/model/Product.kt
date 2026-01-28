package com.example.fakestore.domain.model

data class Product(
    val id: Long,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: Rating?,
    val isFavorite: Boolean,
    val isFromCache: Boolean
)