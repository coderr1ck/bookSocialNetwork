package com.coderrr1ck.bookBackend.controller;


import com.coderrr1ck.bookBackend.beansConfig.PageResponse;
import com.coderrr1ck.bookBackend.feedbackDTOs.FeedbackRequestDTO;
import com.coderrr1ck.bookBackend.feedbackDTOs.FeedbackResponseDTO;
import com.coderrr1ck.bookBackend.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;
    public FeedbackController(FeedbackService feedbackService){
        this.feedbackService = feedbackService;
    }

    @PostMapping()
    public ResponseEntity<Void> createFeedbackForBook(
            @Valid @RequestBody FeedbackRequestDTO feedbackDTO,
            Authentication authentication
    ){
        UUID feedbackId = feedbackService.createFeedback(feedbackDTO,authentication);
        return ResponseEntity.created(URI.create(String.valueOf(feedbackId))).build();
    }

    @GetMapping("{book-id}")
    public ResponseEntity<PageResponse<FeedbackResponseDTO>> getAllFeedbacks(
            @RequestParam(value = "page",defaultValue = "0") int page,
            @RequestParam(value = "size",defaultValue = "10") int size,
            @PathVariable("book-id") UUID bookId,
            Authentication authentication
    ){
        return ResponseEntity.ok(feedbackService.getAllFeedbacks(bookId,page,size,authentication));
    }
}
