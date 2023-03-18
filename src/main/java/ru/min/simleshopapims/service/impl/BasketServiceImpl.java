package ru.min.simleshopapims.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.min.simleshopapims.model.Product;
import ru.min.simleshopapims.service.BasketService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasketServiceImpl implements BasketService {

    private final List<Product> basket = new ArrayList<>();

    @Override
    public List<Product> addToBasket(Product product){
        basket.add(product);
        return basket;
    }

    @Override
    public List<Product> returnBasket(){
        return basket;
    }

    @Override
    public double returnTotalCost(){
        return basket.stream()
                .peek(x -> x.getCost())
                .count();
    }
}
