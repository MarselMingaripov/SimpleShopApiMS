package ru.min.simleshopapims.security.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Erole name;

    public Role() {
    }

    public Role(Erole name) {

        this.name = name;
    }
}
