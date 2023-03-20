package ru.min.simleshopapims.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.min.simleshopapims.exception.DontExistsByNameException;
import ru.min.simleshopapims.exception.MyValidationException;
import ru.min.simleshopapims.exception.NotFoundByIdException;
import ru.min.simleshopapims.exception.OrgStatusException;
import ru.min.simleshopapims.model.*;
import ru.min.simleshopapims.model.dto.ProductDto;
import ru.min.simleshopapims.repository.ProductRepository;
import ru.min.simleshopapims.security.model.User;
import ru.min.simleshopapims.security.repository.UserRepository;
import ru.min.simleshopapims.service.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ValidationService validationService;
    private final ProductRepository productRepository;
    private final OrganizationService organizationService;
    private final UserRepository userRepository;
    private final DiscountService discountService;
    private final CharacteristicService characteristicService;
    private final KeyWordService keyWordService;

    /**
     * для админа создает активный продукт
     *
     * @param productDto
     * @return
     * @throws MyValidationException
     */
    @Override
    public Product createProduct(ProductDto productDto) throws MyValidationException {
        Product product = new Product(productDto.getName(), productDto.getDescription(), productDto.getCost(), productDto.getStockBalance(), productDto.getProductStatus());
        if (validationService.validateProduct(product)) {
            product.setProductStatus(ProductStatus.ACTIVE);
            /*product.getCharacteristic().stream()
                    .forEach(characteristicService::createCharacteristic);
            product.getKeyWords().stream()
                    .forEach(keyWordService::createKeyWord);*/
           /* Set<Product> products = product.getOrganization().getProducts();
            products.add(product);
            product.getOrganization().setProducts(products);
            organizationService.updateOrganization(product.getOrganization(), product.getOrganization().getId());*/
            return productRepository.save(product);
        } else {
            throw new MyValidationException("Product has invalid fields!");
        }
    }

    @Override
    public void deleteProductById(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.delete(productRepository.findById(id).get());
        } else {
            throw new NotFoundByIdException("Product not found!");
        }
    }

    @Override
    public Product updateProduct(Product product, Long id) {
        if (validationService.validateProduct(product)) {
            if (productRepository.existsById(id)) {
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
    public List<Product> findAllWithDiscount() {
        List<Product> products = productRepository.findAll().stream()
                .filter(x -> x.getDiscount() != null)
                .peek(x -> x.setCost(returnCostWithDiscount(x)))
                .peek(x -> x.setAvgGrade(showAvgGrade(x.getId())))
                .collect(Collectors.toList());
        return products;
    }

    @Override
    public List<Product> findAllWithAS() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .filter(x -> x.getOrganization() != null)
                .filter(x -> x.getOrganization().getOrganizationStatus().equals(OrganizationStatus.ACTIVE))
                .filter(x -> x.getProductStatus().equals(ProductStatus.ACTIVE))
                .peek(x -> x.setCost(returnCostWithDiscount(x)))
                .peek(x -> x.setAvgGrade(showAvgGrade(x.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public Double returnCostWithDiscount(Product product) {
        if (product.getDiscount() == null) {
            return product.getCost();
        }
        if (validationService.validateDiscount(product.getDiscount())) {
            product.setCost(product.getCost() * product.getDiscount().getDiscountInPercent() / 100);
            return product.getCost();
        } else {
            throw new MyValidationException("Discount has invalid fields!");
        }
    }

    @Override
    public Product findByName(String name) {
        if (productRepository.existsByName(name)) {
            return productRepository.findByName(name).get();
        } else {
            throw new DontExistsByNameException("Product not found!");
        }
    }

    @Override
    public double showAvgGrade(Long id) {
        if (productRepository.existsById(id)) {
            List<Grade> gradeList = productRepository.findById(id).get().getGrade();
            return gradeList.stream()
                    .mapToDouble(x -> x.getGrade())
                    .average()
                    .orElse(0);
        } else {
            throw new DontExistsByNameException("Product not found!");
        }
    }

    /**
     * для пользователя, заявка на создание продукта
     *
     * @param product
     * @return
     */
    @Override
    public Product applyToCreateProduct(Product product) {
        if (validationService.validateProduct(product)) {
            User user = userRepository.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if (user.getOrganizations().contains(product.getOrganization())) {
                return productRepository.save(product);
            } else {
                throw new DontExistsByNameException("You can add products only for your organization!");
            }
        } else {
            throw new MyValidationException("Product has invalid fields!");
        }
    }

    @Override
    public Product updateOwnProduct(Product product, Long id) {
        if (validationService.validateProduct(product)) {
            User user = userRepository.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if (user.getOrganizations().stream().anyMatch(x -> x.getProducts().contains(product))) {
                return updateProduct(product, id);
            } else {
                throw new DontExistsByNameException("You can update products only for your organization!");
            }
        } else {
            throw new MyValidationException("Product has invalid fields!");
        }
    }

    @Override
    public Product setFrozenStatus(String organizationName, Product product) {
        Organization organization = organizationService.findAll().stream()
                .filter(x -> x.getName().equals(organizationName))
                .findFirst().orElse(null);
        if (organization == null) {
            throw new DontExistsByNameException("Organization not found!");
        }
        if (organization.getOrganizationStatus().equals(OrganizationStatus.ACTIVE)) {
            if (organization.getProducts().contains(product)) {
                product.setProductStatus(ProductStatus.FROZEN);
                return updateProduct(product, product.getId());
            } else {
                throw new NotFoundByIdException("Product not found!");
            }
        } else {
            throw new RuntimeException("Check organization status!");
        }
    }

    @Override
    public Product setActiveStatus(String organizationName, Product product) {
        Organization organization = organizationService.findAll().stream()
                .filter(x -> x.getName().equals(organizationName))
                .findFirst().orElse(null);
        if (organization == null) {
            throw new DontExistsByNameException("Organization not found!");
        }
        if (organization.getProducts().contains(product)) {
            product.setProductStatus(ProductStatus.ACTIVE);
            return updateProduct(product, product.getId());
        } else {
            throw new NotFoundByIdException("Product not found!");
        }
    }

    @Override
    public Product findById(Long id) {
        if (productRepository.existsById(id)) {
            return productRepository.findById(id).get();
        } else {
            throw new NotFoundByIdException("Product not found!");
        }
    }

    @Override
    public Product setOrganization(Long productId, Long orgId){
        if (productRepository.existsById(productId)){
            Product product = productRepository.findById(productId).get();
            Organization organization = organizationService.findById(orgId);
            product.setOrganization(organization);
            Set<Product> products = organization.getProducts();
            products.add(product);
            organization.setProducts(products);
            updateProduct(product, productId);
            //organizationService.updateOrganization(organization, orgId);
            return product;
        } else {
            throw new NotFoundByIdException("Product not found!");
        }
    }
}
