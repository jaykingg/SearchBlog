package com.kb.blogservice.blog

import jakarta.validation.constraints.NotBlank

data class BlogCriteria(
    @field: NotBlank
    val query: String,

    val page: Int?,

    val size: Int?,

    val sort: String?
)