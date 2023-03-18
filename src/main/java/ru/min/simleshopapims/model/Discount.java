package ru.min.simleshopapims.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "discount")
@Data
@NoArgsConstructor
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @NotNull
    @OneToMany(fetch = FetchType.LAZY)
    private List<Product> namesOfProducts;
    @NotNull
    @NotBlank
    private int discountInPercent;
    @NotNull
    @NotBlank
    private LocalDate startOfDiscount;
    @NotNull
    @NotBlank
    private LocalDate endOfDiscount;

    public Discount(List<Product> namesOfProducts, int discountInPercent, LocalDate startOfDiscount, LocalDate endOfDiscount) {
        this.namesOfProducts = namesOfProducts;
        this.discountInPercent = discountInPercent;
        this.startOfDiscount = startOfDiscount;
        this.endOfDiscount = endOfDiscount;
    }
}
