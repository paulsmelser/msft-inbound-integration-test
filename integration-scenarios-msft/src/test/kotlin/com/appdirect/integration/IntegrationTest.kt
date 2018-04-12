package com.appdirect.integration

import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.meltwater.docker.compose.DockerCompose
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Rule

open class IntegrationTest {
    @Rule
    fun wireMockRule(): WireMockRule = wireMockRule

    private val wireMockRule = WireMockRule(WireMockConfiguration.options().port(MSFT_PORT))

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