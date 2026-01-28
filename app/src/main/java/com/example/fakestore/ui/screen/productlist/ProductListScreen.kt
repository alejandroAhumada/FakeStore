package com.example.fakestore.ui.screen.productlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fakestore.ui.components.ErrorView
import com.example.fakestore.ui.components.LoadingView
import com.example.fakestore.ui.components.ProductRow
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun ProductListScreen(
    onOpenDetail: (Long) -> Unit,
    vm: ProductListViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()
    val swipeState = rememberSwipeRefreshState(isRefreshing = state.isRefreshing)

    Scaffold(
        topBar = { TopAppBar(title = { Text("Productos") }) }
    ) { padding ->
        when {
            state.isLoading && state.items.isEmpty() -> LoadingView()
            state.errorMessage != null && state.items.isEmpty() -> {
                ErrorView(
                    message = state.errorMessage ?: "Error",
                    onRetry = { vm.onEvent(ProductListEvent.Retry) }
                )
            }
            else -> {
                SwipeRefresh(
                    state = swipeState,
                    onRefresh = { vm.onEvent(ProductListEvent.Refresh) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    LazyColumn(
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 12.dp,
                            bottom = 16.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.items, key = { it.id }) { item ->
                            ProductRow(
                                item = item,
                                onClick = { onOpenDetail(item.id) },
                                onToggleFavorite = {
                                    vm.onEvent(ProductListEvent.ToggleFavorite(item.id))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}