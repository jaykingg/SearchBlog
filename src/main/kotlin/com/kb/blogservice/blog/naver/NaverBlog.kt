package com.kb.blogservice.blog.naver

import com.kb.blogservice.blog.kakao.KakaoBlog
import java.time.Instant

data class NaverBlog(
    val title: String,
    val description: String,
    val link: String,
    val bloggername: String,
    val postdate : Instant
) {
    fun toKakaoBlog() = KakaoBlog(
        title = title,
        contents = description,
        url = link,
        blogname = bloggername,
        thumbnail = null,
        datetime = postdate
    )
}