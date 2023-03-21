package ru.min.simleshopapims.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.min.simleshopapims.exception.NotFoundByIdException;
import ru.min.simleshopapims.model.Purchase;
import ru.min.simleshopapims.repository.PurchaseRepository;
import ru.min.simleshopapims.security.repository.UserRepository;
import ru.min.simleshopapims.service.PurchaseService;
import ru.min.simleshopapims.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;

    @Override
    public Purchase createPurchase(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    @Override
    public void deletePurchase(Long id) {
        if (purchaseRepository.existsById(id)){
            purchaseRepository.delete(purchaseRepository.findById(id).get());
        } else {
            throw new NotFoundByIdException("Purchase not found!");
        }
    }

    @Override
    public Purchase updatePurchase(Purchase purchase, Long id) {
        if (purchaseRepository.existsById(id)){
            Purchase ps = purchaseRepository.findById(id).get();
            ps.setPurchaseStatus(purchase.getPurchaseStatus());
            ps.setProducts(purchase.getProducts());
            ps.setLocalDate(purchase.getLocalDate());
            ps.setUser(purchase.getUser());
            ps.setTotalCost(purchase.getTotalCost());
            return purchaseRepository.save(ps);
        } else {
            throw new NotFoundByIdException("Purchase not found!");
        }
    }

    @Override
    public List<Purchase> findAllByUsername(String username){
        return purchaseRepository.findAllByUser(userRepository.findUserByUsername(username));
    }

    @Override
    public List<Purchase> findAll() {
        return purchaseRepository.findAll();
    }

    @Override
    public List<Purchase> findAllOwn(){
        return userRepository.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .getPurchaseList();
    }
}
