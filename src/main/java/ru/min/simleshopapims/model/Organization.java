package ru.min.simleshopapims.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "organization")
@Data
@NoArgsConstructor
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String refToLogo;
    @OneToMany(fetch = FetchType.EAGER)
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
