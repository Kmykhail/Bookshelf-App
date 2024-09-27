package com.kote.bookshelf.util

import androidx.room.TypeConverter
import com.kote.bookshelf.model.VolumeInfo
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DaoTypeConverter {
    @TypeConverter
    fun fromAuthorsList(authors: List<String>): String? {
        return authors?.joinToString(",") // Convert List<String> to a String
    }

    @TypeConverter
    fun toAuthorsList(authorsString: String?): List<String>? {
        return authorsString?.split(",") // Convert String back to List<String>
    }
}