package com.example.chatservice.common.paging

import com.example.chatservice.common.paging.annotation.QueryString
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class QueryStringArgumentResolver : HandlerMethodArgumentResolver {
    val objectMapper: ObjectMapper = ObjectMapper()
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(QueryString::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val request: HttpServletRequest = webRequest.getNativeRequest(HttpServletRequest::class.java)!!
        val queryStringJson = queryStringToJson(request.queryString)
        return objectMapper.readValue(queryStringJson, parameter.parameterType)
    }

    private fun queryStringToJson(a: String): String {
        var res = "{\""
        for (i in a.indices) {
            if (a[i] == '=') {
                res += "\"" + ":" + "\""
            } else if (a[i] == '&') {
                res += "\"" + "," + "\""
            } else {
                res += a[i]
            }
        }
        res += "\"" + "}"
        return res
    }
}