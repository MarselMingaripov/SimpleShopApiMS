package ru.min.simleshopapims.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "organization", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
@Data
@NoArgsConstructor
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @NotNull
    private String name;
    @NotBlank
    @NotNull
    private String description;
    @NotBlank
    @NotNull
    private String refToLogo;
    @NotBlank
    @OneToMany(fetch = FetchType.LAZY)
    private Set<Product> products;

    private double profit;

    public Organization(String name, String description, String refToLogo, Set<Product> products) {
        this.name = name;
        this.description = description;
        this.refToLogo = refToLogo;
        this.products = products;
    }
}
