package com.coderrr1ck.bookBackend.repository;

import com.coderrr1ck.bookBackend.models.BookTransactionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, UUID> {
    @Query(
            """
            Select bth 
            from BookTransactionHistory bth 
            where bth.user.id = :userId
            """
    )
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable,UUID userId);
}
