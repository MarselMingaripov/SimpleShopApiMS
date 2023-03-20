package ru.min.simleshopapims.service;

import ru.min.simleshopapims.model.Product;

import java.util.List;

public interface BasketService {
    List<Product> addToBasket(String productName);

    List<Product> returnBasket();

    double returnTotalCost();
}
