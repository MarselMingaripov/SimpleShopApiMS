package ru.min.simleshopapims.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "review")
@Data
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @NotBlank
    @NotNull
    private String description;
    @NotNull
    @NotBlank
    private String author;

    public Review(String name, String description, String author) {
        this.name = name;
        this.description = description;
        this.author = author;
    }
}
