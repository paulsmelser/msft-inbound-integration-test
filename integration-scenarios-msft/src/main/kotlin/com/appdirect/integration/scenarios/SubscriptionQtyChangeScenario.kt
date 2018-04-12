package com.appdirect.integration.scenarios

import com.appdirect.integration.http.RestResourceFactory
import com.appdirect.integration.scenario.Scenario
import com.appdirect.integration.scenario.ScenarioContext
import com.appdirect.integration.scenario.mocks.ISVEventMocks

class SubscriptionQtyChangeScenario(connectorPort: Int, validation: (scenario: Scenario<ScenarioContext>) -> Unit = {}, timeout: Long = 60000) : Scenario<ScenarioContext>(RestResourceFactory.createOAuthResource(connectorPort, "office-365-enterprise-322", "BlPkzMNVp2DYdgYu"), validation, timeout) {
    override fun setupMocks(scenario: Scenario<ScenarioContext>) {
        ISVEventMocks.mockSubscriptionChangeEventEndpoints(scenario.context)
    }

    override fun setupTestData(scenario: Scenario<ScenarioContext>) {

    }

    override fun onExecute(scenario: Scenario<ScenarioContext>) {
        restResource.request()
                .path("/api/v1/integration/processEvent")
                .queryParam("eventUrl", "http://docker.for.mac.localhost:8899/isv/event/${scenario.context.eventToken}")
                .queryParam("applicationUuid", "b29602c1-e1a9-442a-bfd3-442a43a2cc89")
                .get(String::class.java)
    }

    override fun preconditions(scenario: Scenario<ScenarioContext>): Boolean {
        return true
    }

    override fun executionComplete(scenario: Scenario<ScenarioContext>): Boolean {
        return true
    }
}