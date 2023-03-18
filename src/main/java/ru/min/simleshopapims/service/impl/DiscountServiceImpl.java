package ru.min.simleshopapims.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.min.simleshopapims.exception.DontExistsByNameException;
import ru.min.simleshopapims.exception.MyValidationException;
import ru.min.simleshopapims.exception.NotFoundByIdException;
import ru.min.simleshopapims.model.Characteristic;
import ru.min.simleshopapims.model.Discount;
import ru.min.simleshopapims.model.Product;
import ru.min.simleshopapims.repository.DiscountRepository;
import ru.min.simleshopapims.repository.ProductRepository;
import ru.min.simleshopapims.service.DiscountService;
import ru.min.simleshopapims.service.ValidationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;
    private final ValidationService validationService;
    private final ProductRepository productRepository;

    @Override
    public Discount createDiscount(Discount discount) throws MyValidationException {
        if (validationService.validateDiscount(discount)) {
                return discountRepository.save(discount);
        } else {
            throw new MyValidationException("Discount has invalid fields");
        }
    }

    @Override
    public void deleteDiscountById(Long id) {
        if (discountRepository.existsById(id)){
            discountRepository.delete(discountRepository.findById(id).get());
        } else {
            throw new NotFoundByIdException("Discount not found by ID!");
        }
    }

    @Override
    public Discount updateDiscount(Discount discount, Long id) {
        if (discountRepository.existsById(id)){
            if (validationService.validateDiscount(discount)){
                Discount dc = discountRepository.findById(id).get();
                dc.setStartOfDiscount(discount.getStartOfDiscount());
                dc.setEndOfDiscount(discount.getEndOfDiscount());
                dc.setDiscountInPercent(discount.getDiscountInPercent());
                dc.setNamesOfProducts(discount.getNamesOfProducts());
                return discountRepository.save(dc);
            } else {
                throw new MyValidationException("Discount has invalid fields");
            }
        } else {
            throw new NotFoundByIdException("Discount not found by ID");
        }
    }

    @Override
    public List<Discount> findAll() {
        return discountRepository.findAll();
    }

    @Override
    public List<Product> setDiscountToListOfProducts(List<Product> products, Discount discount){
        List<Product> productList = products.stream()
                .filter(x -> productRepository.existsById(x.getId()))
                .collect(Collectors.toList());
        if (productList.size() == products.size()){
            return products.stream()
                    .peek(x -> x.setDiscount(discount))
                    .peek(productRepository::save)
                    .collect(Collectors.toList());
        } else {
            throw new NotFoundByIdException("Some products not found!");
        }

    }

    @Override
    public Product setDiscountToProduct(Product product, Discount discount) {
        if (productRepository.existsById(product.getId())) {
            Product pr = productRepository.findById(product.getId()).get();
            pr.setDiscount(discount);
            return productRepository.save(pr);
        } else {
            throw new NotFoundByIdException("Product not found!");
        }
    }
}
