package com.coderrr1ck.bookBackend.feedbackDTOs;


import com.coderrr1ck.bookBackend.models.Book;
import com.coderrr1ck.bookBackend.models.Feedback;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FeedbackMapper {
    public Feedback toFeedback(FeedbackRequestDTO request) {
        return Feedback.builder()
                .note(request.getNote())
                .comment(request.getComment())
                .book(Book.builder()
                        .id(request.getBookId())
                        .sharable(false) // Not required and has no impact :: just to satisfy lombok
                        .archived(false) // Not required and has no impact :: just to satisfy lombok
                        .build()
                )
                .build();
    }

    public FeedbackResponseDTO toFeedbackResponse(Feedback feedback, String owner) {
        return FeedbackResponseDTO.builder()
                .note(feedback.getNote())
                .comment(feedback.getComment())
                .ownFeedback(Objects.equals(feedback.getCreatedBy(), owner))
                .build();
    }
}