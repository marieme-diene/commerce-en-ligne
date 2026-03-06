package com.groupeisi.company.mapper;

import com.groupeisi.company.dto.VentesDto;
import com.groupeisi.company.entities.Ventes;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProduitsMapper.class})
public interface VentesMapper {

    VentesDto toDto(Ventes ventes);

    Ventes toEntity(VentesDto ventesDto);

    List<VentesDto> toDtoList(List<Ventes> ventesList);

    List<Ventes> toEntityList(List<VentesDto> ventesDtoList);

    void updateEntityFromDto(VentesDto dto, @MappingTarget Ventes entity);
}
