package com.coderrr1ck.bookBackend.feedbackDTOs;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackResponseDTO {
        private Double note;
        private String comment;
        private boolean ownFeedback;
}
