package ru.min.simleshopapims.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String description;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "product_organization",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "organization_id"))
    private Organization organization;
    @NotNull
    private double cost;
    private int stockBalance;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "product_discount",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "discount_id"))
    private Discount discount;
    @NotNull
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_reviews",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "review_id"))
    private Set<Review> reviews;
    @NotNull
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_keyWords",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "keyWord_id"))
    private Set<KeyWord> keyWords;
    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "product_characteristics",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "characteristic_id"))
    private Characteristic characteristic;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_grades",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "grade_id"))
    private List<Grade> grade;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Purchase> purchases;

    public Product(String name, String description, Organization organization, double cost, int stockBalance,
                   Discount discount, Set<Review> reviews, Set<KeyWord> keyWords, Characteristic characteristic, List<Grade> grade) {
        this.name = name;
        this.description = description;
        this.organization = organization;
        this.cost = cost;
        this.stockBalance = stockBalance;
        this.discount = discount;
        this.reviews = reviews;
        this.keyWords = keyWords;
        this.characteristic = characteristic;
        this.grade = grade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(product.cost, cost) == 0 && Objects.equals(name, product.name) && Objects.equals(description, product.description) && Objects.equals(organization, product.organization) && Objects.equals(discount, product.discount) && Objects.equals(keyWords, product.keyWords) && Objects.equals(characteristic, product.characteristic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, organization, cost, discount, keyWords, characteristic);
    }
}
