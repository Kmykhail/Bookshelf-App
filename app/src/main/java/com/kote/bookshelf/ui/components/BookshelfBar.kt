package com.kote.bookshelf.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kote.bookshelf.ui.BookshelfViewModel
import com.kote.bookshelf.ui.Content

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

        TextButton(onClick = sortBooks) {
            Log.d("BookshelfView", "App")
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "SORT", style = MaterialTheme.typography.labelLarge)
                Icon(
                    imageVector = Icons.Default.SortByAlpha,
                    contentDescription = null
                )
            }
        }
    }
}
