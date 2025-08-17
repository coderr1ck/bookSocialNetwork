package com.coderrr1ck.bookBackend.authDTOs;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BookRequestDTO {

    @NotNull(message = "book title cannot be null")
    @NotBlank(message = "book title cannot be empty")
    private String title;

    @NotNull(message = "book author name cannot be null")
    @NotBlank(message = "book author name cannot be empty")
    private String authorName;

    @NotNull(message = "book ISBN cannot be null")
    @NotBlank(message = "book ISBN cannot be empty")
    private String isbn;

    private String synopsis;

    private boolean sharable;

    @NotNull
    @NotBlank(message = "book Cover cannot be empty")
    private String bookCover;

}
