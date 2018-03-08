package com.appdirect.integration.scenario

import com.appdirect.web.client.RestClient
import com.appdirect.web.client.RestResource
import org.springframework.web.client.RestTemplate
import java.net.URI

open class Scenario(val properties: Map<String, Any> = emptyMap(), port: Int, private val mocks: () -> Unit, private val ready: () -> Boolean, private val validate: (scenario: Scenario) -> Unit) {
    val restResource: RestResource = RestClient(RestTemplate(), URI.create("http://localhost:$port"))

    fun execute(onExecute: (scenario: Scenario) -> Unit) {
        mocks.invoke()

        onExecute.invoke(this)
        while(!ready.invoke()) {
            Thread.sleep(3000)
        }

        validate.invoke(this)
    }
}