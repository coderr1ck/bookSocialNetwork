package com.coderrr1ck.bookBackend.repository;

import com.coderrr1ck.bookBackend.authDTOs.BookResponseDTO;
import com.coderrr1ck.bookBackend.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
    @Query("""
           SELECT new com.coderrr1ck.bookBackend.authDTOs.BookResponseDTO(
               b.id,
               b.title,
               b.authorName,
               b.isbn,
               b.synopsis,
               b.sharable,
               b.bookCover,
               b.archived
           )
           FROM Book b
           """) // constructor projection using hibernate + chatGPT
    List<BookResponseDTO> findAllBooksAsDto();
}