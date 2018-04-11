package com.appdirect.integration.scenarios

import java.math.BigDecimal
import kotlin.collections.ArrayList

data class OfferWS (
        val id: String?,
        val uuid: String,
        val name: String,
        val resellerDomain: String,
        val description: String,
        val minimumQuantity: BigDecimal,
        val maximumQuantity: BigDecimal,
        val rank: Int,
        val uri: String,
        val locale: String,
        val country: String,
        val categoryId: String,
        val prerequisiteOffers: ArrayList<String> = arrayListOf(),
        val upgradeTargetOffers: ArrayList<String> = arrayListOf(),
        val conversionTargetOffers: ArrayList<String> = arrayListOf(),
        val partnerQualifications: ArrayList<String> = arrayListOf(),
        val reselleeQualifications: ArrayList<String> = arrayListOf(),
        val resellerQualifications: ArrayList<String> = arrayListOf(),
        val salesGroupId: String,
        val isAddOn: Boolean,
        val hasAddOns: Boolean,
        val isAvailableForPurchase: Boolean,
        val billing: String,
        val isAutoRenewable: Boolean,
        val productId: String,
        val productName: String,
        val productUnit: String,
        val unitType: String,
        val isTrial: Boolean,
        val supportedBillingCycles: ArrayList<String> = arrayListOf(),
        val isInternal: Boolean,
        val acquisitionType: String?,
        val supportedCatalogTypes: ArrayList<String> = arrayListOf())