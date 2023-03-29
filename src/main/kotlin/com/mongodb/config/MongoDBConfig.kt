package com.mongodb.config

import com.mongodb.converter.EnumToIntegerWritingConverter
import com.mongodb.converter.IntegerToEnumConverterFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.convert.CustomConversions
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.convert.DbRefResolver
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper
import org.springframework.data.mongodb.core.convert.MappingMongoConverter
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import org.springframework.data.mongodb.core.mapping.MongoMappingContext

@Configuration
class MongoDBConfig {

    @Bean
    fun mongoTemplate(databaseFactory: MongoDatabaseFactory): MongoTemplate? {
        val mappingContext = MongoMappingContext()
        val dbRefResolver: DbRefResolver = DefaultDbRefResolver(databaseFactory)
        val mongoConverter = MappingMongoConverter(dbRefResolver, mappingContext)
        mongoConverter.typeMapper = DefaultMongoTypeMapper(null)
        val customConversion: CustomConversions? = customConversions()
        if (customConversion != null) {
            mongoConverter.customConversions = customConversion
        }
        mongoConverter.afterPropertiesSet()
        return MongoTemplate(databaseFactory, mongoConverter)
    }

    fun customConversions(): CustomConversions? {
        return MongoCustomConversions(
            listOf(
                EnumToIntegerWritingConverter(),
                IntegerToEnumConverterFactory()
            )
        )
    }
}
