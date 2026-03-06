package com.groupeisi.company.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "achats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Achats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La date d'achat est obligatoire")
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date dateP;

    @NotNull(message = "La quantité est obligatoire")
    @Positive(message = "La quantité doit être positive")
    @Column(nullable = false)
    private Double quantity;

    @NotNull(message = "Le produit est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_id", nullable = false)
    private Produits product;
}
