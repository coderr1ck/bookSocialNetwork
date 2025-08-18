package com.coderrr1ck.bookBackend.bookDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

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

    @NotNull(message = "book Synopsis cannot be null")
    @NotBlank(message = "book Synopsis cannot be empty")
    private String synopsis;

    private boolean sharable;

}
