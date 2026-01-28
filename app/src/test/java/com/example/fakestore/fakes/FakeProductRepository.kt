import com.example.fakestore.core.result.AppError
import com.example.fakestore.core.result.Resource
import com.example.fakestore.domain.model.Product
import com.example.fakestore.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

private class FakeProductRepository : ProductRepository {
    val flow = MutableStateFlow<Resource<List<Product>>>(Resource.Success(emptyList()))
    var refreshCalled = false
    var toggledId: Long? = null

    override fun observeProducts(): Flow<Resource<List<Product>>> = flow

    override fun observeProduct(id: Long): Flow<Resource<Product>> =
        flowOf(Resource.Error(AppError.NotFound))

    override suspend fun refreshProducts(): Resource<Unit> {
        refreshCalled = true
        return Resource.Success(Unit)
    }

    override suspend fun toggleFavorite(productId: Long): Resource<Unit> {
        toggledId = productId
        return Resource.Success(Unit)
    }
}