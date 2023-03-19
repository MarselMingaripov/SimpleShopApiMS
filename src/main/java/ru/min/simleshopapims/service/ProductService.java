package ru.min.simleshopapims.service;

import ru.min.simleshopapims.exception.MyValidationException;
import ru.min.simleshopapims.model.Organization;
import ru.min.simleshopapims.model.Product;

import java.util.List;

public interface ProductService {

    Product createProduct(Product product) throws MyValidationException;
    void deleteProductById(Long id);
    Product updateProduct(Product product, Long id);
    List<Product> findAll();

    List<Product> findAllWithDiscount();

    List<Product> findAllWithAS();

    Double returnCostWithDiscount(Product product);

    Product findByName(String name);

    double showAvgGrade(Long id);

    Product applyToCreateProduct(Product product);

    Product updateOwnProduct(Product product, Long id);

    Product setFrozenStatus(String organization, Product product);

    Product setActiveStatus(String organization, Product product);
}
