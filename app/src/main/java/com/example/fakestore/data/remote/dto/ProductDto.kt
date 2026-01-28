package com.example.fakestore.data.remote.dto

data class ProductDto(
    val id: Long,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: RatingDto?
)