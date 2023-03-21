package com.kb.blogservice.blog.naver

import com.kb.blogservice.blog.kakao.KakaoBlog
import com.kb.blogservice.blog.kakao.KakaoBlogResponse

data class NaverBlogResponse (
    val total: Long,
    val start: Int,
    val display: Int,
    val items: List<NaverBlog>
) {
    fun toKakaoBlogResponse() : KakaoBlogResponse  {
        val toKaKaoBlog: MutableList<KakaoBlog> = mutableListOf()
        items.forEach {
            toKaKaoBlog.add(it.toKakaoBlog())
        }
        return KakaoBlogResponse(
            documents = toKaKaoBlog,
            meta = null
        )
    }

}