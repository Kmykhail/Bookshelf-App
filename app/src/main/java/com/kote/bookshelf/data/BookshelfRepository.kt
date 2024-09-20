package com.kote.bookshelf.data

import com.kote.bookshelf.model.Bookshelf
import com.kote.bookshelf.model.TestInfo
import com.kote.bookshelf.network.BookshelfApiService
import retrofit2.Call
import retrofit2.http.Path
import retrofit2.http.Query

interface BookshelfRepository {
    suspend fun searchBooks(@Query("q") query: String): Call<Bookshelf>
    suspend fun getSpecificBook(@Path("id") param: String): TestInfo
    suspend fun testRequest(@Query("q") query: String): Bookshelf
}

class NetworkBookshelfRepository(private val bookshelfApiService: BookshelfApiService) : BookshelfRepository {
    override suspend fun searchBooks(query: String): Call<Bookshelf> = bookshelfApiService.searchBooks(query)
    override suspend fun getSpecificBook(id: String): TestInfo = bookshelfApiService.getSpecificBook(id)
    override suspend fun testRequest(query: String): Bookshelf = bookshelfApiService.testRequest(query)
}