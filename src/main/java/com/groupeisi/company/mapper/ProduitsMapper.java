package com.groupeisi.company.mapper;

import com.groupeisi.company.dto.ProduitsDto;
import com.groupeisi.company.entities.Produits;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProduitsMapper {

    ProduitsDto toDto(Produits produits);

    Produits toEntity(ProduitsDto produitsDto);

    List<ProduitsDto> toDtoList(List<Produits> produitsList);

    List<Produits> toEntityList(List<ProduitsDto> produitsDtoList);

    void updateEntityFromDto(ProduitsDto dto, @MappingTarget Produits entity);
}
