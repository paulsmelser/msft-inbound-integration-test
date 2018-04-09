package com.appdirect.integration.scenarios

import com.appdirect.integration.file.Resource
import com.appdirect.integration.file.Resource.Companion.parseFile
import com.appdirect.integration.http.RestResourceFactory.createOAuthResource
import com.appdirect.integration.scenario.ISVEventMocks.mockSubscriptionOrderEventEndpoints
import com.appdirect.integration.scenario.OfferMocks.mockOffersForMarket
import com.appdirect.integration.scenario.Scenario
import com.appdirect.integration.scenario.ScenarioRunner
import com.appdirect.integration.scenario.microsoft.CustomerMocks.mockGetCustomer
import com.appdirect.integration.scenario.microsoft.MicrosoftOAuthMocks.mockMicrosoftOAuthEndpoints
import com.appdirect.integration.scenario.microsoft.OrderMocks.mockOrderCreationWithSuccessfulProvisioning
import com.appdirect.integration.scenario.microsoft.SubscriptionMocks
import com.appdirect.integration.scenario.microsoft.SubscriptionMocks.mockGetSubscription
import com.appdirect.integration.scenario.microsoft.UserMocks.mockAssignUser
import com.appdirect.integration.scenario.microsoft.UserMocks.mockCreateUser
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.givenThat
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import org.apache.commons.lang3.RandomStringUtils
import org.apache.http.HttpHeaders
import org.apache.http.entity.ContentType
import org.springframework.core.ParameterizedTypeReference
import org.springframework.social.partnercenter.api.order.offer.Offer
import java.util.UUID


class SubscriptionOrderHappyPathScenario(port: Int, validate: (scenario: Scenario<Payload>) -> Unit)
    : ScenarioRunner<SubscriptionOrderHappyPathScenario.Payload>(createOAuthResource(port, "office-365-enterprise-322", "BlPkzMNVp2DYdgYu"), validate) {
    var mockOAuth: () -> Unit = { mockMicrosoftOAuthEndpoints() }
    var mockGetSubscription: (scenario: Scenario<Payload>) -> Unit = { scenario ->  SubscriptionMocks.mockGetSubscription(scenario.payload.customerId, scenario.payload.subscriptionId, scenario.payload.orderId) }
    init {
        scenario = Scenario(
                restResource,
                Payload(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString()),
                { scenario -> mockFor(scenario) },
                { scenario -> preconditions(scenario) },
                { scenario ->  executionComplete(scenario) },
                validate,
                timeout)

        execution = {
            restResource.request()
                    .path("/api/v1/integration/processEvent")
                    .queryParam("eventUrl", "http://docker.for.mac.localhost:8899/isv/event/${scenario.payload.eventToken}")
                    .queryParam("applicationUuid", "b29602c1-e1a9-442a-bfd3-442a43a2cc89")
                    .get(String::class.java)
        }
    }

    private fun mockFor(scenario: Scenario<Payload>) {
        println(if (System.getenv("docker.host.os").isNullOrEmpty()) "osx" else System.getenv("docker.host.os"))
        mockOAuth.invoke()
        mockGetSubscription.invoke(scenario)
        mockGetCustomer(scenario.payload.customerId)
        mockOrderCreationWithSuccessfulProvisioning(scenario.payload.customerId, scenario.payload.orderId, scenario.payload.subscriptionId)
        mockCreateUser(scenario.payload.customerId)
        mockAssignUser(scenario.payload.customerId)
        mockCreateOrder(scenario)
        mockEventUrl(
                scenario.payload.eventToken,
                scenario.payload.customerId,
                scenario.payload.companyId,
                "${RandomStringUtils.random(8, true, true)}.onmicrosoft.com")
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

    private fun mockEventUrl(eventToken: String, customerId: String, companyId: String, customerDomain: String) {
        mockSubscriptionOrderEventEndpoints(eventToken, customerId, companyId, customerDomain)
    }

    private fun preconditions(scenario: Scenario<Payload>): Boolean {
            return try {
//                this.restResource.request()
//                        .path("api/v1/offers")
//                        .post(Resource.parseFile("data/offer/e1.json").getJsonAsObject<Offer>(), Offer::class.java)

                this.restResource.request()
                        .path("api/v1/offers/91FD106F-4B2C-4938-95AC-F54F74E9A239")
                        .queryParam("resellerDomain", "csptip58s.onmicrosoft.com")
                        .get(String::class.java)
                true
            } catch (e: Exception) {
                false
            }
    }

    private fun executionComplete(scenario: Scenario<Payload>) : Boolean {
            return try {
                val entity = this.restResource.request()
                        .path("api/v1/events/isvevents/bytoken/${scenario.payload.eventToken}")
                        .get(object : ParameterizedTypeReference<List<ISVEvent>>() {})
                entity.body.any { it.eventType == "SUBSCRIPTION_ORDER_SUCCESSFUL_EVENT" || it.eventType == "I_S_V_EVENT_FAILED_EVENT" }
            } catch (e: Exception) {
                false
            }
    }

    data class Payload(
            val companyId: String,
            val customerId: String,
            val orderId: String,
            val subscriptionId: String,
            val eventToken: String)
}