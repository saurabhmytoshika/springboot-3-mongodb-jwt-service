package com.mongodb.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.core.convert.converter.ConverterFactory
import org.springframework.data.convert.ReadingConverter

class IntegerToEnumConverterFactory: ConverterFactory<Int, Enum<*>> {
    override fun <T : Enum<*>?> getConverter(targetType: Class<T>): Converter<Int, T> {
        var enumType = targetType
        while (enumType != null && !enumType.isEnum) {
            enumType = enumType.superclass as Class<T>
        }
        if (enumType == null) {
            throw IllegalArgumentException(
                    "The target class type " + targetType.name + " does not refer to an enum")
        }
        return IntegerToEnumReadingConverter(enumType)
    }

    @ReadingConverter
    class IntegerToEnumReadingConverter<T : Enum<*>?>(private val enumType: Class<T>?) : Converter<Int, T> {
        override fun convert(source: Int): T? {
            for (t in enumType?.enumConstants!!) {
                if (t is EnumWithValue) {
                    if ((t as EnumWithValue).value == source) {
                        return t
                    }
                }
            }
            return null
        }
    }

}

interface EnumWithValue {
    val value: Int
}