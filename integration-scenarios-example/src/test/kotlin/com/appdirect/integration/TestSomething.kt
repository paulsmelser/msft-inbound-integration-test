package com.appdirect.integration

import com.appdirect.integration.mock.Scenario
import com.appdirect.integration.scenarios.Subscription
import com.appdirect.integration.scenarios.SubscriptionOrderHappyPathScenario
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
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate
import java.util.concurrent.TimeoutException


class TestSomething {
    @Rule
    fun wireMockRule(): WireMockRule = wireMockRule

    private val wireMockRule = WireMockRule(options().port(MSFT_PORT))

    @Test
    fun `When subscription order is placed for a new company then subscription then subscription is purchase and saved successfully`() {
        val scenario = SubscriptionOrderHappyPathScenario(8888, { validate(it) })

        val start = System.currentTimeMillis()
        while (!ready(scenario)) {
            Thread.sleep(5000)
            if ((System.currentTimeMillis() - start) > 120000) throw TimeoutException()
        }

        scenario.execute()
    }

    private fun validate(scenario: Scenario<SubscriptionOrderHappyPathScenario.Payload>) {
        verify(postRequestedFor(urlEqualTo("/v1/customers/${scenario.payload.customerId}/orders")))
        scenario.restResource.request()
                .path("api/v1/subscriptions")
                .queryParam("subscriptionUUID", "84a03d81-6b37-4d66-8d4a-faea24541538")
                .get(object: ParameterizedTypeReference<Subscription>(){})

    }

    private fun ready(scenario: SubscriptionOrderHappyPathScenario): Boolean {
        return try {
            RestTemplate().exchange("http://localhost:8888/info", HttpMethod.GET, HttpEntity<Any>(HttpHeaders()), String::class.java)
            true
        } catch (e: Exception) {
            false
        }
    }

    companion object {
        private var container: DockerCompose = DockerCompose("docker-compose.yml", "msft", HashMap())
        const val MSFT_PORT = 8899

        @BeforeClass
        @JvmStatic
        fun init() {
            container.build()
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
