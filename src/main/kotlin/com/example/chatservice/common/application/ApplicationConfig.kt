package com.example.chatservice.common.application

import com.example.chatservice.common.filter.LoggingFilter
import com.example.chatservice.common.paging.QueryStringArgumentResolver
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class ApplicationConfig(private val queryString: QueryStringArgumentResolver) : WebMvcConfigurer {


    @Bean
    fun filterRegistrationBean(): FilterRegistrationBean<LoggingFilter> {
        val filterRegistrationBean = FilterRegistrationBean(LoggingFilter())
        filterRegistrationBean.order = Int.MIN_VALUE
        return filterRegistrationBean
    }

    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        argumentResolvers.add(queryString)
    }

    @Bean
    fun objectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        objectMapper.registerModules(JavaTimeModule())
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return objectMapper
    }

}