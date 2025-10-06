package com.managr.app.personal.data.local.converters

import androidx.room.TypeConverter
import com.managr.app.personal.data.local.entity.AIQueryType

class AIQueryTypeConverter {
    @TypeConverter
    fun fromAIQueryType(value: AIQueryType): String {
        return value.name
    }

    @TypeConverter
    fun toAIQueryType(value: String): AIQueryType {
        return AIQueryType.valueOf(value)
    }
}
