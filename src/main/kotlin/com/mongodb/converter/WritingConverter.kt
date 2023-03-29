package com.mongodb.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter

@WritingConverter
class EnumToIntegerWritingConverter: Converter<Enum<*>, Int> {

      override fun convert(source: Enum<*>): Int {
        return if (source is EnumWithValue) {
            (source as EnumWithValue).value
        } else 0
    }

}