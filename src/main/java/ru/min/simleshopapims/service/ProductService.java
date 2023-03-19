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

    Double returnCostWithDiscount(Product product);

    Product findByName(String name);

    double showAvgGrade(Long id);

    Product applyToCreateProduct(Product product);

    Product setFrozenStatus(Organization organization, Product product);

    Product setActiveStatus(Organization organization, Product product);
}
