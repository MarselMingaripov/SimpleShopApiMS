package ru.min.simleshopapims.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DiscountDto {

    private int discountInPercent;
    private LocalDate startOfDiscount;
    private LocalDate endOfDiscount;
}
