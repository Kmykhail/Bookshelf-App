package com.kote.bookshelf.model

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
data class BookshelfResponse(
    var items: MutableList<BookItem> = mutableListOf()
) {
    fun getBookNumber() = items.size
    fun getBooks() : List<BookItem> = items
}

@Serializable
data class BookItem (
    val id: String,
    val volumeInfo: VolumeInfo,
)

@Serializable
data class VolumeInfo (
    val title: String,
    val authors: List<String> = emptyList(), // Nullable in case no authors are provided ,
    val publisher: String? = null,
    val description: String? = null,
    val categories: List<String>? = null,
    val imageLinks: ImageLinks? = null, // Nullable if images are missing
) {
    fun isCompleted(): Boolean {
        return authors.isNotEmpty() &&
                categories != null &&
                imageLinks != null &&
                publisher != null &&
                description != null
    }

    fun getThumbnail(): String? {
        if (isCompleted()) {
            return imageLinks!!.thumbnail
        }
        return null
    }
}

@Serializable
data class ImageLinks(
    val smallThumbnail: String? = null,
    val thumbnail: String? = null
)