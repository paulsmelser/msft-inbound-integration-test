package com.appdirect.integration.scenario.mocks

import com.appdirect.integration.file.Resource.Companion.parseFile
import com.appdirect.integration.scenario.ScenarioContext
import com.appdirect.jackson.json.Json.toJson
import com.fasterxml.jackson.databind.node.ObjectNode
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.givenThat
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import org.apache.commons.lang3.RandomStringUtils
import org.apache.http.HttpHeaders
import org.apache.http.entity.ContentType

object ISVEventMocks {

    fun mockSubscriptionOrderEventEndpoints(scenarioContext: ScenarioContext) {
        mockSubscriptionOrderFetch(scenarioContext.eventToken, scenarioContext.customerId, scenarioContext.companyId, "${RandomStringUtils.random(8, true, true)}.onmicrosoft.com")
        mockResolveEvent(scenarioContext.eventToken)
    }

    private fun mockSubscriptionOrderFetch(eventToken: String, customerId: String, companyId: String, customerDomain: String) {
        val event = parseFile("data/isv-event/subscription_order.json")
                .getAsObjectNode()
                .put("id", eventToken)

        val configuration = event.path("payload").path("configuration") as ObjectNode
        configuration.put("MICROSOFT_MIGRATE_TENANT_ID", customerId)
        configuration.put("MICROSOFT_MIGRATE_TENANT_DOMAIN_KEY", customerDomain)

        (event.path("payload").path("company") as ObjectNode).put("uuid", companyId)

        givenThat(get(urlPathEqualTo("/isv/event/$eventToken"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())
                        .withBody(toJson(event))))
    }

    private fun mockResolveEvent(eventToken: String) {
        givenThat(post(urlEqualTo("/api/integration/v1/events/$eventToken/result")))
    }
}