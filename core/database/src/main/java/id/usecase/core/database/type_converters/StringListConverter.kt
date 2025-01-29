package id.usecase.core.database.type_converters

import androidx.room.TypeConverter

class StringListConverter {
    @TypeConverter
    fun fromString(value: String): List<String> {
        if (value.isEmpty()) return emptyList()
        return value.split(",")
    }

    @TypeConverter
    fun toString(list: List<String>): String {
        return list.joinToString(",")
    }
}