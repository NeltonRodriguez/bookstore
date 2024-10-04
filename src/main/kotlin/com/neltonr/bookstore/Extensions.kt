package com.neltonr.bookstore

import com.neltonr.bookstore.domain.AuthorSummary
import com.neltonr.bookstore.domain.AuthorUpdateRequest
import com.neltonr.bookstore.domain.BookSummary
import com.neltonr.bookstore.domain.BookUpdateRequest
import com.neltonr.bookstore.domain.dto.*
import com.neltonr.bookstore.domain.entities.AuthorEntity
import com.neltonr.bookstore.domain.entities.BookEntity
import com.neltonr.bookstore.exceptions.InvalidAuthorException

fun AuthorEntity.toAuthorDto() = AuthorDto(
    id=this.id,
    name=this.name,
    age=this.age,
    description=this.description,
    image=this.image
)

fun AuthorDto.toAuthorEntity() = AuthorEntity(
    id=this.id,
    name=this.name,
    age=this.age,
    description=this.description,
    image=this.image
)

fun AuthorUpdateRequestDto.toAuthorUpdateRequest() = AuthorUpdateRequest(
    id=this.id,
    name=this.name,
    age=this.age,
    description=this.description,
    image=this.image
)

fun BookSummary.toBookEntity(author: AuthorEntity) = BookEntity(
    isbn=this.isbn,
    title=this.title,
    description = this.description,
    image = this.image,
    authorEntity = author
)


fun AuthorSummaryDto.toAuthorSummary() = AuthorSummary(
    id=this.id,
    name=this.name,
    image=this.image,
)

fun BookSummaryDto.toBookSummary() = BookSummary(
    isbn=this.isbn,
    title=this.title,
    description=this.description,
    image=this.image,
    author = this.author.toAuthorSummary()
)

fun AuthorEntity.toAuthorSummaryDto() : AuthorSummaryDto {
    val authorId = this.id ?: throw InvalidAuthorException()
    return AuthorSummaryDto(
        id = authorId,
        name=this.name,
        image=this.image
    )
}


fun BookEntity.toBookSummaryDto() = BookSummaryDto(
    isbn = this.isbn,
    title = this.title,
    description = this.description,
    image = this.image,
    author = authorEntity.toAuthorSummaryDto()
)


fun BookUpdateRequestDto.toBookUpdateRequest() = BookUpdateRequest(
    title=this.title,
    description=this.description,
    image=this.image
)