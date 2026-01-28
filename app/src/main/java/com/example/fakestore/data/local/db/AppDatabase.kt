package com.example.fakestore.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fakestore.data.local.entity.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}