package com.kote.bookshelf.data
import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.kote.bookshelf.database.BookDao
import com.kote.bookshelf.database.BookshelfDatabase
import com.kote.bookshelf.network.BookshelfApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val bookshelfRepository: BookshelfRepository
}

class DefaultAppContainer(context: Context): AppContainer {
    // Retrofit
    private val baseUrl = "https://www.googleapis.com/books/v1/"

    private val json = Json{
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: BookshelfApiService by lazy {
        retrofit.create(BookshelfApiService::class.java)
    }

    // Local database for favorite books
    private val favoriteDatabase: BookshelfDatabase by lazy {
        BookshelfDatabase.getDatabase(context)
    }

    private val favoriteDao : BookDao by lazy {
        favoriteDatabase.favoriteBooksDao()
    }

    // Initialize the repository combining both Room and Retrofit
    override val bookshelfRepository: BookshelfRepository by lazy {
        GeneralBookshelfRepository(retrofitService, favoriteDao)
    }
}