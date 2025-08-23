package com.coderrr1ck.bookBackend.repository;

import com.coderrr1ck.bookBackend.bookDTOs.BookResponseDTO;
import com.coderrr1ck.bookBackend.models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> , JpaSpecificationExecutor<Book> {
    @Query("""
           Select b from Book b
            where b.archived = false 
            AND b.sharable = true 
            AND b.owner.id != :userId
            """
    )
    Page<Book> findAllDisplayableBooks(Pageable pageable,UUID userId);
}