package com.kote.bookshelf.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kote.bookshelf.model.Book
import com.kote.bookshelf.util.DaoTypeConverter

@Database(entities = [Book::class], version = 3, exportSchema = false)
@TypeConverters(DaoTypeConverter::class)
abstract class BookshelfDatabase: RoomDatabase() {
    abstract fun favoriteBooksDao() : BookDao

    companion object {
        @Volatile
        private var favoriteBooksDaoInstance: BookshelfDatabase? = null

        fun getDatabase(context: Context): BookshelfDatabase {
            return (favoriteBooksDaoInstance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    BookshelfDatabase::class.java,"database-favorite"
                ).build()
                favoriteBooksDaoInstance = instance
                instance
            })
        }
    }
}