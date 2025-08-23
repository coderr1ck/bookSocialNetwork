package com.coderrr1ck.bookBackend.service;

import com.coderrr1ck.bookBackend.beansConfig.PageResponse;
import com.coderrr1ck.bookBackend.bookDTOs.BookMapper;
import com.coderrr1ck.bookBackend.bookDTOs.BookRequestDTO;
import com.coderrr1ck.bookBackend.bookDTOs.BookResponseDTO;
import com.coderrr1ck.bookBackend.bookDTOs.BorrowedBookResponseDTO;
import com.coderrr1ck.bookBackend.exceptions.EntityNotFoundException;
import com.coderrr1ck.bookBackend.exceptions.OperationNotPermittedException;
import com.coderrr1ck.bookBackend.models.Book;
import com.coderrr1ck.bookBackend.models.BookTransactionHistory;
import com.coderrr1ck.bookBackend.models.User;
import com.coderrr1ck.bookBackend.models.common.BookSpecification;
import com.coderrr1ck.bookBackend.repository.BookRepository;
import com.coderrr1ck.bookBackend.repository.BookTransactionHistoryRepository;
import org.hibernate.cache.spi.support.CacheUtils;
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
    private final String BOOK_NOT_BORROWABLE = "You cannot borrow this book.";
    private final String BOOK_NOT_RETURNABLE = "You can not return this book.";
    private final String BOOK_RETURN_NOT_APPROVABLE = "You can not approve return of this book.";
    private final BookMapper mapper;
    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;

    public BookService(BookRepository bookRepository, BookMapper mapper,BookTransactionHistoryRepository bookTransactionHistoryRepository){
        this.mapper  = mapper;
        this.bookRepository = bookRepository;
        this.bookTransactionHistoryRepository = bookTransactionHistoryRepository;
    }


    public PageResponse<BookResponseDTO> getAllBooks(int page, int size, Authentication authentication, boolean owner) {
        User user = (User) authentication.getPrincipal();
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable,user.getId());
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

    public UUID borrowBook(UUID bookId,Authentication authentication) throws OperationNotPermittedException{
        User user = (User) authentication.getPrincipal();
        Book book = bookRepository.findById(bookId).orElseThrow(()->{
            throw new EntityNotFoundException(BOOK_NOT_FOUND);
        });
        if(book.isArchived() || !book.isSharable()){
            throw new OperationNotPermittedException(BOOK_NOT_BORROWABLE);
        }
        if(book.getOwner().getId().equals(user.getId())){
            throw new OperationNotPermittedException(BOOK_NOT_BORROWABLE);
        }
        if(bookTransactionHistoryRepository.existsByBookIdAndIsReturnApprovedFalse(book.getId())){
            throw new OperationNotPermittedException(BOOK_NOT_BORROWABLE);
        }
        BookTransactionHistory bth = BookTransactionHistory.builder()
                .user(user)
                .book(book)
                .isReturned(false)
                .isReturnApproved(false)
                .build();
        
        BookTransactionHistory saved_bth = bookTransactionHistoryRepository.save(bth);
        
//        if it is sharable or archived you cannot borrow it ,
//        if your are owner you cannot boorow it,
//        if it is already borrowed by someoneElse and not returned you cannot borrow it,
//        if it is already borrowed by the borrower himself it cannot be borrowed again,
//        finally borrow the book  , this you have to think as a developer.
        
        return saved_bth.getId();

    }

    public PageResponse<BorrowedBookResponseDTO> findAllBooksBorrowedByUser(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page,size,Sort.by("createdDate").descending());
        Page<BookTransactionHistory> borrowedBooks  = bookTransactionHistoryRepository.findAllBorrowedBooks(pageable,user.getId());
        List<BorrowedBookResponseDTO> borrowedResponse = borrowedBooks.stream().map(mapper::toBorrowedBookResponse).toList();
        return new PageResponse<>(
                borrowedResponse,
                page,
                size,
                borrowedBooks.getTotalElements(),
                borrowedBooks.getTotalPages(),
                borrowedBooks.isFirst(),
                borrowedBooks.isLast()
        );
    }

    public PageResponse<BorrowedBookResponseDTO> findAllBooksReturnedToUser(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page,size,Sort.by("createdDate").descending());
        Page<BookTransactionHistory> borrowedBooks  = bookTransactionHistoryRepository.findAllReturnedBooks(pageable,user.getId());
        List<BorrowedBookResponseDTO> borrowedResponse = borrowedBooks.stream().map(mapper::toBorrowedBookResponse).toList();
        return new PageResponse<>(
                borrowedResponse,
                page,
                size,
                borrowedBooks.getTotalElements(),
                borrowedBooks.getTotalPages(),
                borrowedBooks.isFirst(),
                borrowedBooks.isLast()
        );
    }


    public UUID updateBookSharable(UUID bookId, Authentication authentication) throws OperationNotPermittedException {
        User user  = (User) authentication.getPrincipal();
        Book book = bookRepository.findById(bookId).orElseThrow(()->{
           throw  new EntityNotFoundException(BOOK_NOT_FOUND);
        });
        if (!book.getOwner().getId().equals(user.getId())) {
            throw  new OperationNotPermittedException("You cannot update the sharable status of book.");
        }
        book.setSharable(!book.isSharable());
        bookRepository.save(book);
        return book.getId();
    }

    public UUID updateBookArchived(UUID bookId, Authentication authentication) throws OperationNotPermittedException {
        User user  = (User) authentication.getPrincipal();
        Book book = bookRepository.findById(bookId).orElseThrow(()->{
            throw  new EntityNotFoundException(BOOK_NOT_FOUND);
        });
        if (!book.getOwner().getId().equals(user.getId())) {
            throw  new OperationNotPermittedException("You cannot update the archived status of book.");
        }
        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return book.getId();
    }

    public UUID returnBorrowedBook(UUID bookId, Authentication authentication) throws OperationNotPermittedException{
        User user = (User) authentication.getPrincipal();
        Book book = bookRepository.findById(bookId).orElseThrow(()->{
            throw new EntityNotFoundException(BOOK_NOT_FOUND);
        });
        if(book.isArchived() || !book.isSharable()){
            throw new OperationNotPermittedException(BOOK_NOT_RETURNABLE);
        }
        if(book.getOwner().getId().equals(user.getId())){
            throw new OperationNotPermittedException(BOOK_NOT_RETURNABLE);
        }
        BookTransactionHistory bth = bookTransactionHistoryRepository.findByBookIdAndUserIdAndIsReturnApprovedFalseAndIsReturnedFalse(bookId,user.getId()).orElseThrow(()->{
            throw new EntityNotFoundException(BOOK_NOT_RETURNABLE);
        });
        bth.setReturned(true);
        BookTransactionHistory updated_bth = bookTransactionHistoryRepository.save(bth);
        return updated_bth.getId();
    }

    public void approveBookReturn(UUID bookId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Book book = bookRepository.findById(bookId).orElseThrow(()->{
            throw new EntityNotFoundException(BOOK_NOT_FOUND);
        });
        if(book.isArchived() || !book.isSharable()){
            throw new OperationNotPermittedException(BOOK_RETURN_NOT_APPROVABLE);
        }
        if(book.getOwner().getId().equals(user.getId())){
            BookTransactionHistory bth = bookTransactionHistoryRepository.findByBookIdAndIsReturnApprovedFalseAndIsReturnedTrue(bookId).orElseThrow(()->{
                throw new EntityNotFoundException(BOOK_RETURN_NOT_APPROVABLE);
            });
            bth.setReturnApproved(true);
            bookTransactionHistoryRepository.save(bth);
        }else{
            throw new OperationNotPermittedException(BOOK_RETURN_NOT_APPROVABLE);
        }
    }
}
