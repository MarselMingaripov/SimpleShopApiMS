package ru.min.simleshopapims.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.min.simleshopapims.model.ProductStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ProductDto {

    private String name;
    private String description;
    private double cost;
    private int stockBalance;
    private ProductStatus productStatus;
}
