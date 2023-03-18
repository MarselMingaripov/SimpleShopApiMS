package ru.min.simleshopapims.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.min.simleshopapims.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
