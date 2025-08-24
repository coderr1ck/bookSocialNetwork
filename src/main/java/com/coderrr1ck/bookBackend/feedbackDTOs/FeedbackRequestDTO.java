package com.coderrr1ck.bookBackend.feedbackDTOs;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

@Data
public class FeedbackRequestDTO {

            @Positive(message = "Feedback rating cannot be negative.")
            @Min(value = 0, message = "Feedback min value must be zero.")
            @Max(value = 5, message = "Feedback max value must be 5")
            @NotNull(message = "Feedback rating cannot be empty.")
            private Double note;

            @NotNull(message = "Feedback comment cannot be null.")
            @NotEmpty(message = "Feedback comment cannot be empty.")
            @NotBlank(message = "Feedback comment cannot be blank")
            private String comment;

            @NotNull(message = "Feedback bookId is required.")
            private UUID bookId;
}
