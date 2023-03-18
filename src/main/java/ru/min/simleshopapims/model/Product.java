package ru.min.simleshopapims.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "product", uniqueConstraints = {
@UniqueConstraint(columnNames = "name")
})
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
}
