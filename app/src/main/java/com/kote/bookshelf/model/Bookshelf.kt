package com.kote.bookshelf.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Bookshelf(
    val items: List<BookItem>? = null
)

@Serializable
data class BookItem (
    val id: String
//    val volumeInfo: VolumeInfo?
)

@Serializable
data class VolumeInfo (
    val title: String,
//    val authors: List<String>? = null, // Nullable in case no authors are provided
//    val publisher: String? = null,
//    val publishedDate: String? = null,
//    val description: String? = null,
//    val pageCount: Int? = null,
//    val printType: String? = null,
//    val categories: List<String>? = null,
//    val imageLinks: ImageLinks? = null, // Nullable if images are missing
//    val language: String,
//    val previewLink: String,
//    val infoLink: String,
//    val canonicalVolumeLink: String
)
//
//@Serializable
//data class ImageLinks(
//    val smallThumbnail: String? = null,
//    val thumbnail: String? = null
//)
//
@Serializable
data class TestInfo(
    val kind: String,
    val id: String,
    val selfLink: String,
    val etag: String,
)