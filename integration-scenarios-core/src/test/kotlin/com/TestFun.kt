package com

import com.appdirect.integration.mock.Scenario
import com.appdirect.jackson.json.Json
import org.junit.Test

class TestFun {

    @Test
    fun test() {
        val scenario = Scenario(444, emptyMap(), {}, { true }, {}, 34555)
        val toJson = Json.toJson(scenario)
        val fromJson = Json.fromJson<Scenario>(toJson)
    }
}