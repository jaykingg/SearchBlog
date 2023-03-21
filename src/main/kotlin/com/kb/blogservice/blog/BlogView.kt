package com.kb.blogservice.blog

import com.kb.blogservice.blog.kakao.KakaoBlog
import com.kb.blogservice.blog.kakao.KakaoBlogMetaInfo

data class BlogView(
    val populars: List<BlogKeyword>,
    val items: List<KakaoBlog>,
    val page: KakaoBlogMetaInfo?
)