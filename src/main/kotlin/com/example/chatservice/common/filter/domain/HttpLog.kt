package com.example.chatservice.common.filter.domain


data class HttpLog(
    val request: Request,
    val response: Response
) {
    data class Request(
        val url: String,
        val method: String,
        val path: String,
        val elapsedTime: Double,
        val headers: Map<String, Any?>,
        val queryString: String?,
        val body: Map<String, Any?>?
    )

    data class Response(
        val status: String,
        val headers: Map<String, Any?>,
        val body: Map<String, Any?>?
    )
}