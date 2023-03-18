package ru.min.simleshopapims.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.min.simleshopapims.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
