package com.kote.bookshelf.ui

import android.util.Log
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
import com.kote.bookshelf.model.Bookshelf
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class BookshelfViewModel(private val bookshelfRepository: BookshelfRepository) : ViewModel() {
    var data: String by mutableStateOf("")
        private set

    init {
        getBookshelf()
    }

    fun getBookshelf() {
        viewModelScope.launch {
            try {
                val info = bookshelfRepository.testRequest("mass effect")
                println("CHECK, ${info.items?.size}")

//                info.enqueue(object : Callback<Bookshelf> {
//                    override fun onResponse(call: Call<Bookshelf>, response: Response<Bookshelf>) {
//                        if (response.isSuccessful) {
//                            val books = response.body()?.items
//                            if (books != null) {
//                                data = books.size.toString()
//                                Log.d("MainActivity", "Success, ${books.size} books")
//                            }
//                        } else {
//                            Log.e("MainActivity", "Error: ${response.code()}")
//                        }
//                    }
//
//                    override fun onFailure(call: Call<Bookshelf>, t: Throwable) {
//                        Log.e("MainActivity", "Failure: ${t.message}")
//                    }
//                })
            } catch (e: IOException) {
                Log.d("MainActivity", "IOException: ${e}")
            } catch (e: HttpException) {
                Log.d("MainActivity", "HttpException: ${e}")
            }
            Log.d("MainActivity", "finished")
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