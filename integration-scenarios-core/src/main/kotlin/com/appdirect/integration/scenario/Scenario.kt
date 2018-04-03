package com.appdirect.integration.scenario

import com.appdirect.web.client.RestResource
import java.util.concurrent.TimeoutException

open class Scenario <T>(
        val restResource: RestResource,
        val payload: T,
        private val mocks: (scenario: Scenario<T>) -> Unit,
        private val preconditions: (scenario: Scenario<T>) -> Boolean,
        private val completed: (scenario: Scenario<T>) -> Boolean,
        private val validate: (scenario: Scenario<T>) -> Unit,
        private val timeout: Long) {

    fun execute(onExecute: (scenario: Scenario<T>) -> Unit) {
        val preconditionStart = System.currentTimeMillis()
        while(!preconditions.invoke(this)) {
            Thread.sleep(3000)
            if ((System.currentTimeMillis() - preconditionStart) > timeout) throw TimeoutException()
        }

        mocks.invoke(this)

        onExecute.invoke(this)
        val startTime = System.currentTimeMillis()
        while(!completed.invoke(this)) {
            Thread.sleep(3000)
            if ((System.currentTimeMillis() - startTime) > timeout) throw TimeoutException()
        }

        validate.invoke(this)
    }
}