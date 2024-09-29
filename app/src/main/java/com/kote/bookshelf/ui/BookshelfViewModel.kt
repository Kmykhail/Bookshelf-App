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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
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
    val bookshelfState: StateFlow<BookshelfUiState> = _bookshelfState

    private var _sortState = MutableStateFlow(false)
    val sortState: StateFlow<Boolean> = _sortState

    // StateFlow to observe the list of favorite books
    val favoriteBooks: StateFlow<List<Book>> = bookshelfRepository.getFavoriteBooks()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

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
                val updatedBook = book.copy(isFavorite = !book.isFavorite)
                _bookshelfState.update { currentState ->
                    val updatedBooks = currentState.bookItems.map {
                        if (it.id == book.id) updatedBook else it
                    }
                    currentState.copy(bookItems = updatedBooks)
                }
                bookshelfRepository.updateBook(updatedBook)
            }
        }
    }

    fun sortByTitle() {
        Log.d("BookshelfView", "sortByTitle triggered")
        val sortedByTitle = if (_sortState.value) {
            _bookshelfState.value.bookItems.sortedByDescending { it.title } // Sort in descending order
        } else {
            _bookshelfState.value.bookItems.sortedBy { it.title } // Sort in ascending order
        }

        _bookshelfState.update { currentState ->
            currentState.copy(bookItems = sortedByTitle)
        }

        _sortState.value = !_sortState.value
        Log.d("BookshelfView", "sortState is $_sortState\n ${_bookshelfState.value.bookItems.forEach { it.title }}")
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