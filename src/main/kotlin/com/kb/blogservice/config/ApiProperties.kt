package com.kb.blogservice.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration


@Configuration
data class ApiProperties(
    @Value("\${api.kakao.auth}")
    val auth: String,

    @Value("\${api.naver.clientId}")
    val clientId: String,

    @Value("\${api.naver.clientSecret}")
    val clientSecret: String
)