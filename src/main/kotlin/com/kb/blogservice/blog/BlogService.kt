package com.kb.blogservice.blog

import com.kb.blogservice.blog.kakao.KakaoBlogResponse
import com.kb.blogservice.blog.naver.NaverBlogResponse
import com.kb.blogservice.config.ApiProperties
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchange

@Service
class BlogService(
    val blogKeywordRepository: BlogKeywordRepository,
    val apiProperties: ApiProperties
) {
    suspend fun search(criteria: BlogCriteria): BlogView {
        val keyword = getKeyword(criteria.query)

        withContext(Dispatchers.IO) {
            blogKeywordRepository.findByKeyword(keyword)
        }?.let {
            updateKeyword(it)
        } ?: saveKeyword(keyword)

        val blogData = getKakaoBlogData(criteria)
        return BlogView(
            populars = withContext(Dispatchers.IO) {
                blogKeywordRepository.findTop10ByOrderByCountDesc()
            },
            items = blogData.documents,
            page = blogData.meta
        )
    }

    private fun getKeyword(query: String): String {
        val querySplit: List<String> = query.split(" ")
        return if (querySplit.size > 1) querySplit[1] else querySplit[0]
    }

    @Transactional
    suspend fun updateKeyword(blogKeyword: BlogKeyword) {
        withContext(Dispatchers.IO) {
            blogKeywordRepository.save(
                blogKeyword.copy(
                    count = blogKeyword.count + 1
                )
            )
        }
    }

    @Transactional
    suspend fun saveKeyword(keyword: String) {
        withContext(Dispatchers.IO) {
            blogKeywordRepository.save(
                BlogKeyword(
                    keyword = keyword,
                    count = 1
                )
            )
        }
    }

    private suspend fun getKakaoBlogData(criteria: BlogCriteria): KakaoBlogResponse {
        return WebClient.builder()
            .baseUrl(apiProperties.kakaoBaseUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
            .get()
            .uri { builder ->
                builder
                    .queryParam("query", criteria.query)
                    .queryParam("page", criteria.page)
                    .queryParam("size", criteria.size)
                    .queryParam("sort", criteria.sort)
                    .build()
            }
            .header("Authorization", apiProperties.auth)
            .awaitExchange { clientResponse ->
                if (clientResponse.statusCode() == HttpStatus.OK) {
                    clientResponse.awaitBody()
                } else {
                    getNaverBlogData(criteria).toKakaoBlogResponse()
                }
            }
    }

    private suspend fun getNaverBlogData(criteria: BlogCriteria): NaverBlogResponse {
        return WebClient.builder()
            .baseUrl(apiProperties.naverBaseUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
            .get()
            .uri { builder ->
                builder
                    .queryParam("query", getKeyword(criteria.query))
                    .queryParam("display", criteria.size ?: "10")
                    .queryParam("start", criteria.page ?: "1")
                    .queryParam("sort", "sim")
                    .build()
            }
            .header("X-Naver-Client-Id", apiProperties.clientId)
            .header("X-Naver-Client-Secret", apiProperties.clientSecret)
            .retrieve()
            .awaitBody()
    }
}