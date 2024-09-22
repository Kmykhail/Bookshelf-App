@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.kote.bookshelf.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kote.bookshelf.R
import com.kote.bookshelf.ui.theme.BookshelfTheme

@Composable
fun MainScreen(
    bookshelfViewModel: BookshelfViewModel,
    bookshelfUiState: BookshelfUiState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    var search by remember { mutableStateOf("") }
    println("Search value: $search")
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
        CustomSearchField(
            search = search,
            onValueChanged = {search = it},
        )
        BookshelfBar()
        BookshelfGrid()
    }
}

@Composable
fun CustomSearchField(
    search: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {
        TextField(
            value = search,
            onValueChange = onValueChanged,
            placeholder = {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "")
                    Text(text = "Search book")
                }
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            )
        )
    }
}

@Composable
fun BookshelfBar(
    bookNumber: Int = 0,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "$bookNumber BOOKS",
            style = MaterialTheme.typography.labelLarge.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            ),
            modifier = modifier.weight(1f),
        )

        TextButton(onClick = { /*TODO*/ }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "SORT", style = MaterialTheme.typography.labelLarge)
                Icon(
                    imageVector = Icons.Default.Sort,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun BookshelfGrid(
    thumbnailList: List<String> = emptyList(),
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 128.dp)) {
        items(thumbnailList) {thumbnail ->
            ThumbnailCard(thumbnail = thumbnail)
        }
    }
}

@Composable
fun ThumbnailCard(
    thumbnail: String,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.small
    ) {
        AsyncImage(
            model = thumbnail,
            contentDescription = "name",
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.loading_img)
        )
    }
}

@Preview
@Composable
fun CustomSearchFieldPreviw() {
    BookshelfTheme {
        var search by remember { mutableStateOf("") }
        CustomSearchField(
            search = search,
            onValueChanged = {search = it}
        )
    }
}

@Preview
@Composable
fun BooksBarPreview() {
    val bookNumber = 42
    BookshelfTheme {
        BookshelfBar(bookNumber = bookNumber)
    }
}

@Preview
@Composable
fun ThumbnailCardPreview() {
    BookshelfTheme {
        ThumbnailCard(
            thumbnail = "http://books.google.com/books/publisher/content?id=AYyjCgAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&imgtk=AFLRE73SyseyQMO9d7m0xeREdQgOWVPzybSpObk13E9hFxIv9Wb4IgNLrA9jIwzsRTbBoNxuoDCqJo0zuK892hSAsHJakjTT0ClPhR1Bl1UFy_VIAlTpL0nsn88zwWJyMYB-L2vJ-JNN&source=gbs_api"
        )
    }
}