package com.kb.blogservice.blog

import jakarta.persistence.*
import org.hibernate.validator.constraints.UniqueElements

@Entity
@Table(name = "blog_keywords")
data class BlogKeyword(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @UniqueElements
    val keyword: String,
    val count: Int,
)
