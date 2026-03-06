package com.groupeisi.company.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProduitsDto {

    private Long id;

    @NotBlank(message = "La référence est obligatoire")
    private String ref;

    @NotBlank(message = "Le nom est obligatoire")
    private String name;

    @NotNull(message = "Le stock est obligatoire")
    @PositiveOrZero(message = "Le stock ne peut pas être négatif")
    private Double stock;
}
