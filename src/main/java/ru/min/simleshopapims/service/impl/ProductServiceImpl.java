package ru.min.simleshopapims.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.min.simleshopapims.exception.DontExistsByNameException;
import ru.min.simleshopapims.exception.MyValidationException;
import ru.min.simleshopapims.exception.NotFoundByIdException;
import ru.min.simleshopapims.model.Grade;
import ru.min.simleshopapims.model.Product;
import ru.min.simleshopapims.repository.ProductRepository;
import ru.min.simleshopapims.service.ProductService;
import ru.min.simleshopapims.service.ValidationService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ValidationService validationService;
    private final ProductRepository productRepository;

    @Override
    public Product createProduct(Product product) throws MyValidationException {
        if (validationService.validateProduct(product)){
            return productRepository.save(product);
        } else {
            throw new MyValidationException("Product has invalid fields!");
        }
    }

    @Override
    public void deleteProductById(Long id) {
        if (productRepository.existsById(id)){
            productRepository.delete(productRepository.findById(id).get());
        } else {
            throw new NotFoundByIdException("Product not found!");
        }
    }

    @Override
    public Product updateProduct(Product product, Long id) {
        if (validationService.validateProduct(product)){
            if (productRepository.existsById(id)){
                Product pr = productRepository.findById(id).get();
                pr.setCharacteristic(product.getCharacteristic());
                pr.setCost(product.getCost());
                pr.setDescription(product.getDescription());
                pr.setDiscount(product.getDiscount());
                pr.setGrade(product.getGrade());
                pr.setKeyWords(product.getKeyWords());
                pr.setName(product.getName());
                pr.setOrganization(product.getOrganization());
                pr.setReviews(product.getReviews());
                pr.setStockBalance(product.getStockBalance());
                return productRepository.save(pr);
            } else {
                throw new NotFoundByIdException("Product not found!");
            }
        } else {
            throw new MyValidationException("Product has invalid fields!");
        }
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Double returnCostWithDiscount(Product product){
        if (validationService.validateDiscount(product.getDiscount())){
            product.setCost(product.getCost() * product.getDiscount().getDiscountInPercent() / 100);
            return product.getCost();
        } else {
            throw new MyValidationException("Discount has invalid fields!");
        }
    }

    @Override
    public Product findByName(String name){
        if (productRepository.existsByName(name)) {
            return productRepository.findByName(name).get();
        } else {
            throw new DontExistsByNameException("Product not found!");
        }
    }

    @Override
    public double showAvgGrade(Long id){
        if (productRepository.existsById(id)){
            List<Grade> gradeList = productRepository.findById(id).get().getGrade();
            return gradeList.stream()
                    .mapToDouble(x -> x.getGrade())
                    .average()
                    .orElse(0);
        } else {
            throw new DontExistsByNameException("Product not found!");
        }
    }
}
