package com.managr.app.core.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

/**
 * Type converters for Room database
 * Handles conversion of complex types to/from database-compatible types
 */
class Converters {
    private val gson = Gson()

    // LocalDate converters
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it) }
    }

    // LocalDateTime converters
    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? {
        return dateTime?.toString()
    }

    @TypeConverter
    fun toLocalDateTime(dateTimeString: String?): LocalDateTime? {
        return dateTimeString?.let { LocalDateTime.parse(it) }
    }

    // YearMonth converters
    @TypeConverter
    fun fromYearMonth(yearMonth: YearMonth?): String? {
        return yearMonth?.toString()
    }

    @TypeConverter
    fun toYearMonth(yearMonthString: String?): YearMonth? {
        return yearMonthString?.let { YearMonth.parse(it) }
    }

    // List<String> converters
    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toStringList(json: String?): List<String>? {
        if (json == null) return null
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type)
    }

    // List<Long> converters
    @TypeConverter
    fun fromLongList(list: List<Long>?): String? {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toLongList(json: String?): List<Long>? {
        if (json == null) return null
        val type = object : TypeToken<List<Long>>() {}.type
        return gson.fromJson(json, type)
    }
}
