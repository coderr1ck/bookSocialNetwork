package com.coderrr1ck.bookBackend.controller;


import com.coderrr1ck.bookBackend.authDTOs.BookRequestDTO;
import com.coderrr1ck.bookBackend.service.BookService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<?> getAllBooks(){
        return bookService.getAllBooks();
    }
    @GetMapping({"{id}"})
    public ResponseEntity<?> getBookById(
            @PathVariable("id") UUID bookId
    ){
        return bookService.getBookById(bookId);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateBookById(
            @PathVariable UUID uuid
    ){
        return bookService.updateBookById();
    }

    @PostMapping
    public ResponseEntity<?> createBook(
            @Valid @RequestBody BookRequestDTO bookRequestDTO
            ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return bookService.createBook(bookRequestDTO,authentication);
    }
    @DeleteMapping
    public ResponseEntity<?> deleteBookById(){
        return bookService.deleteBookById();
    }
}
