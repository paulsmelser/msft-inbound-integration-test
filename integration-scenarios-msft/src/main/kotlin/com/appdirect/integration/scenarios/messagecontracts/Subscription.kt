package com.appdirect.integration.scenarios.messagecontracts

import java.util.UUID

data class Subscription constructor(private val uuid: UUID, private val applicationUUID: UUID, private val customerTenantId: UUID, private val appdirectAccountIdentifier: UUID)