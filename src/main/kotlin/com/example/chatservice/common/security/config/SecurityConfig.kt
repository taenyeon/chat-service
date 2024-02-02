package com.example.chatservice.common.security.config

import com.example.chatservice.common.security.provider.JwtAuthenticationProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(private val jwtAuthenticationProvider: JwtAuthenticationProvider) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(jwtAuthenticationProvider, UsernamePasswordAuthenticationFilter::class.java)
            .authorizeHttpRequests { auth -> settingUrlFilter(auth) }
            .httpBasic(Customizer.withDefaults())
            .formLogin { it.disable() }
            .logout { it.disable() }

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val corsConfiguration: CorsConfiguration = CorsConfiguration()
        corsConfiguration.addAllowedOriginPattern("*")
        corsConfiguration.allowedMethods = listOf("HEAD", "GET", "POST", "PUT", "DELETE")
        corsConfiguration.addAllowedHeader("*")
        corsConfiguration.maxAge = 3600
        val source: UrlBasedCorsConfigurationSource = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfiguration)
        return source

    }

    private fun settingUrlFilter(auth: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry) {
        auth.requestMatchers(
            "/test/**",
            "/api/user/login",
            "/api/user/join",
            "/error",
            "/api/user/accessToken",
            "/chat/**"
        )
            .permitAll()
            .anyRequest()
            .authenticated()
    }
}