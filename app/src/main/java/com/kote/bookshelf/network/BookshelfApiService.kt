package com.kote.bookshelf.network

//import com.kote.bookshelf.model.TestInfo
import com.kote.bookshelf.model.BookshelfResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BookshelfApiService {
    @GET("volumes/")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 20
    ): BookshelfResponse

//    @GET("volumes/{id}")
//    suspend fun getSpecificBook(@Path("id") id: String): TestInfo
}