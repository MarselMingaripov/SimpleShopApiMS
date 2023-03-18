package ru.min.simleshopapims.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.min.simleshopapims.model.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);
    Boolean existsByName(String name);
}
