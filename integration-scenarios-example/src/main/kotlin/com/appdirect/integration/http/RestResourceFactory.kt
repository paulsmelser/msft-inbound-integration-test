package com.appdirect.integration.http

import com.appdirect.web.client.RestClient
import com.appdirect.web.client.RestResource
import org.springframework.http.MediaType
import org.springframework.http.converter.ByteArrayHttpMessageConverter
import org.springframework.http.converter.FormHttpMessageConverter
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.ResourceHttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.security.oauth.common.signature.SharedConsumerSecretImpl
import org.springframework.security.oauth.consumer.BaseProtectedResourceDetails
import org.springframework.security.oauth.consumer.ProtectedResourceDetails
import org.springframework.security.oauth.consumer.client.OAuthRestTemplate
import org.springframework.social.support.ClientHttpRequestFactorySelector
import java.net.URI
import java.nio.charset.Charset
import java.util.ArrayList
import java.util.Arrays

object RestResourceFactory {
    fun createOAuthResource(port: Int, key: String, secret: String) : RestResource {
        val client = OAuthRestTemplate(protectedResourceDetails(key, secret))
        client.messageConverters = getMessageConverters()
        client.requestFactory = ClientHttpRequestFactorySelector.getRequestFactory()
        return RestClient(client, URI.create("http://localhost:$port"))
    }

    private fun protectedResourceDetails(key: String, secret: String) : ProtectedResourceDetails {
        val protectedResourceDetails = BaseProtectedResourceDetails()
        protectedResourceDetails.consumerKey = key
        protectedResourceDetails.sharedSecret = SharedConsumerSecretImpl(secret)
        return protectedResourceDetails
    }

    private fun getJsonMessageConverter(): MappingJackson2HttpMessageConverter {
        return MappingJackson2HttpMessageConverter()
    }

    private fun getMessageConverters(): List<HttpMessageConverter<*>> {
        val messageConverters = ArrayList<HttpMessageConverter<*>>()
        messageConverters.add(StringHttpMessageConverter())
        messageConverters.add(getFormMessageConverter())
        messageConverters.add(getJsonMessageConverter())
        messageConverters.add(getByteArrayMessageConverter())
        return messageConverters
    }

    private fun getFormMessageConverter(): FormHttpMessageConverter {
        val converter = FormHttpMessageConverter()
        converter.setCharset(Charset.forName("UTF-8"))
        val partConverters = ArrayList<HttpMessageConverter<*>>()

        partConverters.add(ByteArrayHttpMessageConverter())

        val stringHttpMessageConverter = StringHttpMessageConverter(Charset.forName("UTF-8"))
        stringHttpMessageConverter.setWriteAcceptCharset(false)
        partConverters.add(stringHttpMessageConverter)

        partConverters.add(ResourceHttpMessageConverter())
        converter.setPartConverters(partConverters)

        return converter
    }

    private fun getByteArrayMessageConverter(): ByteArrayHttpMessageConverter {
        val converter = ByteArrayHttpMessageConverter()
        converter.supportedMediaTypes = Arrays.asList(MediaType.IMAGE_JPEG, MediaType.IMAGE_GIF, MediaType.IMAGE_PNG)
        return converter
    }
}