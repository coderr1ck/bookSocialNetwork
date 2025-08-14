package com.coderrr1ck.bookBackend.repository;

import com.coderrr1ck.bookBackend.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {

}
