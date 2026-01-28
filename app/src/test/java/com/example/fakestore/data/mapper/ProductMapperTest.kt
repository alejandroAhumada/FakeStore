package com.example.fakestore.data.mapper

import com.example.fakestore.data.remote.dto.ProductDto
import com.example.fakestore.data.remote.dto.RatingDto
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ProductMapperTest {

    @Test
    fun `ProductDto toEntity mapea rating correctamente`() {
        val dto = ProductDto(
            id = 1,
            title = "A",
            price = 10.0,
            description = "desc",
            category = "cat",
            image = "img",
            rating = RatingDto(4.5, 100)
        )

        val entity = dto.toEntity(
            isFavorite = false,
            lastUpdatedMillis = 123
        )

        assertThat(entity.ratingRate).isEqualTo(4.5)
        assertThat(entity.ratingCount).isEqualTo(100)
    }

    @Test
    fun `ProductEntity toDomain mapea rating correctamente`() {
        val entity = com.example.fakestore.data.local.entity.ProductEntity(
            id = 1,
            title = "A",
            price = 10.0,
            description = "desc",
            category = "cat",
            image = "img",
            ratingRate = 4.2,
            ratingCount = 50,
            isFavorite = true,
            lastUpdatedMillis = 1
        )

        val domain = entity.toDomain(isFromCache = true)

        assertThat(domain.rating?.rate).isEqualTo(4.2)
        assertThat(domain.rating?.count).isEqualTo(50)
        assertThat(domain.isFavorite).isTrue()
    }
}