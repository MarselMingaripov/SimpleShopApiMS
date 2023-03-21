package ru.min.simleshopapims.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.min.simleshopapims.model.enums.ProductStatus;

@Data
@AllArgsConstructor
public class ProductDto {

    private String name;
    private String description;
    private double cost;
    private int stockBalance;
    private ProductStatus productStatus;
}
