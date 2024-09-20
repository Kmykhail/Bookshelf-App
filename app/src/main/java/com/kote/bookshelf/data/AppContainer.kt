package com.kote.bookshelf.data
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.kote.bookshelf.network.BookshelfApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val bookshelfRepository: BookshelfRepository
}

class DefaultAppContainer: AppContainer{
    private val baseUrl = "https://www.googleapis.com/books/v1/"

    private val json = Json{
        ignoreUnknownKeys = true
        isLenient = true
//        classDiscriminator = "type"
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: BookshelfApiService by lazy {
        retrofit.create(BookshelfApiService::class.java)
    }

    override val bookshelfRepository: BookshelfRepository by lazy {
        NetworkBookshelfRepository(retrofitService)
    }
}