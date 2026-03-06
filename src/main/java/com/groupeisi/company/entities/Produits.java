package com.groupeisi.company.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Entity
@Table(name = "produits")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La référence est obligatoire")
    @Column(unique = true, nullable = false, length = 50)
    private String ref;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(nullable = false, length = 100)
    private String name;

    @NotNull(message = "Le stock est obligatoire")
    @PositiveOrZero(message = "Le stock ne peut pas être négatif")
    @Column(nullable = false)
    private Double stock;
}
