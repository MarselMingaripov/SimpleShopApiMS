package ru.min.simleshopapims.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;
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
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<Product> products;

    private String owner;

    private double profit;
    @Enumerated(EnumType.STRING)
    private OrganizationStatus organizationStatus;

    public Organization(String name, String description, String refToLogo, String owner) {
        this.name = name;
        this.description = description;
        this.refToLogo = refToLogo;
        this.owner = owner;
        this.products = null;
        this.organizationStatus = OrganizationStatus.PENDING;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(refToLogo, that.refToLogo) && Objects.equals(owner, that.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, refToLogo, owner);
    }
}
