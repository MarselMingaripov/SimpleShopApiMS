package ru.min.simleshopapims.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.min.simleshopapims.model.Purchase;
import ru.min.simleshopapims.security.model.User;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findAllByUser(User user);
}
