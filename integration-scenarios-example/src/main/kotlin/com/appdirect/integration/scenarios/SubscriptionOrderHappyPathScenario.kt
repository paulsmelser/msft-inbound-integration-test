package com.appdirect.integration.scenarios

import com.appdirect.integration.file.Resource.Companion.parseFile
import com.appdirect.integration.http.RestResourceFactory.createOAuthResource
import com.appdirect.integration.mock.ISVEventMocks.mockEventEndpoints
import com.appdirect.integration.mock.microsoft.MicrosoftOAuthMocks.mockMicrosoftOAuthEndpoints
import com.appdirect.integration.mock.OfferMocks.mockOffersForMarket
import com.appdirect.integration.mock.Scenario
import com.appdirect.integration.mock.ScenarioRunner
import com.appdirect.integration.mock.microsoft.Customers.mockGetCustomer
import com.appdirect.integration.mock.microsoft.Orders.mockOrderCreationWithSuccessfulProvisioning
import com.appdirect.integration.mock.microsoft.Users.mockAssignUser
import com.appdirect.integration.mock.microsoft.Users.mockCreateUser
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.givenThat
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import org.apache.http.HttpHeaders
import org.apache.http.entity.ContentType
import org.springframework.core.ParameterizedTypeReference
import java.util.UUID


class SubscriptionOrderHappyPathScenario(port: Int, validate: (scenario: Scenario<Payload>) -> Unit)
    : ScenarioRunner<SubscriptionOrderHappyPathScenario.Payload>(createOAuthResource(port, "office-365-enterprise-322", "BlPkzMNVp2DYdgYu"), validate) {
    init {
        scenario = Scenario(
                restResource,
                Payload("0b50b2d5-30d2-4e6b-b3f9-2bc105e878b8", UUID.randomUUID().toString()),
                { scenario -> setMocks(scenario) },
                { scenario -> preconditions(scenario) },
                { scenario ->  ready(scenario) },
                validate,
                timeout)

        execution = {
            restResource.request()
                    .path("/api/v1/integration/processEvent")
                    .queryParam("eventUrl", "http://localhost:8899/isv/event/${scenario.payload.eventToken}")
                    .queryParam("applicationUuid", "b29602c1-e1a9-442a-bfd3-442a43a2cc89")
                    .get(String::class.java)
        }
    }

    private fun setMocks(scenario: Scenario<Payload>) {
        mockMicrosoftOAuthEndpoints()
        mockGetCustomer(scenario.payload.customerId)
        mockOrderCreationWithSuccessfulProvisioning(scenario.payload.customerId)
        mockCreateUser(scenario.payload.customerId)
        mockAssignUser(scenario.payload.customerId)
        mockCreateOrder(scenario)
        mockEventUrl(scenario.payload.eventToken)
        mockEventResolution()
        mockOffers()
    }

    private fun mockCreateOrder(scenario: Scenario<Payload>) {
        givenThat(post(urlEqualTo("/v1/customers/${scenario.payload.customerId}/orders"))
                .withRequestBody(equalToJson(parseFile("data/order/create.json").text))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())
                        .withBody(parseFile("data/subscription/ok.json").text)))
    }

    private fun mockEventResolution() {
        givenThat(post(urlEqualTo("/api/integration/v1/events/event/result"))
                .willReturn(aResponse().withStatus(200)))
    }

    private fun mockOffers() {
        mockOffersForMarket("AU")
        mockOffersForMarket("US")
    }

    private fun mockEventUrl(eventToken: String) {
        mockEventEndpoints(ID, eventToken)
    }

    private fun preconditions(scenario: Scenario<Payload>): Boolean {
        val start = System.currentTimeMillis()
        while(true){
            return try {
                this.restResource.request()
                        .path("api/v1/offers/91FD106F-4B2C-4938-95AC-F54F74E9A239")
                        .queryParam("resellerDomain", "csptip58s.onmicrosoft.com")
                        .get(String::class.java)
                Thread.sleep(5000)
                true
            } catch (e: Exception) {
                if ((System.currentTimeMillis() - start) > 40000) throw e
                false
            }
        }
    }

    private fun ready(scenario: Scenario<Payload>) : Boolean {
        val start = System.currentTimeMillis()
        while(true){
            return try {
                val entity = this.restResource.request()
                        .path("api/v1/events/isvevents/bytoken/${scenario.payload.eventToken}")
                        .get(object : ParameterizedTypeReference<List<ISVEvent>>() {})
                if (entity.body.any { it.eventType.equals("SUBSCRIPTION_ORDER_SUCCESSFUL_EVENT", true) }) return true
                Thread.sleep(5000)
                false
            } catch (e: Exception) {
                if ((System.currentTimeMillis() - start) > 40000) throw e
                false
            }
        }
    }

    data class Payload(val customerId: String, val eventToken: String)

    companion object {
        private const val ID: String = "S001"
    }
}