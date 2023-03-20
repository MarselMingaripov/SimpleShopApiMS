package ru.min.simleshopapims.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.min.simleshopapims.security.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "purchase")
@Data
@NoArgsConstructor
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate localDate;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "purchase_products",
            joinColumns = @JoinColumn(name = "purchase_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonManagedReference
    private User user;
    private double totalCost;
    @Enumerated(EnumType.STRING)
    private PurchaseStatus purchaseStatus;

    public Purchase(LocalDate localDate, List<Product> products, User user, double totalCost) {
        this.localDate = localDate;
        this.products = products;
        this.user = user;
        this.totalCost = totalCost;
    }
}
