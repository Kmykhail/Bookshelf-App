package com.kote.bookshelf.ui

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kote.bookshelf.BookshelfApplication
import com.kote.bookshelf.data.BookshelfRepository
import com.kote.bookshelf.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

enum class ResponseState{
    Success, Error, Loading
}

data class BookshelfUiState(
    val bookItems: List<Book> = emptyList(),
    val responseState: ResponseState = ResponseState.Loading
)

class BookshelfViewModel(private val bookshelfRepository: BookshelfRepository) : ViewModel() {
    private val _bookshelfState = MutableStateFlow(BookshelfUiState())
    private var _sortState: MutableState<Boolean> = mutableStateOf(false)
    val bookshelfState: StateFlow<BookshelfUiState> = _bookshelfState

    fun getBookshelf(userInput: String) {
        if (userInput.isNotEmpty()) {
            viewModelScope.launch {
                try {
                    val validResponse = bookshelfRepository.searchBooks(userInput)
                        val filteredBooks: List<Book> = validResponse.items.map { bookItem ->
                            Book(
                                id = bookItem.id,
                                title = bookItem.volumeInfo.title,
                                authors = bookItem.volumeInfo.authors,
                                thumbnail = bookItem.volumeInfo.getThumbnail() ?: "",
                                isFavorite = false
                            )
                    }
                    _bookshelfState.update {
                        it.copy(bookItems = filteredBooks, responseState = ResponseState.Success)
                    }

                    _bookshelfState.value.bookItems.forEach {book ->
                        bookshelfRepository.insertBook(book)
                    }
                } catch (e: IOException) {
                    _bookshelfState.update {
                        it.copy(responseState = ResponseState.Error)
                    }
                } catch (e: HttpException) {
                    _bookshelfState.update {
                        it.copy(responseState = ResponseState.Error)
                    }
                }
            }
        }
    }

    fun toggleFavorite(book: Book) {
        viewModelScope.launch {
            _bookshelfState.value.run {
                val updatedBooks = bookItems.map {
                    if (it.id == book.id) it.copy(isFavorite = !book.isFavorite) else it
                }
                _bookshelfState.update {
                    it.copy(bookItems = updatedBooks)
                }
            }

            val newBook = _bookshelfState.value.bookItems.find { it.id == book.id }
            if (newBook != null) {
                bookshelfRepository.updateBook(newBook)
            }
        }
    }

    fun testRoom() {
        viewModelScope.launch {
            try {
                val all = bookshelfRepository.getFavoriteBooks()
                Log.d("BookshelfApp", "list:${all}")
            } catch (e: Exception) {
                Log.d("BookshelfApp", "Ops new exception $e")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as BookshelfApplication)
                BookshelfViewModel(application.container.bookshelfRepository)
            }
        }
    }
}