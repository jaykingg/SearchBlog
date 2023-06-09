package com.kb.blogservice.blog

import org.springdoc.api.annotations.ParameterObject
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/blog")
class BlogController(
    val blogService: BlogService
) {
    @GetMapping("/search", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun searchBlogs(
        @Valid @ParameterObject
        criteria: BlogCriteria
    ): BlogView = blogService.search(criteria)
}