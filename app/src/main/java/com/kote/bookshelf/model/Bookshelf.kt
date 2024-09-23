package com.kote.bookshelf.model

import android.util.Log
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Bookshelf(
    var items: MutableList<BookItem> = mutableListOf()
) {
    fun getBookNumber() = items.size
    fun getBooks() : List<BookItem> = items
    fun sortByTitle(sortState: Boolean) {
        when(sortState) {
            false -> items.sortBy { it.volumeInfo.title.lowercase() }
            true -> items.sortByDescending { it.volumeInfo.title.lowercase() }
        }
        Log.d("BookshelfApp", "sorted by title:\n")
        items.forEach{ bookItem ->
            Log.d("BookshelfApp", "${bookItem.volumeInfo.title}")
        }
    }
}

@Serializable
data class BookItem (
    val id: String,
    val volumeInfo: VolumeInfo
) {
    fun getThumbnail() : String? {
        return volumeInfo.getThumbnail()
    }
}

@Serializable
data class VolumeInfo (
    val title: String,
    val authors: List<String> = emptyList(), // Nullable in case no authors are provided
    val publisher: String,
    val publishedDate: String,
    val description: String,
    val pageCount: Int,
    val printType: String,
    val categories: List<String>? = null,
    val imageLinks: ImageLinks? = null, // Nullable if images are missing
    val language: String,
    val previewLink: String,
    val infoLink: String,
    val canonicalVolumeLink: String
) {
    private fun isCompleted(): Boolean {
        return authors.isNotEmpty() &&
                categories != null &&
                imageLinks != null
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