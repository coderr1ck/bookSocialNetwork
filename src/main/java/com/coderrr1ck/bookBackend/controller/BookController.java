package com.coderrr1ck.bookBackend.controller;


import com.coderrr1ck.bookBackend.beansConfig.PageResponse;
import com.coderrr1ck.bookBackend.bookDTOs.BookRequestDTO;
import com.coderrr1ck.bookBackend.bookDTOs.BookResponseDTO;
import com.coderrr1ck.bookBackend.bookDTOs.BorrowedBookResponseDTO;
import com.coderrr1ck.bookBackend.service.BookService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.OperationNotSupportedException;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("books")
@Tag(name = "Books", description = "Book management APIs")
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

    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedBookResponseDTO>> findAllBooksBorrowedByUser(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.findAllBooksBorrowedByUser(page, size, connectedUser));
    } // done

    @GetMapping("/returned")
    public ResponseEntity<PageResponse<BorrowedBookResponseDTO>> findAllBooksReturnedByUser(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.findAllBooksReturnedToUser(page, size, connectedUser));
    } // done

    @PatchMapping("shareable/{book-id}")
    public ResponseEntity<Void> updateSharable(
            @PathVariable("book-id") UUID bookId,
            Authentication authentication
    ) throws OperationNotSupportedException {
        UUID updatedBookId = bookService.updateBookSharable(bookId,authentication);
        return ResponseEntity.created(URI.create(String.valueOf(updatedBookId))).build();
    }

    @PatchMapping("archive/{book-id}")
    public ResponseEntity<Void> updateArchived(
            @PathVariable("book-id") UUID bookId,
            Authentication authentication
    ) throws OperationNotSupportedException {
        UUID updatedBookId = bookService.updateBookArchived(bookId,authentication);
        return ResponseEntity.created(URI.create(String.valueOf(updatedBookId))).build();
    }


    @PostMapping("borrow/{book-id}")
    public ResponseEntity<Void> borrowBook(
            @PathVariable("book-id") UUID bookId,
            Authentication authentication
    ) throws OperationNotSupportedException{
        UUID bthId = bookService.borrowBook(bookId,authentication);
        return ResponseEntity.created(URI.create(String.valueOf(bthId))).build();
    }

    @PostMapping("return/borrowed/{book-id}")
    public ResponseEntity<Void> returnBookwedBook(
            @PathVariable("book-id") UUID bookId,
            Authentication authentication
    ) throws OperationNotSupportedException{
        UUID bthId = bookService.returnBorrowedBook(bookId,authentication);
        return ResponseEntity.created(URI.create(String.valueOf(bthId))).build();
    }

    @PostMapping("approve/return/borrowed/{book-id}")
    public ResponseEntity<Void> approveReturnOfBorrowedBook(
            @PathVariable("book-id") UUID bookId,
            Authentication authentication
    ){
       bookService.approveBookReturn(bookId,authentication);
       return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "upload/cover/{book-id}",consumes = "multipart/form-data")
    public ResponseEntity<Void> uploadBookCover(
            @Parameter()
            @PathVariable("book-id") UUID bookId,
            Authentication authentication,
            @RequestPart("file") MultipartFile file
            ){
      bookService.uploadBookCover(bookId,authentication,file);
      return ResponseEntity.accepted().build();
    }

}
