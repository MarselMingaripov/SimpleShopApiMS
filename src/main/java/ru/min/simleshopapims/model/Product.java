package ru.min.simleshopapims.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.min.simleshopapims.model.enums.ProductStatus;

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
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinTable(name = "product_organization",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "organization_id"))
    @JsonManagedReference
    private Organization organization;
    @NotNull
    private double cost;
    private int stockBalance;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "product_discount",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "discount_id"))
    @JsonBackReference
    private Discount discount;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "product_reviews",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "review_id"))
    private Set<Review> reviews;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "product_keyWords",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "keyWord_id"))
    //@Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    private Set<KeyWord> keyWords;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "product_characteristics",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "characteristic_id"))
    //@Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    private Set<Characteristic> characteristic;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_grades",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "grade_id"))
    private List<Grade> grade;

    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    private double avgGrade;

    public Product(String name, String description, Organization organization, double cost, int stockBalance,
                   Set<KeyWord> keyWords, Set<Characteristic> characteristic) {
        this.name = name;
        this.description = description;
        this.organization = organization;
        this.cost = cost;
        this.stockBalance = stockBalance;
        this.discount = null;
        this.keyWords = keyWords;
        this.characteristic = characteristic;
        this.productStatus = ProductStatus.PENDING;
        this.avgGrade = 0;
    }

    public Product(String name, String description, double cost, int stockBalance, ProductStatus productStatus) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.stockBalance = stockBalance;
        this.productStatus = productStatus;
        this.organization = null;
        this.discount = null;
        this.reviews = null;
        this.keyWords = null;
        this.characteristic = null;
        this.grade = null;
        this.avgGrade = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(product.cost, cost) == 0 && stockBalance == product.stockBalance && Objects.equals(name, product.name) && Objects.equals(description, product.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, cost, stockBalance);
    }
}
