package id.usecase.core.database.type_converters

import androidx.room.TypeConverter

class IntListConverter {
    @TypeConverter
    fun fromString(value: String): List<Int> {
        if (value.isEmpty()) return emptyList()
        return value.split(",").map { it.toInt() }
    }

    @TypeConverter
    fun toString(list: List<Int>): String {
        return list.joinToString(",")
    }
}