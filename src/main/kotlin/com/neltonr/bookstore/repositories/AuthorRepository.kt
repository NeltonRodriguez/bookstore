package com.neltonr.bookstore.repositories

import com.neltonr.bookstore.domain.entities.AuthorEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthorRepository : JpaRepository<AuthorEntity, Long?>

// With this we inherit CRUD behaviour