package com.appdirect.docker

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL

@JsonInclude(NON_NULL)
class ComposeConfig {
    lateinit var version: String
    lateinit var services: Map<String, ComposeService>
    lateinit var networks: Map<String, ComposeNetwork>
}