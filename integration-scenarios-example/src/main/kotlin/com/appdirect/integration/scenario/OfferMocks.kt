package com.appdirect.integration.scenario

import com.appdirect.integration.file.Resource
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.givenThat
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import org.apache.http.HttpHeaders
import org.apache.http.entity.ContentType

object OfferMocks {
    fun mockOffersForMarket(country: String) {
        givenThat(get(urlPathEqualTo("/v1/offers"))
                .withQueryParam("country", WireMock.equalTo(country))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())
                        .withBody(Resource.parseFile("data/offer/$country.json").text)))
    }
}