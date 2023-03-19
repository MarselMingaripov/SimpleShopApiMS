package ru.min.simleshopapims.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.min.simleshopapims.exception.*;
import ru.min.simleshopapims.model.*;
import ru.min.simleshopapims.repository.PurchaseRepository;
import ru.min.simleshopapims.security.model.AccountStatus;
import ru.min.simleshopapims.security.model.User;
import ru.min.simleshopapims.security.repository.UserRepository;
import ru.min.simleshopapims.service.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ProductService productService;
    private final PurchaseService purchaseService;
    private final UserRepository userRepository;
    private final BasketService basketService;
    private final GradeService gradeService;
    private final ReviewService reviewService;
    private final PurchaseRepository purchaseRepository;
    private final OrganizationService organizationService;


    @Override
    public Double setBalance(User user, double amount) {
        user.setBalance(user.getBalance() + amount);
        return user.getBalance();
    }

    @Override
    public User getCurrentUser() {
        return userRepository.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    public boolean checkStatus(User user) {
        if (user.getAccountStatus() != AccountStatus.FROZEN) {
            return true;
        } else {
            throw new AccountIsFrozenException("Sorry, your account is frozen(");
        }
    }

    @Override
    public Purchase buy() {
        checkStatus(getCurrentUser());
        Purchase purchase = new Purchase(LocalDate.now(), basketService.returnBasket(), getCurrentUser(), basketService.returnTotalCost());
        purchase.setPurchaseStatus(PurchaseStatus.INPROCESS);
        if (getCurrentUser().getBalance() >= basketService.returnTotalCost()) {
            purchase.setPurchaseStatus(PurchaseStatus.COMPLETED);
            purchaseService.createPurchase(purchase);
            getCurrentUser().getPurchaseList().add(purchase);
            List<Product> products = basketService.returnBasket().stream()
                    .peek(x -> x.setStockBalance(productService.findByName(x.getName()).getStockBalance() - x.getStockBalance()))
                    .peek(x -> productService.updateProduct(x, x.getId()))
                    .peek(x -> organizationService.addProfit(x.getOrganization(),
                            x.getOrganization().getProfit() + (x.getCost() - x.getCost() * 0.05)))
                    .collect(Collectors.toList());

            return purchase;
        } else {
            throw new TooMuchMoneyException("You dont have so much money(");
        }
    }

    @Override
    public Purchase purchaseReturns(Product product) {
        checkStatus(getCurrentUser());
        List<Purchase> purchases = getCurrentUser().getPurchaseList();
        Product pr = purchases.stream()
                .flatMap(x -> x.getProducts().stream())
                .filter(v -> v.equals(product))
                .findFirst().orElse(null);
        if (pr != null) {
            Purchase purchase = purchases.stream()
                    .filter(x -> x.getProducts().contains(product))
                    .findFirst().get();
            if (purchase.getLocalDate().plusDays(1).isAfter(LocalDate.now())) {
                purchase.getProducts().remove(product);
                Organization organization = product.getOrganization();
                organizationService.addProfit(organization, organization.getProfit() - (product.getCost() - product.getCost() * 0.05));
                Product returnedProduct = productService.findByName(product.getName());
                returnedProduct.setStockBalance(returnedProduct.getStockBalance() + product.getStockBalance());
                productService.updateProduct(returnedProduct, returnedProduct.getId());
                return purchaseService.updatePurchase(purchase, purchase.getId());
            } else {
                throw new ItsTooLateException("Sorry, its too late to return your purchase. You can return it during a day!");
            }
        } else {
            throw new DontExistsByNameException("You dont bought this product!");
        }
    }

    @Override
    public Product putGrade(Product product, int grade) {
        checkStatus(getCurrentUser());
        Grade gr = new Grade(grade);
        if (getCurrentUser().getPurchaseList().contains(product)) {
            gradeService.createGrade(gr);
            Product pr = getCurrentUser().getPurchaseList().stream()
                    .flatMap(x -> x.getProducts().stream())
                    .filter(v -> v.equals(product))
                    .findFirst().get();
            List<Grade> gradeList = pr.getGrade();
            gradeList.add(gr);
            pr.setGrade(gradeList);
            return pr;
        } else {
            throw new ProductIsNotBoughtException("Product is not bought!");
        }
    }

    @Override
    public Product putReview(Product product, String name, String review) {
        checkStatus(getCurrentUser());
        Review rv = new Review(name, review, getCurrentUser().getUsername());
        if (getCurrentUser().getPurchaseList().contains(product)) {
            reviewService.createReview(rv);
            Product pr = getCurrentUser().getPurchaseList().stream()
                    .flatMap(x -> x.getProducts().stream())
                    .filter(v -> v.equals(product))
                    .findFirst().get();
            Set<Review> reviews = pr.getReviews();
            reviews.add(rv);
            pr.setReviews(reviews);
            return pr;
        } else {
            throw new ProductIsNotBoughtException("Product is not bought!");
        }
    }

    @Override
    public List<Purchase> findAllByCurrentUser() {
        return purchaseRepository.findAllByUser(getCurrentUser());
    }

    @Override
    public AccountStatus changeAccountStatus(User user, AccountStatus accountStatus){
        user.setAccountStatus(accountStatus);
        return accountStatus;
    }

    @Override
    public List<Notification> readNotifications(){
        List<Notification> notifications = getCurrentUser().getNotifications();
        return notifications.stream()
                .filter(x -> x.getNotificationStatus().equals(NotificationStatus.NOT_READ))
                .peek(x -> x.setNotificationStatus(NotificationStatus.READ))
                .collect(Collectors.toList());
    }

    @Override
    public List<Notification> showAllNotifications(){
        return getCurrentUser().getNotifications();
    }

    @Override
    public double addManyFromOrgToBalance(){
        List<Organization> organizations = organizationService.findByOwner(getCurrentUser().getUsername());
        for (Organization organization : organizations) {
            getCurrentUser().setBalance(getCurrentUser().getBalance() + organization.getProfit());
            organization.setProfit(0);
            organizationService.updateOrganization(organization, organization.getId());
        }
        return getCurrentUser().getBalance();
    }
}
