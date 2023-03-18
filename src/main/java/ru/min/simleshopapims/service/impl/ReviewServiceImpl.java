package ru.min.simleshopapims.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.min.simleshopapims.exception.DontExistsByNameException;
import ru.min.simleshopapims.exception.MyValidationException;
import ru.min.simleshopapims.exception.NotFoundByIdException;
import ru.min.simleshopapims.model.Review;
import ru.min.simleshopapims.repository.ReviewRepository;
import ru.min.simleshopapims.service.ReviewService;
import ru.min.simleshopapims.service.ValidationService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ValidationService validationService;
    private final ReviewRepository reviewRepository;

    @Override
    public Review createReview(Review review) throws MyValidationException {
        if (validationService.validateReview(review)){
            return reviewRepository.save(review);
        } else {
            throw new MyValidationException("Review has invalid fields");
        }
    }

    @Override
    public void deleteReviewById(Long id) {
        if (reviewRepository.existsById(id)){
            reviewRepository.delete(reviewRepository.findById(id).get());
        } else {
            throw new NotFoundByIdException("Review not found!");
        }
    }

    @Override
    public Review updateReview(Review review, Long id) {
        if (validationService.validateReview(review)){
            if (reviewRepository.existsById(id)){
                Review rw = reviewRepository.findById(id).get();
                rw.setName(review.getName());
                rw.setDescription(review.getDescription());
                rw.setAuthor(review.getAuthor());
                return reviewRepository.save(rw);
            } else {
                throw new NotFoundByIdException("Review not found!");
            }
        } else {
            throw new MyValidationException("Review has invalid fields!");
        }
    }

    @Override
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }
}
