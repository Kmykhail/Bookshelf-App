@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.kote.bookshelf.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.kote.bookshelf.R
import com.kote.bookshelf.model.Book
import com.kote.bookshelf.ui.components.BookshelfBar
import com.kote.bookshelf.ui.components.SearchField
import com.kote.bookshelf.ui.theme.BookshelfTheme

@Composable
fun MainScreen(
    bookshelfViewModel: BookshelfViewModel = viewModel(factory = BookshelfViewModel.Factory),
    bookshelfUiState: BookshelfUiState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
    ) {
        Text(
            text = "Library",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        SearchField(
            onSearchChange = {bookshelfViewModel.getBookshelf(it)}
        )

        BookshelfBar(
            bookshelfViewModel = bookshelfViewModel,
            favoriteAction = { bookshelfViewModel.testRoom() },
            bookNumber = bookshelfUiState.bookItems.size
        )

        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            when (bookshelfUiState.responseState) {
                ResponseState.Success -> BookshelfGrid(
                    books = bookshelfUiState.bookItems,
                    favoriteAction = {bookshelfViewModel.toggleFavorite(it)}
                )
                ResponseState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
                ResponseState.Error -> ErrorScreen(retryAction = {})
            }
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.loading_img),
        contentDescription = "Loading",
        modifier = modifier
    )
}

@Composable
fun ErrorScreen(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Failed to load", modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(text = "Retry")
        }
    }
}

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
    Log.d("MainScreen", "check thumbnail: $thumbnail")
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
                    Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint = if (book.isFavorite) Color.Red else Color.Gray)
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

@Preview
@Composable
fun SearchFieldPreview() {
    BookshelfTheme {
        var search by remember { mutableStateOf("") }
        SearchField(
            onSearchChange = {search = it}
        )
    }
}

@Preview
@Composable
fun BooksBarPreview() {
    val bookNumber = 42
    BookshelfTheme {
//        BookshelfBar(bookNumber = 10)
    }
}

//@Preview
//@Composable
//fun ThumbnailCardPreview() {
//    BookshelfTheme {
//        ThumbnailCard(
////            thumbnail = "http://books.google.com/books/publisher/content?id=AYyjCgAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&imgtk=AFLRE73SyseyQMO9d7m0xeREdQgOWVPzybSpObk13E9hFxIv9Wb4IgNLrA9jIwzsRTbBoNxuoDCqJo0zuK892hSAsHJakjTT0ClPhR1Bl1UFy_VIAlTpL0nsn88zwWJyMYB-L2vJ-JNN&source=gbs_api"
//        )
//    }
//}