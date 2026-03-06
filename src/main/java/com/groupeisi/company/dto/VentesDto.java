package com.groupeisi.company.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentesDto {

    private Long id;

    @NotNull(message = "La date de vente est obligatoire")
    private Date dateP;

    @NotNull(message = "La quantité est obligatoire")
    @Positive(message = "La quantité doit être positive")
    private Double quantity;

    @NotNull(message = "Le produit est obligatoire")
    private ProduitsDto product;
}
