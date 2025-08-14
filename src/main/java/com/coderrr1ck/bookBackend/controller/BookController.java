package com.coderrr1ck.bookBackend.controller;


import com.coderrr1ck.bookBackend.service.BookService;
import org.springframework.http.ResponseEntity;
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

    @PutMapping("{id}")
    public ResponseEntity<?> updateBookById(
            @PathVariable UUID uuid
    ){
        return bookService.updateBookById();
    }

    @PostMapping
    public ResponseEntity<?> createBook(){
        return bookService.createBook();
    }
    @DeleteMapping
    public ResponseEntity<?> deleteBookById(){
        return bookService.deleteBookById();
    }
}
