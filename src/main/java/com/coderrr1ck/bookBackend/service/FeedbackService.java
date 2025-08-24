package com.coderrr1ck.bookBackend.service;

import com.coderrr1ck.bookBackend.beansConfig.PageResponse;
import com.coderrr1ck.bookBackend.exceptions.EntityNotFoundException;
import com.coderrr1ck.bookBackend.exceptions.OperationNotPermittedException;
import com.coderrr1ck.bookBackend.feedbackDTOs.FeedbackMapper;
import com.coderrr1ck.bookBackend.feedbackDTOs.FeedbackRequestDTO;
import com.coderrr1ck.bookBackend.feedbackDTOs.FeedbackResponseDTO;
import com.coderrr1ck.bookBackend.models.Book;
import com.coderrr1ck.bookBackend.models.Feedback;
import com.coderrr1ck.bookBackend.models.User;
import com.coderrr1ck.bookBackend.repository.BookRepository;
import com.coderrr1ck.bookBackend.repository.FeedbackRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

import static com.coderrr1ck.bookBackend.service.BookService.BOOK_NOT_FOUND;

@Service
public class FeedbackService {
    private static final String FEEDBACK_NOT_APPLICABLE = "You cannot give feedback for this book.";
    private final BookRepository bookRepository;
    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper mapper;
    public FeedbackService(FeedbackMapper mapper,BookRepository bookRepository,FeedbackRepository feedbackRepository){
        this.bookRepository = bookRepository;
        this.feedbackRepository = feedbackRepository;
        this.mapper = mapper;
    }

    public UUID createFeedback(FeedbackRequestDTO feedbackDTO, Authentication authentication) {
        Book book = bookRepository.findById(feedbackDTO.getBookId()).orElseThrow(()->{
            throw new EntityNotFoundException(BOOK_NOT_FOUND);
        });
        if(!book.isSharable() ||book.isArchived()){
            throw  new OperationNotPermittedException(FEEDBACK_NOT_APPLICABLE);
        }
        User user = (User) authentication.getPrincipal();
        if(book.getOwner().getId().equals(user.getId())){
            throw new OperationNotPermittedException(FEEDBACK_NOT_APPLICABLE);
        }
        Feedback feedback = mapper.toFeedback(feedbackDTO);
        Feedback savedFB = feedbackRepository.save(feedback);
        return savedFB.getId();
    }

    public PageResponse<FeedbackResponseDTO> getAllFeedbacks(UUID bookId,int page,int size,Authentication authentication) {
        Book book = bookRepository.findById(bookId).orElseThrow(()->{
            throw new EntityNotFoundException(BOOK_NOT_FOUND);
        });
        User user = (User) authentication.getPrincipal();
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdDate").descending());
        Page<Feedback> feedbacks = feedbackRepository.findAllByBookId(pageable,bookId);
        List<FeedbackResponseDTO> feedbackResponseDTOS = feedbacks.stream().map((fb)->mapper.toFeedbackResponse(fb,user.getEmail())).toList();
        return new PageResponse<FeedbackResponseDTO>(
                feedbackResponseDTOS,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );
    }
}
