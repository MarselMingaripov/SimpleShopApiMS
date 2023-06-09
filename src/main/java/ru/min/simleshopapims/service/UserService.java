package ru.min.simleshopapims.service;

import ru.min.simleshopapims.model.Notification;
import ru.min.simleshopapims.model.Product;
import ru.min.simleshopapims.model.Purchase;
import ru.min.simleshopapims.security.model.AccountStatus;
import ru.min.simleshopapims.security.model.User;

import java.util.List;

public interface UserService {

    Double setBalance(String username, double amount);

    User getCurrentUser();

    boolean checkStatus(User user);

    Purchase buy();

    Purchase purchaseReturns(Product product);

    Product putGrade(Long id, int grade);

    Product putReview(Long id,String name, String review);

    List<Purchase> findAllByCurrentUser();

    AccountStatus changeAccountStatus(User user, AccountStatus accountStatus);

    List<Notification> readNotifications();

    List<Notification> showAllNotifications();

    double addManyFromOrgToBalance();
}
