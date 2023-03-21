package ru.min.simleshopapims.service;

import ru.min.simleshopapims.exception.MyValidationException;
import ru.min.simleshopapims.model.Review;

import java.util.List;

public interface ReviewService {

    Review createReview(Review review) throws MyValidationException;
    void deleteReviewById(Long id);
    Review updateReview(Review review, Long id);
    List<Review> findAll();
}
