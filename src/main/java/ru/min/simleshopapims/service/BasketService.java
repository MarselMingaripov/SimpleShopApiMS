package ru.min.simleshopapims.service;

import ru.min.simleshopapims.model.Product;

import java.util.List;

public interface BasketService {
    List<Product> addToBasket(Product product);

    List<Product> returnBasket();

    double returnTotalCost();
}
