package com.appdirect.integration.scenarios

import com.appdirect.integration.file.Resource
import com.appdirect.integration.http.RestResourceFactory
import com.appdirect.integration.scenario.Scenario
import com.appdirect.integration.scenario.ScenarioContext
import com.appdirect.integration.scenario.mocks.ISVEventMocks.mockSubscriptionOrderEventEndpoints
import com.appdirect.integration.scenario.mocks.microsoft.CustomerMocks.mockGetCustomer
import com.appdirect.integration.scenario.mocks.microsoft.MicrosoftOAuthMocks
import com.appdirect.integration.scenario.mocks.microsoft.OrderMocks.mockOrderCreationWithSuccessfulProvisioning
import com.appdirect.integration.scenario.mocks.microsoft.SubscriptionMocks.getSubscriptionMock
import com.appdirect.integration.scenario.mocks.microsoft.UserMocks.mockAssignUser
import com.appdirect.integration.scenario.mocks.microsoft.UserMocks.mockCreateUser
import org.springframework.core.ParameterizedTypeReference

open class SubscriptionOrderScenario(connectorPort: Int,
                                     validate: (scenario: Scenario<ScenarioContext>) -> Unit = {},
                                     timeout: Long = 60000,
                                     private val mockOAuthMock: () -> Unit = { MicrosoftOAuthMocks.mockMicrosoftOAuthEndpoints() },
                                     private val mockGetSubscriptionMock: (scenario: Scenario<ScenarioContext>) -> Unit = { getSubscriptionMock(it) },
                                     private val createUserMock: (scenario: Scenario<ScenarioContext>) -> Unit = { mockCreateUser(it.context.customerId) },
                                     private val assignUserMock: (scenario: Scenario<ScenarioContext>) -> Unit = { mockAssignUser(it.context.customerId) },
                                     private val getCustomerMock: (scenario: Scenario<ScenarioContext>) -> Unit = { mockGetCustomer(it.context.customerId) },
                                     private val createOrderMock: (scenario: Scenario<ScenarioContext>) -> Unit = { mockOrderCreationWithSuccessfulProvisioning(it.context) },
                                     private val appDirectEventMock: (scenario: Scenario<ScenarioContext>) -> Unit = { mockSubscriptionOrderEventEndpoints(it.context) }) : Scenario<ScenarioContext>(RestResourceFactory.createOAuthResource(connectorPort, "office-365-enterprise-322", "BlPkzMNVp2DYdgYu"), validate, timeout) {

    override fun setupMocks(scenario: Scenario<ScenarioContext>) {
        mockOAuthMock.invoke()
        mockGetSubscriptionMock.invoke(scenario)
        createUserMock.invoke(scenario)
        assignUserMock.invoke(scenario)
        getCustomerMock.invoke(scenario)
        createOrderMock.invoke(scenario)
        appDirectEventMock.invoke(scenario)
    }

    override fun setupTestData(scenario: Scenario<ScenarioContext>) {
        restResource.request()
                .path("api/v1/offers")
                .post(Resource.parseFile("data/offer/E1.json").getJsonAsObject<OfferWS>(), OfferWS::class.java)
    }

    override fun onExecute(scenario: Scenario<ScenarioContext>) {
        restResource.request()
                .path("/api/v1/integration/processEvent")
                .queryParam("eventUrl", "http://docker.for.mac.localhost:8899/isv/event/${scenario.context.eventToken}")
                .queryParam("applicationUuid", "b29602c1-e1a9-442a-bfd3-442a43a2cc89")
                .get(String::class.java)
    }

    override fun preconditions(scenario: Scenario<ScenarioContext>): Boolean {
        return try {
            this.restResource.request()
                    .path("api/v1/offers/91FD106F-4B2C-4938-95AC-F54F74E9A239")
                    .queryParam("resellerDomain", "csptip58s.onmicrosoft.com")
                    .get(String::class.java)
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun executionComplete(scenario: Scenario<ScenarioContext>): Boolean {
        return try {
            val entity = this.restResource.request()
                    .path("api/v1/events/isvevents/bytoken/${scenario.context.eventToken}")
                    .get(object : ParameterizedTypeReference<List<ISVEvent>>() {})
            entity.body.any { it.eventType == "SUBSCRIPTION_ORDER_SUCCESSFUL_EVENT" || it.eventType == "I_S_V_EVENT_FAILED_EVENT" }
        } catch (e: Exception) {
            false
        }
    }


}
