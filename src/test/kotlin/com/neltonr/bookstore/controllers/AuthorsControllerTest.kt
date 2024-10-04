package com.neltonr.bookstore.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.neltonr.bookstore.domain.dto.AuthorUpdateRequestDto
import com.neltonr.bookstore.domain.entities.AuthorEntity
import com.neltonr.bookstore.services.AuthorService
import com.neltonr.bookstore.testAuthorDtoA
import com.neltonr.bookstore.testAuthorEntityA
import com.neltonr.bookstore.testAuthorUpdateRequestDtoA
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*

private const val AUTHORS_BASE_URL = "/v1/authors"


@SpringBootTest
@AutoConfigureMockMvc
class AuthorsControllerTest @Autowired constructor (
    private val mockMvc: MockMvc,
    @MockkBean private val authorService: AuthorService
) {
    val objectMapper = ObjectMapper()

    @BeforeEach
    fun beforeEach() {
        every {
            authorService.create(any())
        } answers {
            firstArg()
        }
    }

    @Test
    fun `Test that create Author saves the Author`() {

        mockMvc.post(AUTHORS_BASE_URL) {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                testAuthorDtoA()
            )
        }

        val expected = AuthorEntity(
            null,
            "Nelson",
            30,
            "Nelson",
            "nelson.jpg"
        )

        verify { authorService.create(expected) }
    }

    @Test
    fun `Test that create Author returns an HTTP 201 status on a successful create`() {
        mockMvc.post(AUTHORS_BASE_URL) {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                testAuthorDtoA()
            )
        }.andExpect {
            status { isCreated() }
        }
    }

    @Test
    fun `Test that create Author returns an HTTP 400 when IllegalArgumentException is thrown`() {
        every {
            authorService.create(any())
        } throws IllegalArgumentException()

        mockMvc.post(AUTHORS_BASE_URL) {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                testAuthorDtoA()
            )
        }.andExpect {
            status { isBadRequest() }
        }

    }


    @Test
    fun `Test that list returns an empty list and HTTP 200 when no authors in the db`() {

        every {
            authorService.list()
        } answers {
            emptyList()
        }

        mockMvc.get(AUTHORS_BASE_URL) {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { json("[]") }
        }
    }

    @Test
    fun `Test that list returns authors and HTTP 200 when authors in the db`() {
        every {
            authorService.list()
        } answers {
            listOf(testAuthorEntityA(1))
        }

        mockMvc.get(AUTHORS_BASE_URL) {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { jsonPath("$[0].id", equalTo(1)) }
            content { jsonPath("$[0].name", equalTo("Nelson")) }
            content { jsonPath("$[0].age", equalTo(30)) }
            content { jsonPath("$[0].description", equalTo("Nelson")) }
            content { jsonPath("$[0].image", equalTo("nelson.jpg")) }
        }
    }

    @Test
    fun `Test that get returns HTTP 404 when author not found in db`() {
        every {
            authorService.get(any())
        } answers {
            null
        }

        mockMvc.get("$AUTHORS_BASE_URL/999") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `Test that get returns HTTP 200 and Author when Author found in db`() {
        every {
            authorService.get(any())
        } answers {
            testAuthorEntityA(999)
        }

        mockMvc.get("$AUTHORS_BASE_URL/999") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { jsonPath("$.id", equalTo(999)) }
            content { jsonPath("$.name", equalTo("Nelson")) }
            content { jsonPath("$.age", equalTo(30)) }
            content { jsonPath("$.description", equalTo("Nelson")) }
            content { jsonPath("$.image", equalTo("nelson.jpg")) }
        }
    }

    @Test
    fun `Test that full update author when HTTP 200 and updated Author on successful update`() {
        every {
            authorService.fullUpdate(any(), any())
        } answers {
            secondArg()
        }

        mockMvc.put("$AUTHORS_BASE_URL/999") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(testAuthorDtoA(id = 999))
        }.andExpect {
            status { isOk() }
            content { jsonPath("$.id", equalTo(999)) }
            content { jsonPath("$.name", equalTo("Nelson")) }
            content { jsonPath("$.age", equalTo(30)) }
            content { jsonPath("$.description", equalTo("Nelson")) }
            content { jsonPath("$.image", equalTo("nelson.jpg")) }
        }
    }

//    @Test
//    fun `Test that full update author return HTTP 400 on IllegalStateException`() {
//        every {
//            authorService.fullUpdate(any(), any())
//        } throws(IllegalStateException())
//
//        mockMvc.put("$AUTHORS_BASE_URL/999") {
//            contentType = MediaType.APPLICATION_JSON
//            accept = MediaType.APPLICATION_JSON
//            content = objectMapper.writeValueAsString(testAuthorDtoA(id = 999))
//        }.andExpect {
//            status { isBadRequest() }
//        }
//    }

    @Test
    fun `test that partial update Author returns HTTP 400 on IllegalStateException`() {
        every {
            authorService.partialUpdate(any(), any())
        } throws IllegalStateException()

        mockMvc.patch("$AUTHORS_BASE_URL/999") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(testAuthorUpdateRequestDtoA(999L))
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `test that partial update returns HTTP 200 and updates Author`() {
        every {
            authorService.partialUpdate(any(), any())
        } answers {
            testAuthorEntityA(id=999)
        }

        mockMvc.patch("$AUTHORS_BASE_URL/999") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(testAuthorUpdateRequestDtoA(999L))
        }.andExpect {
            status { isOk() }
            content { jsonPath("$.id", equalTo(999)) }
            content { jsonPath("$.name", equalTo("Nelson")) }
            content { jsonPath("$.age", equalTo(30)) }
            content { jsonPath("$.description", equalTo("Nelson")) }
            content { jsonPath("$.image", equalTo("nelson.jpg")) }
        }
    }

    @Test
    fun `Test that delete author returns HTTP 204 on success`(){
        every {
            authorService.delete(any())
        } answers {}

        mockMvc.delete("$AUTHORS_BASE_URL/999") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNoContent() }
        }
    }

}

