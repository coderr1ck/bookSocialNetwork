package com.coderrr1ck.bookBackend.service;

import com.coderrr1ck.bookBackend.beansConfig.PageResponse;
import com.coderrr1ck.bookBackend.bookDTOs.BookMapper;
import com.coderrr1ck.bookBackend.bookDTOs.BookRequestDTO;
import com.coderrr1ck.bookBackend.bookDTOs.BookResponseDTO;
import com.coderrr1ck.bookBackend.exceptions.EntityNotFoundException;
import com.coderrr1ck.bookBackend.models.Book;
import com.coderrr1ck.bookBackend.models.User;
import com.coderrr1ck.bookBackend.models.common.BookSpecification;
import com.coderrr1ck.bookBackend.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BookService {
    private final String BOOK_NOT_FOUND = "Book Not Found ";
    private final BookMapper mapper;
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository, BookMapper mapper){
        this.mapper  = mapper;
        this.bookRepository = bookRepository;
    }


    public PageResponse<BookResponseDTO> getAllBooks(int page, int size, Authentication authentication, boolean owner) {
        User user = (User) authentication.getPrincipal();
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable,String.valueOf(user.getId()));
        List<BookResponseDTO> _books = books.stream().map(mapper::toBookResponse).toList();
        return new PageResponse<>(
                _books,
                page,
                size,
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast());
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
            new EntityNotFoundException(BOOK_NOT_FOUND)
        );
        return mapper.toBookResponse(book);
    }

    public PageResponse<BookResponseDTO> findAllBooksByOwner(int page, int size, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAll(BookSpecification.withOwnerId(user),pageable);
        List<BookResponseDTO> _books = books.stream().map(mapper::toBookResponse).toList();
        return new PageResponse<>(
                _books,
                page,
                size,
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast());
    }
}
