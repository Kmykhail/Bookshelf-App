package com.kote.bookshelf.ui

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kote.bookshelf.BookshelfApplication
import com.kote.bookshelf.data.BookshelfRepository
import com.kote.bookshelf.model.BookItem
import com.kote.bookshelf.model.Bookshelf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

enum class ResponseState{
    Success, Error, Loading
}

data class BookshelfUiState(
    val bookshelf: Bookshelf? = null,
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
                    _bookshelfState.update {
                        it.copy(bookshelf = validResponse, responseState = ResponseState.Success)
                    }
                } catch (e: IOException) {
                    _bookshelfState.update {
                        it.copy(bookshelf = null, responseState = ResponseState.Error)
                    }
                } catch (e: HttpException) {
                    _bookshelfState.update {
                        it.copy(bookshelf = null, responseState = ResponseState.Error)
                    }
                }
            }
        }
    }

    fun sort() {
        _bookshelfState.value.bookshelf?.sortByTitle(sortState = _sortState.value)
        _sortState.value = !_sortState.value
        Log.d("BookshelfApp", "sort state:$_sortState")
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