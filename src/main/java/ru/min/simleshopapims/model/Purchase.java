package ru.min.simleshopapims.model;

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
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "purchase_products",
            joinColumns = @JoinColumn(name = "purchase_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    private double totalCost;
    private PurchaseStatus purchaseStatus;

    public Purchase(LocalDate localDate, List<Product> products, User user, double totalCost) {
        this.localDate = localDate;
        this.products = products;
        this.user = user;
        this.totalCost = totalCost;
    }
}
