package ru.min.simleshopapims.service;

import ru.min.simleshopapims.exception.MyValidationException;
import ru.min.simleshopapims.model.Discount;
import ru.min.simleshopapims.model.Product;

import java.util.List;

public interface DiscountService {

    Discount createDiscount(Discount discount) throws MyValidationException;
    void deleteDiscountById(Long id);
    Discount updateDiscount(Discount discount, Long id);
    List<Discount> findAll();
    List<Product> setDiscountToListOfProducts(List<Product> products, Discount discount);
    Product setDiscountToProduct(Product product, Discount discount);
}
