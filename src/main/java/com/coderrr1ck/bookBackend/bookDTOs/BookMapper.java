package com.coderrr1ck.bookBackend.bookDTOs;

import com.coderrr1ck.bookBackend.bookDTOs.BookRequestDTO;
import com.coderrr1ck.bookBackend.models.Book;
import com.coderrr1ck.bookBackend.models.BookTransactionHistory;
import com.coderrr1ck.bookBackend.service.FileUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BookMapper {
    public Book toBook(BookRequestDTO request) {
        return Book.builder()
                .title(request.getTitle())
                .isbn(request.getIsbn())
                .authorName(request.getAuthorName())
                .synopsis(request.getSynopsis())
                .archived(false)
                .sharable(request.isSharable())
                .build();
    }

    public BookResponseDTO toBookResponse(Book book) {
        //                .rate(book.getRate())
        return BookResponseDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .isbn(book.getIsbn())
                .synopsis(book.getSynopsis())
//                .rate(book.getRate())
                .archived(book.isArchived())
                .shareable(book.isSharable())
                 .owner(book.getOwner().getFullName())
                .cover(FileUtils.readFileFromLocation(book.getBookCover(), book.getId()))
                .build();
    }

    public BorrowedBookResponseDTO toBorrowedBookResponse(BookTransactionHistory history) {
        return BorrowedBookResponseDTO.builder()
                .id(history.getBook().getId())
                .title(history.getBook().getTitle())
                .authorName(history.getBook().getAuthorName())
                .isbn(history.getBook().getIsbn())
                .rate(history.getBook().getRate())
                .returned(history.isReturned())
                .returnApproved(history.isReturnApproved())
                .build();
    }
}