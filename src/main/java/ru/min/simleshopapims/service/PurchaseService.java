package ru.min.simleshopapims.service;

import ru.min.simleshopapims.exception.MyValidationException;
import ru.min.simleshopapims.model.Purchase;

import java.util.List;

public interface PurchaseService {

    Purchase createPurchase(Purchase purchase);
    void deletePurchase(Long id);
    Purchase updatePurchase(Purchase purchase, Long id);

    List<Purchase> findAllByUsername(String username);

    List<Purchase> findAll();

}
