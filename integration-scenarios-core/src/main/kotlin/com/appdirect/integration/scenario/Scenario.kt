package com.appdirect.integration.scenario

import com.appdirect.web.client.RestResource
import java.util.concurrent.TimeoutException

abstract class Scenario<T>(var restResource: RestResource,
                           private val validation: (scenario: Scenario<T>) -> Unit,
                           private val timeout: Long = 60000) {
    lateinit var context: ScenarioContext
    abstract fun setupMocks(scenario: Scenario<T>)
    abstract fun setupTestData(scenario: Scenario<T>)
    abstract fun onExecute(scenario: Scenario<T>)
    abstract fun preconditions(scenario: Scenario<T>): Boolean
    abstract fun executionComplete(scenario: Scenario<T>): Boolean

    fun ready(): Boolean {
        return try {
            restResource.request()
                    .path("/info")
                    .get(String::class.java)
            true
        } catch (e: RuntimeException) {
            false
        }
    }

    fun execute() {
        val preconditionStart = System.currentTimeMillis()
        if (ready() && !preconditions(this)) {
            setupTestData(this)
        }

        while (!preconditions(this)) {
            Thread.sleep(3000)
            if ((System.currentTimeMillis() - preconditionStart) > timeout) throw TimeoutException()
        }

        setupMocks(this)

        onExecute(this)

        val startTime = System.currentTimeMillis()
        while (!executionComplete(this)) {
            Thread.sleep(3000)
            if ((System.currentTimeMillis() - startTime) > timeout) throw TimeoutException()
        }

        validation.invoke(this)
    }
}