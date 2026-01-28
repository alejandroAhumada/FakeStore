package com.example.fakestore.ui.screen.productdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.fakestore.ui.components.ErrorView
import com.example.fakestore.ui.components.LoadingView

@Composable
fun ProductDetailScreen(
    onBack: () -> Unit,
    vm: ProductDetailViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Text("←") }
                }
            )
        }
    ) { padding ->
        when {
            state.isLoading -> LoadingView()

            state.errorMessage != null -> {
                ErrorView(
                    message = state.errorMessage ?: "Error",
                    onRetry = onBack
                )
            }

            state.product != null -> {
                val p = state.product!!

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AsyncImage(
                        model = p.image,
                        contentDescription = p.title,
                        modifier = Modifier.height(260.dp)
                    )

                    Text(text = p.title)
                    Text(text = "Precio: $${p.price}")
                    Text(text = "Categoría: ${p.category}")

                    p.rating?.let { r ->
                        Text(text = "Rating: ⭐ ${r.rate} (${r.count})")
                    }

                    if (p.isFromCache) {
                        Text(text = "Mostrando desde caché")
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = p.description)

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = { vm.onEvent(ProductDetailEvent.ToggleFavorite(p.id)) }) {
                        Text(if (p.isFavorite) "Quitar favorito ★" else "Marcar favorito ☆")
                    }
                }
            }
        }
    }
}