package com.neltonr.bookstore.services

import com.neltonr.bookstore.domain.BookSummary
import com.neltonr.bookstore.domain.BookUpdateRequest
import com.neltonr.bookstore.domain.entities.BookEntity

interface BookService {
    fun createUpdate(isbn: String, bookSummary: BookSummary): Pair<BookEntity, Boolean>
    fun list(authorId: Long? = null): List<BookEntity>
    fun get(isbn: String): BookEntity?
    fun partialUpdate(isbn: String, bookUpdateRequest: BookUpdateRequest): BookEntity
    fun delete(isbn: String)
}