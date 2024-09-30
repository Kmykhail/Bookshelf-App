package com.kote.bookshelf.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
                ThumbnailGridCard(
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
fun ThumbnailGridCard(
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
                painter = rememberVectorPainter(Icons.Default.Image),
                contentDescription = "no image",
                modifier = Modifier.fillMaxSize()
            )
            Log.e("ThumbnailCard", "Book without image: ${book.id}, ${book.title}, ${book.thumbnail}")
        }
    }
}

@Composable
fun FavoriteBooks(
    books: List<Book>,
    favoriteAction: (Book) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(books) {book ->
            ThumbnailFavoriteCard(
                book = book,
                favoriteAction = favoriteAction
            )
        }
    }
}

@Composable
fun ThumbnailFavoriteCard(
    book: Book,
    favoriteAction: (Book) -> Unit,
    modifier: Modifier = Modifier
) {
    val thumbnail = book.thumbnail
    var expanded by remember { mutableStateOf(false) }
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
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
                        tint = Color.Red,
                        modifier = modifier.fillMaxSize()
                    )
                }
            }
            Text(
                text = book.descriptor,
                textAlign = TextAlign.Justify,
                maxLines = if (expanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null)
//                Icon(text = if (expanded) "See less" else "See more")
            }
        }
    }
}