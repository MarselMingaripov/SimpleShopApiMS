package ru.min.simleshopapims.service;

import ru.min.simleshopapims.model.*;

public interface ValidationService {

    boolean validateCharacteristic(Characteristic characteristic);
    boolean validateDiscount(Discount discount);
    boolean validateGrade(Grade grade);
    boolean validateKeyWord(KeyWord keyWord);
    boolean validateOrganization(Organization organization);
    boolean validateProduct(Product product);
    boolean validateReview(Review review);
}
