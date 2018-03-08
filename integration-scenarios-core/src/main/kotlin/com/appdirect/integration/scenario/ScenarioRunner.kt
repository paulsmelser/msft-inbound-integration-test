package com.appdirect.integration.scenario

abstract class ScenarioRunner(protected val port: Int, protected val validate: (scenario: Scenario) -> Unit) {
    protected lateinit var scenario: Scenario
    protected lateinit var execution: (scenario: Scenario) -> Unit

    fun restResource() = scenario.restResource
    fun execute() = scenario.execute(execution)
}