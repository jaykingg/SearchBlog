package com.kb.blogservice.blog.kakao

import java.time.Instant


data class KakaoBlog (
    val title: String,
    val contents: String,
    val url: String,
    val blogname: String,
    val thumbnail: String?,
    val datetime: Instant,
)