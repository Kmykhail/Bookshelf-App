package com.kote.bookshelf.data

import com.kote.bookshelf.database.BookDao
import com.kote.bookshelf.model.Book
import com.kote.bookshelf.model.BookItem
import com.kote.bookshelf.model.BookshelfResponse
import com.kote.bookshelf.network.BookshelfApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Query

interface BookshelfRepository {
    //    suspend fun getSpecificBook(@Path("id") param: String): TestInfo
    suspend fun getAll() : List<Book>
    fun getFavoriteBooks() : Flow<List<Book>>
    suspend fun searchBooks(@Query("q") query: String): BookshelfResponse
    suspend fun insertBook(book: Book)
    suspend fun updateBook(book: Book)
    suspend fun deleteBook(book: Book)
}

class GeneralBookshelfRepository(
    private val bookshelfApiService: BookshelfApiService,
    private val favoriteBooksDao: BookDao
) : BookshelfRepository {
    //    override suspend fun getSpecificBook(id: String): TestInfo = bookshelfApiService.getSpecificBook(id)
    override suspend fun searchBooks(query: String): BookshelfResponse = bookshelfApiService.searchBooks(query)

    override suspend fun getAll(): List<Book>  = favoriteBooksDao.getAll()
    override fun getFavoriteBooks(): Flow<List<Book>> = favoriteBooksDao.getFavoriteBooks()
    override suspend fun insertBook(book: Book) {
        favoriteBooksDao.insertBook(book)
    }
    override suspend fun updateBook(book: Book) {
        favoriteBooksDao.updateBook(book)
    }
    override suspend fun deleteBook(book: Book) {
        favoriteBooksDao.deleteBook(book)
    }
}