package com.appdirect.integration.mock

import com.appdirect.web.client.RestResource

abstract class ScenarioRunner<T>(val restResource: RestResource, protected val validate: (scenario: Scenario<T>) -> Unit, val timeout: Long) {
    constructor(restResource: RestResource, validate: (scenario: Scenario<T>) -> Unit) : this(restResource, validate, 120000)

    protected lateinit var scenario: Scenario<T>
    protected lateinit var execution: (scenario: Scenario<T>) -> Unit

    fun execute() = scenario.execute(execution)
}