package com.coderrr1ck.bookBackend.authDTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class BookResponseDTO {

    public BookResponseDTO(
            UUID book_id,
            String bookTitle,
            String bookAuthorName,
            String bookISBN,
            String bookSynopsis,
            boolean isBookSharable,
            String bookCover,
            boolean isBookArchived
    ) {
        this.book_id = book_id;
        this.bookTitle = bookTitle;
        this.bookAuthorName = bookAuthorName;
        this.bookISBN = bookISBN;
        this.bookSynopsis = bookSynopsis;
        this.isBookSharable = isBookSharable;
        this.bookCover = bookCover;
        this.isBookArchived = isBookArchived;
    }


    @JsonProperty("id")
    private UUID book_id;

    @JsonProperty("title")
    private String bookTitle;

    @JsonProperty("authorName")
    private String bookAuthorName;

    @JsonProperty("isbn")
    private String bookISBN;

    @JsonProperty("synopsis")
    private String bookSynopsis;

    @JsonProperty("sharable")
    private boolean isBookSharable;

    private String bookCover;

    @JsonProperty("archived")
    private boolean isBookArchived;
}
