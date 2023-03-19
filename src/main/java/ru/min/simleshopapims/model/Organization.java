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

    private String owner;

    private double profit;
    private OrganizationStatus organizationStatus;

    public Organization(String name, String description, String refToLogo, String owner) {
        this.name = name;
        this.description = description;
        this.refToLogo = refToLogo;
        this.owner = owner;
        this.products = null;
        this.organizationStatus = OrganizationStatus.PENDING;
    }
}
