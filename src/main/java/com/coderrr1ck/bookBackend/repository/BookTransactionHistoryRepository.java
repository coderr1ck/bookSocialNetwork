package com.coderrr1ck.bookBackend.repository;

import com.coderrr1ck.bookBackend.models.BookTransactionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, UUID> {
    @Query(
            """
            Select bth 
            from BookTransactionHistory bth 
            where bth.user.id = :userId
            AND bth.isReturned = false 
            OR bth.isReturnApproved = false
            """
    )
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable,UUID userId);


    @Query(
            """
            Select bth 
            from BookTransactionHistory bth 
            where bth.book.owner.id = :userId
            """
    )
    Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable,UUID userId);


    boolean existsByBookIdAndIsReturnApprovedFalse(UUID bookId);
//    check book's borrowalbe status

    Optional<BookTransactionHistory> findByBookIdAndUserIdAndIsReturnApprovedFalseAndIsReturnedFalse(UUID bookId, UUID userId);

    Optional<BookTransactionHistory> findByBookIdAndIsReturnApprovedFalseAndIsReturnedTrue(UUID bookId);
// this get's the current active borrow record for a user and a book,
}
