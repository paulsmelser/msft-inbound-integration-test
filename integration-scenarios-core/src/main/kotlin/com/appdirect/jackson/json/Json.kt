package com.appdirect.jackson.json

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode

object Json {
	var jsonConverter: JsonConverter = JsonConverter()

    fun converter(): JsonConverter {
        return JsonConverter()
    }

    fun converter(serializationSettings: JsonSerializationSettings): JsonConverter {
        return JsonConverter(serializationSettings)
    }

    fun <T> fromJson(jsonString: String, targetClass: Class<T>): T {
        return jsonConverter.fromJson(jsonString, targetClass)
    }

    fun <T> fromJson(jsonString: String, targetClass: TypeReference<*>): T {
        return jsonConverter.fromJson(jsonString, targetClass)
    }

    fun <T> fromJson(jsonString: String, targetClass: Class<T>, serializationSettings: JsonSerializationSettings): T {
        return jsonConverter.fromJson(jsonString, targetClass, serializationSettings)
    }

	inline fun <reified T> fromJson(jsonString: String): T {
		return jsonConverter.fromJson(jsonString)
	}

	inline fun <reified T> fromJson(jsonString: String, serializationSettings: JsonSerializationSettings): T {
		return jsonConverter.fromJson(jsonString, serializationSettings)
	}

    fun <T> toJson(objectToSerialize: T): String {
        return jsonConverter.toJson(objectToSerialize)
    }

    fun <T> toJson(objectToSerialize: T, serializationSettings: JsonSerializationSettings): String {
        return jsonConverter.toJson(objectToSerialize, serializationSettings)
    }

    fun toJsonNode(json: String): JsonNode {
        return jsonConverter.toJsonNode(json)
    }

    fun toJsonNode(json: Any): JsonNode {
        return jsonConverter.toJsonNode(json)
    }

}
