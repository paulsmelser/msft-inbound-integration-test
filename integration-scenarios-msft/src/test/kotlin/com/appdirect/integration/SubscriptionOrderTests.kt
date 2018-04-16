package com.appdirect.integration

import com.appdirect.integration.scenario.TestRun
import com.appdirect.integration.scenarios.SubscriptionOrderScenario
import com.appdirect.integration.scenarios.messagecontracts.Subscription
import com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.verify
import org.junit.Test
import org.springframework.core.ParameterizedTypeReference


class SubscriptionOrderTests: IntegrationTest() {
    @Test
    fun `When subscription order is placed for a new company, then subscription then subscription is purchase and saved successfully`() {
        TestRun.startWith(SubscriptionOrderScenario(
                8888,
                {
//                    verify(postRequestedFor())
                    it.restResource.request()
                            .path("api/v1/subscriptions")
                            .queryParam("subscriptionUUID", it.context.subscriptionId)
                            .get(object : ParameterizedTypeReference<Subscription>() {})
                })).execute()
    }


}
