package ru.min.simleshopapims.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.min.simleshopapims.exception.DontExistsByNameException;
import ru.min.simleshopapims.exception.MyValidationException;
import ru.min.simleshopapims.exception.NotFoundByIdException;
import ru.min.simleshopapims.model.Characteristic;
import ru.min.simleshopapims.model.Discount;
import ru.min.simleshopapims.repository.DiscountRepository;
import ru.min.simleshopapims.service.DiscountService;
import ru.min.simleshopapims.service.ValidationService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;
    private final ValidationService validationService;

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
}
