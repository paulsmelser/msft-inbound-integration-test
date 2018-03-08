package com.appdirect.web.client

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.util.Arrays.asList

class HttpRequestBuilderImpl internal constructor(private val restResource: RestClient, private val uriBuilder: UriComponentsBuilder) : HttpRequestBuilder {
    private val headers: HttpHeaders = HttpHeaders()

	override fun header(key: String, vararg value: String): HttpRequestBuilder {
        headers[key] = asList(*value)
        return this
    }

    override fun pathSegment(vararg pathSegments: String): HttpRequestBuilder {
        uriBuilder.pathSegment(*pathSegments)
        return this
    }

    override fun queryParam(name: String, vararg values: Any): HttpRequestBuilder {
        uriBuilder.queryParam(name, *values)
        return this
    }

    override fun path(path: String): HttpRequestBuilder {
        uriBuilder.path(path)
        return this
    }

    override fun <T, R> put(payload: T, aClass: Class<R>): ResponseEntity<R> {
        val entity = HttpEntity(payload, headers)
        return execute(uriBuilder.build().toUri(), HttpMethod.PUT, entity, aClass)
    }

    override fun <T, R> post(payload: T, aClass: Class<R>): ResponseEntity<R> {
        val entity = HttpEntity(payload, headers)
        return execute(uriBuilder.build().toUri(), HttpMethod.POST, entity, aClass)
    }

    override fun <T, R> patch(payload: T, aClass: Class<R>): ResponseEntity<R> {
        val entity = HttpEntity(payload, headers)
        return execute(uriBuilder.build().toUri(), HttpMethod.PATCH, entity, aClass)
    }

    override fun <T> get(aClass: Class<T>): ResponseEntity<T> {
        return execute(uriBuilder.build().toUri(), HttpMethod.GET, HttpEntity<Any>(headers), aClass)
    }

    override fun head(): ResponseEntity<Void> {
        return execute(uriBuilder.build().toUri(), HttpMethod.HEAD, HttpEntity<Any>(headers), Void::class.java)
    }

    override fun <T> get(aClass: ParameterizedTypeReference<T>): ResponseEntity<T> {
        return execute(uriBuilder.build().toUri(), HttpMethod.GET, HttpEntity<Any>(headers), aClass)
    }

    override fun delete(): ResponseEntity<*> {
            return restResource.delete(uriBuilder.build().toUri(), headers)
    }

    private fun <T, R> execute(uri: URI, httpMethod: HttpMethod, entity: HttpEntity<T>, responseType: Class<R>): ResponseEntity<R> {
            return restResource.execute(uri, httpMethod, entity, responseType)
    }

    private fun <T, R> execute(uri: URI, httpMethod: HttpMethod, entity: HttpEntity<T>, responseType: ParameterizedTypeReference<R>): ResponseEntity<R> {
            return restResource.execute(uri, httpMethod, entity, responseType)
    }
}
