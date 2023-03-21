package com.kb.blogservice.blog

import org.springframework.data.jpa.repository.JpaRepository

interface BlogKeywordRepository : JpaRepository<BlogKeyword, Long> {

    fun findByKeyword(keyword: String): BlogKeyword?
    fun findTop10ByOrderByCountDesc(): List<BlogKeyword>
}