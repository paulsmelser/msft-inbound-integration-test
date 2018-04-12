package com.appdirect.integration.scenario

import com.appdirect.web.client.RestResource
import java.util.UUID

class TestRun(private val context: ScenarioContext = ScenarioContext()) {
    private lateinit var restResource: RestResource
    private val scenarios: MutableList<Scenario<ScenarioContext>> = mutableListOf()

    fun continueWith(scenario: Scenario<ScenarioContext>) : TestRun{
        scenario.context = context.copy(eventToken = UUID.randomUUID().toString())
        scenario.restResource = restResource
        scenarios.add(scenario)
        return this
    }

    fun execute() {
        scenarios.forEach {
            it.execute()
        }
    }

    companion object {
        @JvmStatic
        fun startWith(scenario: Scenario<ScenarioContext>) : TestRun {
            val scenarioBuilder = TestRun()
            scenarioBuilder.restResource = scenario.restResource
            return scenarioBuilder.continueWith(scenario)
        }
    }
}