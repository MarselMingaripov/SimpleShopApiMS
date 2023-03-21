package ru.min.simleshopapims.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "discount")
@Data
@NoArgsConstructor
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Product> namesOfProducts;
    private int discountInPercent;
    private LocalDate startOfDiscount;
    private LocalDate endOfDiscount;

    public Discount(int discountInPercent, LocalDate startOfDiscount, LocalDate endOfDiscount) {
        this.namesOfProducts = new ArrayList<>();
        this.discountInPercent = discountInPercent;
        this.startOfDiscount = startOfDiscount;
        this.endOfDiscount = endOfDiscount;
    }
}
