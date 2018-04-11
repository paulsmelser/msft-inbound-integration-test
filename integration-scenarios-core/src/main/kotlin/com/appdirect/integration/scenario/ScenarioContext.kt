package com.appdirect.integration.scenario

import java.util.UUID

data class ScenarioContext(
        val companyId: String = UUID.randomUUID().toString(),
        val customerId: String = UUID.randomUUID().toString(),
        val orderId: String = UUID.randomUUID().toString(),
        val subscriptionId: String = UUID.randomUUID().toString(),
        val eventToken: String = UUID.randomUUID().toString())