package ru.min.simleshopapims.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.min.simleshopapims.exception.DontExistsByNameException;
import ru.min.simleshopapims.model.Product;
import ru.min.simleshopapims.repository.ProductRepository;
import ru.min.simleshopapims.service.BasketService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasketServiceImpl implements BasketService {

    private final List<Product> basket = new ArrayList<>();
    private final ProductRepository productRepository;

    @Override
    public List<Product> addToBasket(String productName){
        if (productRepository.existsByName(productName)){
            Product product = productRepository.findByName(productName).get();
            basket.add(product);
            return basket;
        } else {
            throw new DontExistsByNameException("Product not found!");
        }
    }

    @Override
    public List<Product> returnBasket(){
        return basket;
    }

    @Override
    public double returnTotalCost(){
        double sum = 0;
        for (Product product : basket) {
            sum += product.getCost();
        }
        return sum;
    }
}
