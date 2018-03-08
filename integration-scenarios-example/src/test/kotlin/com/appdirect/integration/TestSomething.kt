package com.appdirect.integration

import com.appdirect.integration.scenario.Scenario
import com.appdirect.integration.scenarios.SubscriptionOrderHappyPathScenario
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.meltwater.docker.compose.DockerCompose
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import java.util.HashMap


class TestSomething {
    @Rule
    fun wireMockRule(): WireMockRule = wireMockRule

    private val wireMockRule = WireMockRule(options().port(MSFT_PORT))

    @Test
    fun `When subscription order is placed for a new company then subscription then subscription is purchase and saved successfully`() {
        val scenario = SubscriptionOrderHappyPathScenario(8888) { validate(it) }
        var isReady = false
        while(!isReady) {
            try {
                scenario.restResource().request().path("info").get(String::class.java)
                isReady = true
            } catch (e: Exception) {
                Thread.sleep(5000)
            }
        }
        scenario.execute()
    }

    private fun validate(scenario: Scenario) {
//        verify(postRequestedFor(urlEqualTo("/v1/customers/${scenario.properties["customerId"]}/orders")))
//        verify(getRequestedFor(urlEqualTo("info")))
    }

    companion object {
        var container: DockerCompose = DockerCompose("docker-compose.yml", "msft", HashMap())
        const val MSFT_PORT = 8899

        @BeforeClass
        @JvmStatic
        fun init() {
            container.up()
        }

        @AfterClass
        @JvmStatic
        fun cleanup() {
            container.kill()
            container.rm()
        }
    }
}
