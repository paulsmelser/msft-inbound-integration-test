package com.appdirect.integration.scenarios.messagecontracts

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import java.time.LocalDateTime
import java.util.UUID

data class ISVEvent(@JsonProperty("id") val id: UUID,
                 @JsonProperty("eventToken") val eventToken: UUID,
                 @JsonProperty("timestamp") val timestamp: LocalDateTime,
                 @JsonProperty("eventType") val eventType: String,
                 @JsonProperty("payload") val payload: JsonNode,
                 @JsonProperty("headers") val headers: Map<String, Any>) {
    companion object {
        fun of(id: UUID, eventToken: UUID, timestamp: LocalDateTime, eventType: String, payload: JsonNode, headers: Map<String, Any>): ISVEvent {
            return ISVEvent(id, eventToken, timestamp, eventType, payload, headers)
        }
    }
}