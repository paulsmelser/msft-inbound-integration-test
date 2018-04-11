package com.appdirect.integration.scenario

class TestRun(private val context: ScenarioContext = ScenarioContext()) {
    private val scenarios: MutableList<Scenario<ScenarioContext>> = mutableListOf()

    fun continueWith(scenario: Scenario<ScenarioContext>) : TestRun{
        scenario.context = context
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
            return scenarioBuilder.continueWith(scenario)
        }
    }
}