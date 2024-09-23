package com.kote.bookshelf.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Bookshelf(
    val items: List<BookItem> = emptyList()
) {
    fun getBookNumber() = items.size
    fun getBooks() : List<BookItem> = items
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
    val publisher: String? = null,
    val publishedDate: String? = null,
    val description: String? = null,
    val pageCount: Int? = null,
    val printType: String? = null,
    val categories: List<String>? = null,
    val imageLinks: ImageLinks? = null, // Nullable if images are missing
    val language: String,
    val previewLink: String,
    val infoLink: String,
    val canonicalVolumeLink: String
) {
    private fun isCompleted(): Boolean {
        return authors.isNotEmpty() &&
                publisher != null &&
                pageCount != null &&
                printType != null &&
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

@Serializable
data class TestInfo(
    val kind: String,
    val id: String,
    val selfLink: String,
    val etag: String,
)