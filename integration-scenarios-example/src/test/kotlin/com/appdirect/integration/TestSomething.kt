package com.appdirect.integration

import com.appdirect.integration.scenario.TestRun
import com.appdirect.integration.scenarios.Subscription
import com.appdirect.integration.scenarios.SubscriptionOrderScenario
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.meltwater.docker.compose.DockerCompose
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.springframework.core.ParameterizedTypeReference


class TestSomething {
    @Rule
    fun wireMockRule(): WireMockRule = wireMockRule

    private val wireMockRule = WireMockRule(options().port(MSFT_PORT))

    @Test
    fun `When subscription order is placed for a new company then subscription then subscription is purchase and saved successfully`() {
        TestRun.startWith(SubscriptionOrderScenario(
                8888,
                {
                    it.restResource.request()
                            .path("api/v1/subscriptions")
                            .queryParam("subscriptionUUID", it.context.subscriptionId)
                            .get(object : ParameterizedTypeReference<Subscription>() {})
                })).execute()
    }

    companion object {
        private lateinit var container: DockerCompose
        const val MSFT_PORT = 8899

        @BeforeClass
        @JvmStatic
        fun init() {
            container = DockerCompose(getDockerFile(), "msft", HashMap())
            container.build()
            container.up()
        }

        @AfterClass
        @JvmStatic
        fun cleanup() {
            container.kill()
            container.rm()
        }

        @JvmStatic
        private fun getDockerFile(): String {
            val dockerEnv = if (System.getenv("DOCKER_HOST_OS").isNullOrEmpty()) "osx" else System.getenv("DOCKER_HOST_OS")
            return if (dockerEnv == "osx") "docker-compose-osx.yml" else "docker-compose-linux.yml"
        }
    }
}
