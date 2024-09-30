@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.kote.bookshelf.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kote.bookshelf.R
import com.kote.bookshelf.ui.components.BookshelfBar
import com.kote.bookshelf.ui.components.BookshelfGrid
import com.kote.bookshelf.ui.components.SearchField
import com.kote.bookshelf.ui.theme.BookshelfTheme
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kote.bookshelf.ui.components.FavoriteBooks

sealed class Content(val route: String) {
    object SearchResults : Content("search_results")
    object FavoriteBooks : Content("favorite_books")
}

@Composable
fun MainScreen(
    bookshelfViewModel: BookshelfViewModel = viewModel(factory = BookshelfViewModel.Factory),
    bookshelfUiState: BookshelfUiState,
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val favoriteBooks by bookshelfViewModel.favoriteBooks.collectAsState()

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
            done = {navController.navigate(Content.SearchResults.route)},
            onSearchChange = {bookshelfViewModel.getBookshelf(it)}
        )

        BookshelfBar(
            showFavorites = { navController.navigate(Content.FavoriteBooks.route) },
            sortBooks = { bookshelfViewModel.sortByTitle() },
            bookNumber = bookshelfUiState.bookItems.size
        )

        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            NavHost(navController = navController, startDestination = Content.SearchResults.route) {
                composable(Content.SearchResults.route) {
                    when (bookshelfUiState.responseState) {
                        ResponseState.Success -> BookshelfGrid(
                            books = bookshelfUiState.bookItems,
                            favoriteAction = {bookshelfViewModel.toggleFavorite(it)}
                        )
                        ResponseState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
                        ResponseState.Error -> ErrorScreen(retryAction = {})
                    }
                }
                composable(Content.FavoriteBooks.route) {
                    FavoriteBooks(
                        books = favoriteBooks,
                        favoriteAction = {bookshelfViewModel.toggleFavorite(it)},
                        modifier = Modifier.fillMaxSize()
                    )
                }
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

@Preview
@Composable
fun SearchFieldPreview() {
    BookshelfTheme {
        var search by remember { mutableStateOf("") }
        SearchField(
            done = {},
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