package com.kote.bookshelf.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kote.bookshelf.R
import com.kote.bookshelf.model.Book

@Composable
fun BookshelfGrid(
    books: List<Book>,
    favoriteAction: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    if (books.isNotEmpty()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 2.dp)
        ) {
            items(books) {book ->
                ThumbnailCard(
                    book = book,
                    favoriteAction = favoriteAction,
                    modifier = Modifier
                        .aspectRatio(1f)
                )
            }
        }
    }
}

@Composable
fun ThumbnailCard(
    book: Book,
    favoriteAction: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    val thumbnail = book.thumbnail
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.small,
        modifier = modifier
    ) {
        if (thumbnail.isNotEmpty() && thumbnail.startsWith("http")) {
            Box(modifier = modifier.fillMaxSize()) {
                AsyncImage(
                    model = thumbnail.replace("http:", "https:"),
                    contentDescription = "name",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.loading_img),
                    error = rememberVectorPainter(Icons.Default.Error),
                    modifier = Modifier.fillMaxSize()
                )
                IconButton(
                    onClick = { favoriteAction(book) },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = if (book.isFavorite) Color.Red else Color.Gray,
                    )
                }
            }
        } else {
            Image(
                painter = rememberVectorPainter(Icons.Default.Error),
                contentDescription = "Invalid Thumbnail",
                modifier = Modifier.fillMaxSize()
            )
            Log.e("ThumbnailCard", "Invalid thumbnail URL: $thumbnail")
        }
    }
}



@Composable
fun FavoriteBooks(
    books: List<Book>,
    favoriteAction: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(books) {book ->
            ThumbnailCard(
                book = book,
                favoriteAction = favoriteAction,
            )
        }
    }
}
