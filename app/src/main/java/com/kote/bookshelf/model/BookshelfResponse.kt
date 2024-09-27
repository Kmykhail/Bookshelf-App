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

@Entity
@Serializable
data class BookItem (
    @PrimaryKey val id: String,
    val volumeInfo: VolumeInfo,
    var isFavorite: Boolean = false
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