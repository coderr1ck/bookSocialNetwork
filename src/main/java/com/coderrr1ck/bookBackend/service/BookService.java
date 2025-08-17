package com.coderrr1ck.bookBackend.service;

import com.coderrr1ck.bookBackend.authDTOs.BookRequestDTO;
import com.coderrr1ck.bookBackend.authDTOs.BookResponseDTO;
import com.coderrr1ck.bookBackend.models.Book;
import com.coderrr1ck.bookBackend.models.User;
import com.coderrr1ck.bookBackend.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final ObjectMapper mapper;
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository, ObjectMapper mapper){
        this.mapper  = mapper;
        this.bookRepository = bookRepository;
    }

    private Book toEntity(BookRequestDTO dto){
        return mapper.convertValue(dto,Book.class);
    }

    private BookResponseDTO toDTO(Book book){
        return mapper.convertValue(book, BookResponseDTO.class);
    }

    public ResponseEntity<?> getAllBooks() {
        List<BookResponseDTO> books = bookRepository.findAllBooksAsDto();
        return ResponseEntity.ok(books);
    }

    public ResponseEntity<?> updateBookById() {
        return ResponseEntity.ok("Book Updated");
    }

    public ResponseEntity<?> createBook(BookRequestDTO bookRequestDTO, Authentication authenticatedUser) {
            User curr_user = (User) authenticatedUser.getPrincipal();
            Book new_book = toEntity(bookRequestDTO);
            new_book.setOwner(curr_user);
            Book saved_book = bookRepository.save(new_book);
//           send api response in header location containing uuid of created book.
            return ResponseEntity.created(URI.create(String.valueOf(saved_book.getId()))).build();
    }

    public ResponseEntity<?> deleteBookById() {
        return ResponseEntity.ok("Book deleted");
    }

    public ResponseEntity<?> getBookById(UUID bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(()->
            new RuntimeException("Book not found")
        );
        return ResponseEntity.ok(toDTO(book));
    }
}
