package com.appdirect.jackson.json

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import java.io.IOException

class JsonConverter {
	val objectMapper: ObjectMapper

    constructor() {
        objectMapper = JsonSerializationSettings.builder()
                .with(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .with(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
                .with(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .with(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
                .with(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .with(MapperFeature.DEFAULT_VIEW_INCLUSION, false)
                .with(JavaTimeModule())
                .build().createMapper()
    }

    constructor(serializationSettings: JsonSerializationSettings) {
        this.objectMapper = serializationSettings.createMapper()
    }

    fun <T> fromJson(jsonString: String, targetClass: Class<T>): T {
        try {
            val reader = objectMapper
                    .reader()
                    .forType(targetClass)

            return reader.readValue(jsonString)

        } catch (e: IOException) {
            throw JsonSerializationException(e)
        }

    }

    fun <T> fromJson(jsonString: String, targetClass: TypeReference<*>): T {
        try {
            return objectMapper.readValue(jsonString, targetClass)

        } catch (e: IOException) {
            throw JsonSerializationException(e)
        }

    }

    fun <T> fromJson(jsonString: String, targetClass: Class<T>, serializationSettings: JsonSerializationSettings): T {
        try {
            val reader = serializationSettings.createMapper()
                    .reader()
                    .forType(targetClass)

            return reader.readValue(jsonString)

        } catch (e: IOException) {
            throw JsonSerializationException(e)
        }

    }

	inline fun <reified T> fromJson(jsonString: String): T {
		try {
			val reader = objectMapper.reader()
					.forType(T::class.java)

			return reader.readValue(jsonString)

		} catch (e: IOException) {
			throw JsonSerializationException(e)
		}

	}

	inline fun <reified T> fromJson(jsonString: String, serializationSettings: JsonSerializationSettings): T {
		try {
			val reader = serializationSettings.createMapper()
					.reader()
					.forType(T::class.java)

			return reader.readValue(jsonString)

		} catch (e: IOException) {
			throw JsonSerializationException(e)
		}

	}

    fun <T> toJson(objectToSerialize: T): String {
        return try {
			objectMapper.writer().writeValueAsString(objectToSerialize)
        } catch (e: JsonProcessingException) {
            throw JsonSerializationException(e)
        }

    }

    fun <T> toJson(objectToSerialize: T, serializationSettings: JsonSerializationSettings): String {
        return try {
			serializationSettings.createMapper().writer().writeValueAsString(objectToSerialize)
        } catch (e: JsonProcessingException) {
            throw JsonSerializationException(e)
        }

    }

    fun toJsonNode(json: String): JsonNode {
        return try {
            objectMapper.reader().readTree(json)
        } catch (e: IOException) {
            throw JsonSerializationException(e)
        }

    }

    fun toJsonNode(json: Any): JsonNode {
        return objectMapper.valueToTree(json)
    }
}
