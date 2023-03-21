package ru.min.simleshopapims.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.min.simleshopapims.exception.DontExistsByNameException;
import ru.min.simleshopapims.exception.MyValidationException;
import ru.min.simleshopapims.exception.NotFoundByIdException;
import ru.min.simleshopapims.model.Characteristic;
import ru.min.simleshopapims.model.Discount;
import ru.min.simleshopapims.model.Product;
import ru.min.simleshopapims.model.dto.DiscountDto;
import ru.min.simleshopapims.repository.DiscountRepository;
import ru.min.simleshopapims.repository.ProductRepository;
import ru.min.simleshopapims.security.model.User;
import ru.min.simleshopapims.security.repository.UserRepository;
import ru.min.simleshopapims.service.DiscountService;
import ru.min.simleshopapims.service.ValidationService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;
    private final ValidationService validationService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public Discount createDiscount(DiscountDto discountDto) throws MyValidationException {
        Discount discount = new Discount(discountDto.getDiscountInPercent(), discountDto.getStartOfDiscount(), discountDto.getEndOfDiscount());
        if (validationService.validateDiscount(discount)) {
            return discountRepository.save(discount);
        } else {
            throw new MyValidationException("Discount has invalid fields");
        }
    }

    @Override
    public void deleteDiscountById(Long id) {
        if (discountRepository.existsById(id)) {
            discountRepository.delete(discountRepository.findById(id).get());
        } else {
            throw new NotFoundByIdException("Discount not found by ID!");
        }
    }

    @Override
    public Discount updateDiscount(DiscountDto discountDto, Long id) {
        Discount discount = new Discount(discountDto.getDiscountInPercent(), discountDto.getStartOfDiscount(), discountDto.getEndOfDiscount());
        discount.setNamesOfProducts(discountRepository.findById(id).get().getNamesOfProducts());
        if (discountRepository.existsById(id)) {
            if (validationService.validateDiscount(discount)) {
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

    /**
     * для админа
     * @param productsId
     * @param discountId
     * @return
     */
    @Override
    public List<Product> setDiscountToListOfProducts(List<Long> productsId, Long discountId) {
        if (!discountRepository.existsById(discountId)){
            throw new NotFoundByIdException("Discount not found!");
        }
        for (Long param : productsId) {
            if (!productRepository.existsById(param)){
                throw new NotFoundByIdException("Some products not found!");
            }
        }
        List<Product> productList = new ArrayList<>();
        for (Long aLong : productsId) {
            Product product = productRepository.findById(aLong).get();
            productList.add(product);
        }
        if (productList.size() == productsId.size()) {
            Discount discount = discountRepository.findById(discountId).get();
            discount.setNamesOfProducts(productList);
            discountRepository.save(discount);
            return productList.stream()
                    .peek(x -> x.setDiscount(discount))
                    .peek(productRepository::save)
                    .collect(Collectors.toList());
        } else {
            throw new DontExistsByNameException("Some products not found!");
        }

    }

    /**
     * для админа
     * @param productId
     * @param discountId
     * @return
     */
    @Override
    public Product setDiscountToProduct(Long productId, Long discountId) {
        if (!discountRepository.existsById(discountId)){
            throw new NotFoundByIdException("Discount not found!");
        }
        if (productRepository.existsById(productId)) {
            Product pr = productRepository.findById(productId).get();
            pr.setDiscount(discountRepository.findById(discountId).get());
            Discount discount = discountRepository.findById(discountId).get();
            List<Product> products = discount.getNamesOfProducts();
            products.add(pr);
            discount.setNamesOfProducts(products);
            discountRepository.save(discount);
            return productRepository.save(pr);
        } else {
            throw new DontExistsByNameException("Product not found!");
        }
    }

    /**
     * для пользователя
     * @param productsId
     * @param discountId
     * @return
     */
    @Override
    public List<Product> setDiscountToListOfOwnProducts(List<Long> productsId, Long discountId) {
        if (!discountRepository.existsById(discountId)){
            throw new NotFoundByIdException("Discount not found!");
        }
        for (Long param : productsId) {
            if (!productRepository.existsById(param)){
                throw new NotFoundByIdException("Some products not found!");
            }
        }
        User user = userRepository.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<Product> productList = new ArrayList<>();
        for (Long aLong : productsId) {
            Product product = productRepository.findById(aLong).get();
            if (user.getOrganizations().stream().flatMap(x -> x.getProducts().stream()).filter(v -> v.equals(product)).findAny().isPresent()){
                productList.add(product);
            }
        }
        if (productList.size() == productsId.size()) {
            Discount discount = discountRepository.findById(discountId).get();

            discount.setNamesOfProducts(productList);
            discountRepository.save(discount);
            return productList.stream()
                    .peek(x -> x.setDiscount(discount))
                    .peek(productRepository::save)
                    .collect(Collectors.toList());
        } else {
            throw new DontExistsByNameException("Some products not found!");
        }

    }

    /**
     * для пользователя
     * @param productId
     * @param discountId
     * @return
     */
    @Override
    public Product setDiscountToOwnProduct(Long productId, Long discountId) {
        if (!discountRepository.existsById(discountId)){
            throw new NotFoundByIdException("Discount not found!");
        }
        User user = userRepository.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Product pr = user.getOrganizations().stream()
                .flatMap(x -> x.getProducts().stream())
                .filter(v -> v.equals(productRepository.findById(productId)))
                .findFirst().orElse(null);
        if (pr != null){
            Product product1 = productRepository.findById(productId).get();
            Discount discount = discountRepository.findById(discountId).get();
            List<Product> products = discount.getNamesOfProducts();
            products.add(product1);
            discount.setNamesOfProducts(products);
            discountRepository.save(discount);
            product1.setDiscount(discount);
            return productRepository.save(product1);
        } else {
            throw new NotFoundByIdException("Product not found!");
        }
    }

    @Override
    public List<Product> showListOfProductsByDiscountId(Long id){
        if (productRepository.existsById(id)) {
            return discountRepository.findById(id).get().getNamesOfProducts();
        } else {
            throw new NotFoundByIdException("Discount not found!");
        }
    }
}
