package com.appdirect.integration.scenario

import com.appdirect.web.client.RestResource
import java.util.concurrent.TimeoutException

abstract class Scenario<T>(val restResource: RestResource,
                           private val validation: (scenario: Scenario<T>) -> Unit,
                           private val timeout: Long = 60000) {
    lateinit var context: ScenarioContext
    abstract fun setupMocks(scenario: Scenario<T>)
    abstract fun setupTestData(scenario: Scenario<T>)
    abstract fun onExecute(scenario: Scenario<T>)
    abstract fun preconditions(scenario: Scenario<T>): Boolean
    abstract fun executionComplete(scenario: Scenario<T>): Boolean

    fun execute() {
        val preconditionStart = System.currentTimeMillis()
        while(!preconditions(this)) {
            Thread.sleep(3000)
            if ((System.currentTimeMillis() - preconditionStart) > timeout) throw TimeoutException()
        }

        setupMocks(this)
        setupTestData(this)

        onExecute(this)

        val startTime = System.currentTimeMillis()
        while(!executionComplete(this)) {
            Thread.sleep(3000)
            if ((System.currentTimeMillis() - startTime) > timeout) throw TimeoutException()
        }

        validation.invoke(this)
    }
}