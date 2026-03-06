package com.groupeisi.company.mapper;

import com.groupeisi.company.dto.AchatsDto;
import com.groupeisi.company.entities.Achats;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProduitsMapper.class})
public interface AchatsMapper {

    AchatsDto toDto(Achats achats);

    Achats toEntity(AchatsDto achatsDto);

    List<AchatsDto> toDtoList(List<Achats> achatsList);

    List<Achats> toEntityList(List<AchatsDto> achatsDtoList);

    void updateEntityFromDto(AchatsDto dto, @MappingTarget Achats entity);
}
