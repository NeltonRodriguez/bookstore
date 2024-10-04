package com.neltonr.bookstore.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.neltonr.bookstore.*
import com.neltonr.bookstore.services.BookService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.hamcrest.Matchers
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.result.StatusResultMatchersDsl

@SpringBootTest
@AutoConfigureMockMvc
class BooksControllerTest @Autowired constructor (
    private val mockMvc: MockMvc,
    @MockkBean private val bookService: BookService
) {
    val objectMapper = ObjectMapper()

    @Test
    fun `Test that createFullUpdateBook returns HTTP 201 when book is created`(){
        assertThatUserCreatedUpdated(true) {  isCreated() }
    }

    @Test
    fun `Test that createFullUpdateBook returns HTTP 200 when book is updated`(){
        assertThatUserCreatedUpdated(false) {  isOk() }
    }

    @Test
    fun `Test that createFullUpdateBook returns HTTP 500 when author in db doesn't have an id`(){
        val isbn = "978-089-230342-0777"
        val author = testAuthorEntityA()
        val savedBook = testBookEntityA(isbn, author)

        val authorSummaryDto = testAuthorSummaryDtoA(id = 1)
        val bookSummaryDto = testBookSummaryDtoA(isbn, authorSummaryDto)

        every {
            bookService.createUpdate(isbn, any())
        } answers {
            Pair(savedBook, true)
        }

        mockMvc.put("/v1/books/${isbn}"){
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(bookSummaryDto)
        }.andExpect {
            status { isInternalServerError() }
        }
    }

    private fun assertThatUserCreatedUpdated(isCreated: Boolean, statusCodeAssertion: StatusResultMatchersDsl.() -> Unit) {
        val isbn = "978-089-230342-0777"
        val author = testAuthorEntityA(id=1)
        val savedBook = testBookEntityA(isbn, author)

        val authorSummaryDto = testAuthorSummaryDtoA(id=1)
        val bookSummaryDto = testBookSummaryDtoA(isbn, authorSummaryDto)

        every {
            bookService.createUpdate(isbn, any())
        } answers {
            Pair(savedBook, isCreated)
        }

        mockMvc.put("/v1/books/${isbn}") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(bookSummaryDto)
        }.andExpect {
            status { statusCodeAssertion() }
        }
    }


    @Test
    fun `Test that createFullUpdateBook returns HTTP 400 when author doesn't exists`() {
        val isbn = "978-089-230342-0777"

        val authorSummaryDto = testAuthorSummaryDtoA(id = 1)
        val bookSummaryDto = testBookSummaryDtoA(isbn, authorSummaryDto)

        every {
            bookService.createUpdate(isbn, any())
        } throws IllegalStateException()

        mockMvc.put("/v1/books/${isbn}"){
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(bookSummaryDto)
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `Test that readManyBooks returns a list of books`(){
        val isbn = "978-089-230342-0777"
        every {
            bookService.list()
        } answers {
            listOf(testBookEntityA(isbn = isbn, testAuthorEntityA(id = 1)))
        }

        mockMvc.get("/v1/books"){
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { jsonPath("$[0].isbn", equalTo(isbn)) }
            content { jsonPath("$[0].title", equalTo("Test Book A")) }
            content { jsonPath("$[0].description", equalTo("A description...")) }
            content { jsonPath("$[0].image", equalTo("nelson.jpg")) }
            content { jsonPath("$[0].author.id", equalTo(1)) }
            content { jsonPath("$[0].author.name", equalTo("Nelson")) }
        }
    }

    @Test
    fun `Test that list returns no books when they do not match the Author ID `(){
        every {
            bookService.list(authorId = any())
        } answers {
            emptyList()
        }
        mockMvc.get("/v1/books?author=999"){
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { json("[]")}
        }
    }

    @Test
    fun `test that list returns book when matches author ID`() {
        val isbn = "978-089-230342-0777"

        every {
            bookService.list(authorId = 1L)
        } answers {
            listOf(
                testBookEntityA(
                    isbn = isbn,
                    testAuthorEntityA(1L)
                )
            )
        }

        mockMvc.get("/v1/books?author=1") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { jsonPath("$[0].isbn", equalTo(isbn)) }
            content { jsonPath("$[0].title", equalTo("Test Book A")) }
            content { jsonPath("$[0].description", equalTo("A description...")) }
            content { jsonPath("$[0].image", equalTo("nelson.jpg")) }
            content { jsonPath("$[0].author.id", equalTo(1)) }
            content { jsonPath("$[0].author.name", equalTo("Nelson")) }
        }
    }

    @Test
    fun `test that readOneBook returns HTTP 404 when no book found`() {
        val isbn = "978-089-230342-0777"

        every {
            bookService.get(any())
        } answers {
            null
        }

        mockMvc.get("/v1/books/$isbn") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status{ isNotFound() }
        }
    }

    @Test
    fun `test that deleteBook deletes a book successfully`() {
        every {
        bookService.delete(BOOK_A_ISBN)
        } answers {}

        mockMvc.delete("/v1/books/$BOOK_A_ISBN") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status{ isNoContent() }
        }



    }



}