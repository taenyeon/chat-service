package com.example.chatservice.common.filter

import com.example.chatservice.common.filter.domain.HttpLog
import com.example.chatservice.common.function.logger
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import org.springframework.web.util.WebUtils
import java.io.UnsupportedEncodingException
import java.util.*


class LoggingFilter : Filter {
    private val log = logger()
    private val objectMapper: ObjectMapper = ObjectMapper()
    private val excludeList: List<String> = listOf(
        "application/javascript",
        "text/html"
    )

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        MDC.put("traceId", UUID.randomUUID().toString())

        val startTime = System.currentTimeMillis()
        val requestWrapper = ContentCachingRequestWrapper((request as HttpServletRequest))
        val responseWrapper = ContentCachingResponseWrapper((response as HttpServletResponse?)!!)
        chain?.doFilter(requestWrapper, responseWrapper)
        val endTime = System.currentTimeMillis()
        val elapsedTime = (endTime - startTime) / 1000.0
        val httpLog = objectMapper.writeValueAsString(createHttpLog(requestWrapper, elapsedTime, responseWrapper))
        log.info("[HTTP] - $httpLog")

        MDC.clear()
    }

    private fun createHttpLog(
        requestWrapper: ContentCachingRequestWrapper,
        elapsedTime: Double,
        responseWrapper: ContentCachingResponseWrapper
    ): HttpLog {
        return HttpLog(
            HttpLog.Request(
                requestWrapper.requestURL.toString(),
                requestWrapper.method,
                requestWrapper.servletPath,
                elapsedTime,
                getRequestHeaders(requestWrapper),
                requestWrapper.queryString,
                getRequestBody(requestWrapper)
            ),
            HttpLog.Response(
                responseWrapper.status.toString(),
                getResponseHeaders(responseWrapper),
                getResponseBody(responseWrapper)
            )
        )
    }

    private fun getRequestHeaders(request: HttpServletRequest): Map<String, Any?> {
        val headers: MutableMap<String, Any?> = mutableMapOf()
        val headerNames: Enumeration<String> = request.headerNames
        while (headerNames.hasMoreElements()) {
            val headerName: String = headerNames.nextElement()
            headers[headerName] = request.getHeader(headerName)
        }
        return headers
    }

    private fun getRequestBody(request: ContentCachingRequestWrapper): Map<String, Any?>? {
        val wrapper = WebUtils.getNativeRequest(
            request,
            ContentCachingRequestWrapper::class.java
        )
        try {
            if (wrapper != null) {
                val buf = wrapper.contentAsByteArray
                if (buf.isNotEmpty()) {
                    val jsonString = String(buf, 0, buf.size, charset(wrapper.characterEncoding))
                    return objectMapper.readValue<Map<String, Any?>>(jsonString)
                }
            }
        } catch (e: UnsupportedEncodingException) {
            return null
        } catch (e: Exception) {
            return null
        }
        return null
    }

    private fun getResponseHeaders(response: HttpServletResponse): MutableMap<String, Any> {
        val headers: MutableMap<String, Any> = mutableMapOf()
        val headerNames: MutableCollection<String> = response.headerNames
        headerNames.forEach { headerName ->
            headers[headerName] = response.getHeader(headerName)
        }
        return headers
    }

    private fun getResponseBody(response: HttpServletResponse): Map<String, Any?>? {
        var payload: String? = null
        val wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper::class.java)
        if (wrapper != null) {
            val buffer: ByteArray = wrapper.contentAsByteArray
            if (buffer.isNotEmpty()) {
                payload = buffer.toString(Charsets.UTF_8)
                wrapper.copyBodyToResponse()
                return objectMapper.readValue<Map<String, Any?>>(payload)
            }
        }
        if (payload != null) {
            if (response.contentType != null) {
                val isMatch: Boolean = excludeList.stream()
                    .anyMatch { exclude -> response.contentType.contains(exclude) }
                if (isMatch) {
                    return null
                }
            }
        }
        return null
    }

}