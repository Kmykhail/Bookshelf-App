@file:OptIn(ExperimentalMaterial3Api::class)

package com.kote.bookshelf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kote.bookshelf.ui.BookshelfViewModel
import com.kote.bookshelf.ui.MainScreen
import com.kote.bookshelf.ui.theme.BookshelfTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookshelfTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BookshelfApp()
                }
            }
        }
    }
}

@Composable
fun BookshelfApp() {
//    Scaffold(
//        topBar = { BookshelfTopBar()}
//    ) {
        val bookshelfViewModel: BookshelfViewModel =
            viewModel(factory = BookshelfViewModel.Factory)
        val bookshelfState by bookshelfViewModel.bookshelfState.collectAsState()

        MainScreen(
            bookshelfViewModel = bookshelfViewModel,
            bookshelfUiState = bookshelfState,
//            contentPadding = it
        )
//    }
}

@Composable
fun BookshelfTopBar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    )
}