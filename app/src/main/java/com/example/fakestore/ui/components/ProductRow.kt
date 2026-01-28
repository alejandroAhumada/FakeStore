package com.example.fakestore.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.fakestore.domain.model.Product

@Composable
fun ProductRow(
    item: Product,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = item.image,
                contentDescription = item.title,
                modifier = Modifier.size(72.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "$${item.price}",
                    style = MaterialTheme.typography.body1
                )

                item.rating?.let { r ->
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "⭐ ${r.rate} (${r.count})",
                        style = MaterialTheme.typography.body2
                    )
                }

                if (item.isFromCache) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(elevation = 2.dp) {
                        Text(
                            text = "CACHE",
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.caption
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            IconButton(onClick = onToggleFavorite) {
                Text(if (item.isFavorite) "★" else "☆")
            }
        }
    }
}