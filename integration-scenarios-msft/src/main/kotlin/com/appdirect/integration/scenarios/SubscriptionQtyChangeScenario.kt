package com.appdirect.integration.scenarios

import com.appdirect.integration.http.RestResourceFactory
import com.appdirect.integration.scenario.Scenario
import com.appdirect.integration.scenario.ScenarioContext
import com.appdirect.integration.scenario.mocks.ISVEventMocks
import com.appdirect.integration.scenario.mocks.microsoft.SubscriptionMocks
import com.appdirect.integration.scenarios.messagecontracts.ISVEvent
import com.psmelser.jackson.json.Json
import org.springframework.core.ParameterizedTypeReference

class SubscriptionQtyChangeScenario(connectorPort: Int, validation: (scenario: Scenario<ScenarioContext>) -> Unit = {}, timeout: Long = 60000) : Scenario<ScenarioContext>(RestResourceFactory.createOAuthResource(connectorPort, "office-365-enterprise-322", "BlPkzMNVp2DYdgYu"), validation, timeout) {
    override fun setupMocks(scenario: Scenario<ScenarioContext>) {
        val jsonNode = Json.toJsonNode(restResource.request()
                .path("/api/v1/subscriptions")
                .queryParam("subscriptionUUID", scenario.context.subscriptionId)
                .get(String::class.java).body)
        val subscription = jsonNode.get("appdirectAccountIdentifier").asText()
        scenario.context.subscriptionAccountIdentifier = subscription
        ISVEventMocks.mockSubscriptionChangeEventEndpoints(scenario.context)
        SubscriptionMocks.patchSubscriptionMock(scenario)
    }

    override fun setupTestData(scenario: Scenario<ScenarioContext>) {}

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
                    .path("api/v1/subscriptions")
                    .queryParam("subscriptionUUID", scenario.context.subscriptionId)
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
            entity.body.any { it.eventType == "SUBSCRIPTION_QUANTITY_CHANGE_SUCCESSFUL_EVENT" } // || it.eventType == "I_S_V_EVENT_FAILED_EVENT" }
        } catch (e: Exception) {
            false
        }
    }
}