package com.kote.bookshelf.network

import com.kote.bookshelf.model.Bookshelf
//import com.kote.bookshelf.model.TestInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookshelfApiService {
    @GET("volumes/")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 20
    ): Bookshelf

//    @GET("volumes/{id}")
//    suspend fun getSpecificBook(@Path("id") id: String): TestInfo
}