package com.kb.blogservice

import com.kb.blogservice.blog.BlogKeyword
import com.kb.blogservice.blog.BlogKeywordRepository
import com.kb.blogservice.blog.BlogView
import com.kb.blogservice.blog.kakao.KakaoBlog
import com.kb.blogservice.blog.kakao.KakaoBlogMetaInfo
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.clearAllMocks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@SpringBootTest
@AutoConfigureWebTestClient
class SearchBlogsIT(
    private val webTestClient: WebTestClient,
    private val blogKeywordRepository: BlogKeywordRepository
) : BehaviorSpec({

    afterEach {
        clearAllMocks()
    }

    val query = "브런치"
    val page = "1"
    val size = "10"
    val sort = ""
    val request = webTestClient
        .get()
        .uri { uriBuilder ->
            uriBuilder.path("/blog/search")
                .queryParam("query", query)
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("sort", sort)
                .build()
        }

    Given("ParameterObject 유효성") {
        When("ParameterObject 가 유효하지 않은 경우") {
            Then("Response 400 BAD_REQUEST") {
                webTestClient.get()
                    .uri { uriBuilder ->
                        uriBuilder.path("/blog/search")
                            .queryParam("query", "")
                            .build()
                    }
                    .exchange()
                    .expectStatus().isBadRequest
            }
        }
    }

    Given("Blog 검색 요청") {
        When("Kakao Blog 검색 요청에 성공했을 경우") {
            val popularList = listOf(
                BlogKeyword(
                    keyword = "세종대왕",
                    count = 20
                ),
                BlogKeyword(
                    keyword = "광개토대왕",
                    count = 12
                ),
                BlogKeyword(
                    keyword = "이순신",
                    count = 41
                ),
                BlogKeyword(
                    keyword = "홍길동",
                    count = 5
                ),
                BlogKeyword(
                    keyword = "카카오뱅크",
                    count = 65
                ),
                BlogKeyword(
                    keyword = "아이유",
                    count = 24
                )
            )


            beforeEach {
                withContext(Dispatchers.IO) {
                    blogKeywordRepository.saveAll(popularList)
                }
            }

            afterEach {
                withContext(Dispatchers.IO) {
                    blogKeywordRepository.deleteAll()
                }
            }

            Then("인기검색목록과 함께 Blog List 를 반환한다") {
                request
                    .exchange()
                    .expectStatus().isOk
                    .expectBody<BlogView>()
                    .returnResult()
                    .responseBody!!
                    .should {
                        it.populars.size shouldBe popularList.size + 1
                        it.items[0].shouldBeTypeOf<KakaoBlog>()
                        it.page.shouldBeTypeOf<KakaoBlogMetaInfo>()
                    }

                withContext(Dispatchers.IO) {
                    blogKeywordRepository.findByKeyword(query)
                }!!.count shouldBe 1
            }
        }
    }
})