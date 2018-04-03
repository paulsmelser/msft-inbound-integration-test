package com.appdirect.docker

import com.fasterxml.jackson.annotation.JsonProperty


class ComposeService (@JsonProperty("image") val image: String,
                      @JsonProperty("ports") var ports: List<String>? = emptyList(),
                      @JsonProperty("depends_on") var dependsOn: List<String>? = emptyList(),
                      @JsonProperty("networks") var networks: List<String>? = emptyList(),
                      @JsonProperty("environment") var environment: Map<String, String>? = emptyMap(),
                      @JsonProperty("command") var command: String? = null)