package com.appdirect.integration.scenarios

import com.appdirect.integration.file.Resource
import com.appdirect.integration.scenario.Scenario
import com.appdirect.integration.scenario.ScenarioRunner
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.givenThat
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import org.apache.http.HttpHeaders
import org.apache.http.entity.ContentType
import org.springframework.social.partnercenter.api.order.subscription.Subscription
import java.util.UUID


class SubscriptionOrderHappyPathScenario(port: Int, validate: (scenario: Scenario) -> Unit) : ScenarioRunner(port, validate) {
    init {
        val properties = mapOf(Pair("customerId", UUID.randomUUID()))
        scenario = Scenario(properties, port, { setMocks(properties) }, { true }, validate)

        execution = {
//            scenario.restResource.request()
//                    .pathSegment("v1/customers/${scenario.properties["customerId"]}/orders")
//                    .post(Resource.parseFile("data/order/create.json").text, Subscription::class.java)
            scenario.restResource.request()
                    .path("info")
                    .get(String::class.java)
        }
    }

    private fun setMocks(properties: Map<String, Any>) {
        mockCreateOrder(properties)
    }

    private fun mockCreateOrder(properties: Map<String, Any>) {
        givenThat(post(urlEqualTo("/v1/customers/${properties["customerId"]}/orders"))
                .withRequestBody(equalToJson(Resource.parseFile("data/order/create.json").text))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())
                        .withBody(Resource.parseFile("data/subscription/ok.json").text)))
    }
}