package com.appdirect.integration

import com.appdirect.integration.scenario.TestRun
import com.appdirect.integration.scenarios.SubscriptionOrderScenario
import com.appdirect.integration.scenarios.SubscriptionQtyChangeScenario
import org.junit.Test

class SubscriptionChangeTest: IntegrationTest() {
    @Test
    fun `When subscription QTY change is requested, then subscription update with the new QTY must be requested from Microsoft`() {
        TestRun.startWith(SubscriptionOrderScenario(8888))
                .continueWith(SubscriptionQtyChangeScenario(8888))
                .execute()
    }
}