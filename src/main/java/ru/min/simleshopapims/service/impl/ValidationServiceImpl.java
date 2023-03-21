package ru.min.simleshopapims.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import ru.min.simleshopapims.model.*;
import ru.min.simleshopapims.service.ValidationService;

import java.time.LocalDate;

@Service
public class ValidationServiceImpl implements ValidationService {
    @Override
    public boolean validateCharacteristic(Characteristic characteristic) {
        return (StringUtils.isNotBlank(characteristic.getName()) &&
                StringUtils.isNotEmpty(characteristic.getName()));
    }

    @Override
    public boolean validateDiscount(Discount discount) {

        return (discount.getDiscountInPercent() > 0 &&
                discount.getDiscountInPercent() <= 100 &&
                !discount.getStartOfDiscount().isBefore(LocalDate.now()) &&
                discount.getEndOfDiscount().isAfter(LocalDate.now()));
    }

    @Override
    public boolean validateGrade(Grade grade) {

        return (grade.getGrade() < 6 &&
                grade. getGrade() > 0);
    }

    @Override
    public boolean validateKeyWord(KeyWord keyWord) {

        return (StringUtils.isNotBlank(keyWord.getName()) &&
                StringUtils.isNotEmpty(keyWord.getName()));
    }

    @Override
    public boolean validateOrganization(Organization organization) {

        return (StringUtils.isNotBlank(organization.getName()) &&
                StringUtils.isNotBlank(organization.getDescription()) &&
                StringUtils.isNotBlank(organization.getRefToLogo()) &&
                StringUtils.isNotEmpty(organization.getName()) &&
                StringUtils.isNotEmpty(organization.getDescription()) &&
                StringUtils.isNotEmpty(organization.getRefToLogo()));
    }

    @Override
    public boolean validateProduct(Product product) {

        return (StringUtils.isNotBlank(product.getName()) &&
                StringUtils.isNotEmpty(product.getName()) &&
                StringUtils.isNotBlank(product.getDescription()) &&
                StringUtils.isNotEmpty(product.getDescription()) &&
                (product.getCost() > 0) &&
                (product.getStockBalance() > -1));
    }

    @Override
    public boolean validateReview(Review review) {

        return (StringUtils.isNotBlank(review.getName()) &&
                StringUtils.isNotEmpty(review.getName()) &&
                StringUtils.isNotBlank(review.getDescription()) &&
                StringUtils.isNotEmpty(review.getDescription()) &&
                StringUtils.isNotBlank(review.getAuthor()) &&
                StringUtils.isNotEmpty(review.getAuthor()));
    }

    @Override
    public boolean validateNotification(Notification notification){
        return (StringUtils.isNotBlank(notification.getHeader()) &&
                StringUtils.isNotEmpty(notification.getHeader()) &&
                StringUtils.isNotEmpty(notification.getDescription()) &&
                StringUtils.isNotBlank(notification.getDescription()) &&
                StringUtils.isNotBlank(notification.getRecipient()) &&
                StringUtils.isNotEmpty(notification.getRecipient()));
    }
}
