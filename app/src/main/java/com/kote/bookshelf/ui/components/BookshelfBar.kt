package com.kote.bookshelf.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun BookshelfBar(
    showFavorites: () -> Unit,
    sortBooks: () -> Unit,
    bookNumber: Int = 0,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "$bookNumber BOOKS",
            style = MaterialTheme.typography.labelLarge.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
        )

        IconButton(onClick = showFavorites) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "favorites",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
        }

        IconButton(onClick = sortBooks) {
            Icon(
                imageVector = Icons.Default.SortByAlpha,
                contentDescription = null
            )
        }
    }
}
