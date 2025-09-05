package com.coderrr1ck.bookBackend.feedbackDTOs;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

@Data
public class FeedbackRequestDTO {

            @Min(value = 0, message = "Feedback rating min value must be 0.")
            @Max(value = 5, message = "Feedback rating max value must be 5")
            @NotNull(message = "Feedback rating cannot be empty.")
            private Double note;

            @NotNull(message = "Feedback comment cannot be null.")
            @NotEmpty(message = "Feedback comment cannot be empty.")
            @NotBlank(message = "Feedback comment cannot be blank")
            private String comment;

            @NotNull(message = "Feedback bookId is required.")
            private UUID bookId;
}
