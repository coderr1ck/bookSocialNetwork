package com.coderrr1ck.bookBackend.controller;


import com.coderrr1ck.bookBackend.beansConfig.PageResponse;
import com.coderrr1ck.bookBackend.bookDTOs.BookRequestDTO;
import com.coderrr1ck.bookBackend.bookDTOs.BookResponseDTO;
import com.coderrr1ck.bookBackend.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<BookResponseDTO>> getAllBooks(
            @RequestParam(value = "page",defaultValue = "0") int page,
            @RequestParam(value = "size",defaultValue = "10") int size,
            @RequestParam(value = "owner",defaultValue = "false") boolean owner,
            Authentication authentication
    ){
        PageResponse<BookResponseDTO> pageResponse = bookService.getAllBooks(page,size,authentication,owner);
        return ResponseEntity.ok(pageResponse);
    }//done

    @GetMapping("{id}")
    public ResponseEntity<BookResponseDTO> getBookById(
            @PathVariable("id") UUID bookId
    ){
        return ResponseEntity.ok(bookService.getBookById(bookId));
    } // done

    @PutMapping("{id}")
    public ResponseEntity<?> updateBookById(
            @PathVariable("id") UUID bookId,
            @Valid @RequestBody BookRequestDTO bookRequestDTO
    ){
        return bookService.updateBookById(bookId,bookRequestDTO);
    }

    @PostMapping
    public ResponseEntity<Void> createBook(
            @Valid @RequestBody BookRequestDTO bookRequestDTO,
            Authentication authentication
            ){
        UUID bookId = bookService.createBook(bookRequestDTO,authentication);
        return ResponseEntity.created(URI.create(String.valueOf(bookId))).build();
    } // done

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteBookById(
            @PathVariable("id") UUID bookId
    ){
        bookService.deleteBookById(bookId);
        return ResponseEntity.noContent().build();
    } // done

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponseDTO>> findAllBooksByOwner(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.findAllBooksByOwner(page, size, connectedUser));
    }

}
