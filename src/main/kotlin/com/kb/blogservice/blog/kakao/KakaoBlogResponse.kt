package com.kb.blogservice.blog.kakao

data class KakaoBlogResponse(
    val documents: List<KakaoBlog>,
    val meta: KakaoBlogMetaInfo?
)
