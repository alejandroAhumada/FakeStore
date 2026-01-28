package com.example.fakestore.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val ratingRate: Double? = null,
    val ratingCount: Int? = null,
    val isFavorite: Boolean = false,
    val lastUpdatedMillis: Long = 0L
)