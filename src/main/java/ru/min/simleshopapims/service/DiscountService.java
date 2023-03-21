package ru.min.simleshopapims.service;

import ru.min.simleshopapims.exception.MyValidationException;
import ru.min.simleshopapims.model.Discount;
import ru.min.simleshopapims.model.Product;
import ru.min.simleshopapims.model.dto.DiscountDto;

import java.util.List;

public interface DiscountService {

    Discount createDiscount(DiscountDto discount) throws MyValidationException;
    void deleteDiscountById(Long id);
    Discount updateDiscount(DiscountDto discountDto, Long id);
    List<Discount> findAll();
    List<Product> setDiscountToListOfProducts(List<Long> productsId, Long discountId);
    Product setDiscountToProduct(Long productId, Long discountId);

    List<Product> setDiscountToListOfOwnProducts(List<Long> productsId, Long discountId);

    Product setDiscountToOwnProduct(Long productId, Long discountId);

    List<Product> showListOfProductsByDiscountId(Long id);
}
