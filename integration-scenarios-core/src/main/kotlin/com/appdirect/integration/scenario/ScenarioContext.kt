package com.appdirect.integration.scenario

import org.apache.commons.lang3.RandomStringUtils
import java.util.UUID

data class ScenarioContext(
        val qty: Int = 3,
        val customerDomain: String = "${RandomStringUtils.random(8, true, true)}.onmicrosoft.com",
        val companyId: String = UUID.randomUUID().toString(),
        val customerId: String = UUID.randomUUID().toString(),
        val orderId: String = UUID.randomUUID().toString(),
        val subscriptionId: String = UUID.randomUUID().toString(),
        val eventToken: String = UUID.randomUUID().toString())