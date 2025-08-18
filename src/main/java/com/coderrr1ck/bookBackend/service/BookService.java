package com.coderrr1ck.bookBackend.service;

import com.coderrr1ck.bookBackend.bookDTOs.BookMapper;
import com.coderrr1ck.bookBackend.bookDTOs.BookRequestDTO;
import com.coderrr1ck.bookBackend.bookDTOs.BookResponseDTO;
import com.coderrr1ck.bookBackend.models.Book;
import com.coderrr1ck.bookBackend.models.User;
import com.coderrr1ck.bookBackend.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final String BOOK_NOT_FOUND = "Book Not Found ";
    private final BookMapper mapper;
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository, BookMapper mapper){
        this.mapper  = mapper;
        this.bookRepository = bookRepository;
    }


    public ResponseEntity<?> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        List<BookResponseDTO> _books = books.stream().map(mapper::toBookResponse).collect(Collectors.toList());
        return ResponseEntity.ok(_books);
    }

    public ResponseEntity<?> updateBookById(UUID bookId, BookRequestDTO bookRequestDTO) {
        return ResponseEntity.ok("Book Updated");
    }

    public UUID createBook(BookRequestDTO bookRequestDTO, Authentication authenticatedUser) {
            User curr_user = (User) authenticatedUser.getPrincipal();
            Book new_book = mapper.toBook(bookRequestDTO);
            new_book.setOwner(curr_user);
            Book saved_book = bookRepository.save(new_book);
//           send api response in header location containing uuid of created book.
            return saved_book.getId();
    }

    public void deleteBookById(UUID bookId) {
        bookRepository.deleteById(bookId);
    }

    public BookResponseDTO getBookById(UUID bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(()->
            new RuntimeException(BOOK_NOT_FOUND)
        );
        return mapper.toBookResponse(book);
    }
}
