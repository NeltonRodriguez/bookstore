package com.neltonr.bookstore

import com.neltonr.bookstore.domain.AuthorSummary
import com.neltonr.bookstore.domain.AuthorUpdateRequest
import com.neltonr.bookstore.domain.BookSummary
import com.neltonr.bookstore.domain.dto.*
import com.neltonr.bookstore.domain.entities.AuthorEntity
import com.neltonr.bookstore.domain.entities.BookEntity

const val BOOK_A_ISBN = "978-089-230342-0777"

fun testAuthorDtoA(id: Long? = null) = AuthorDto(
    id=id,
    name = "Nelson",
    age = 30,
    description = "Nelson",
    image = "nelson.jpg"
)

fun testAuthorEntityA(id: Long? = null) = AuthorEntity(
    id=id,
    name = "Nelson",
    age = 30,
    description = "Nelson",
    image = "nelson.jpg"
)

fun testAuthorEntityB(id: Long? = null) = AuthorEntity(
    id = id,
    name = "Don Joe",
    age = 65,
    description = "Some other description",
    image = "sol-maria.jpeg"
)

fun testAuthorSummaryDtoA(id: Long) = AuthorSummaryDto(
    id=id,
    name = "Nelson",
    image = "nelson.jpg"
)

fun testAuthorSummaryA(id: Long) = AuthorSummary(
    id=id,
    name = "Nelson",
    image = "nelson.jpg"
)

fun testAuthorUpdateRequestDtoA(id: Long? = null) = AuthorUpdateRequestDto(
    id=id,
    name = "Nelson",
    age = 30,
    description = "Nelson",
    image = "nelson.jpg"
)

fun testAuthorUpdateRequestA(id: Long? = null) = AuthorUpdateRequest(
    id=id,
    name = "Nelson",
    age = 30,
    description = "Nelson",
    image = "nelson.jpg"
)

fun testBookEntityA(isbn: String, author: AuthorEntity) = BookEntity(
    isbn = isbn,
    title = "Test Book A",
    description = "A description...",
    image = "nelson.jpg",
    authorEntity = author
)

fun testBookSummaryDtoA(isbn: String, author: AuthorSummaryDto) = BookSummaryDto(
    isbn = isbn,
    title = "Test Book A",
    description = "A description...",
    image = "nelson.jpg",
    author = author
)

fun testBookSummaryA(isbn: String, author: AuthorSummary) = BookSummary(
    isbn = isbn,
    title = "Test Book A",
    description = "A description...",
    image = "nelson.jpg",
    author = author
)

fun testBookSummaryB(isbn: String, author: AuthorSummary) = BookSummary(
    isbn = isbn,
    title = "Test Book B",
    description = "Another description...",
    image = "another-image.jpeg",
    author = author
)
