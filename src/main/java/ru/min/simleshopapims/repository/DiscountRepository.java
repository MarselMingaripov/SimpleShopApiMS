package ru.min.simleshopapims.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.min.simleshopapims.model.Discount;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

}
