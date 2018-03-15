package com.appdirect.integration.mock

import com.appdirect.integration.file.Resource
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import org.apache.http.HttpHeaders
import org.apache.http.entity.ContentType

object OfferMocks {
    fun mockOffersForMarket(country: String) {
        WireMock.givenThat(WireMock.get(urlPathEqualTo("/v1/offers"))
                .withQueryParam("country", WireMock.equalTo(country))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())
                        .withBody(Resource.parseFile("data/offer/$country.json").text)))
    }
}