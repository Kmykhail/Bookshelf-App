package com.kote.bookshelf.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book", indices = [androidx.room.Index(value = ["isFavorite"])])
data class Book(
    @PrimaryKey val id: String,
    val title: String,
    val authors: List<String>,
    val thumbnail: String,
    val descriptor: String,
    val isFavorite: Boolean,
)