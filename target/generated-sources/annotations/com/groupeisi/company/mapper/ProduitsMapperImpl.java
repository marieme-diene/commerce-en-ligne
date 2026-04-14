package com.groupeisi.company.mapper;

import com.groupeisi.company.dto.ProduitsDto;
import com.groupeisi.company.entities.Produits;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-14T01:22:47+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class ProduitsMapperImpl implements ProduitsMapper {

    @Override
    public ProduitsDto toDto(Produits produits) {
        if ( produits == null ) {
            return null;
        }

        ProduitsDto.ProduitsDtoBuilder produitsDto = ProduitsDto.builder();

        produitsDto.id( produits.getId() );
        produitsDto.ref( produits.getRef() );
        produitsDto.name( produits.getName() );
        produitsDto.stock( produits.getStock() );

        return produitsDto.build();
    }

    @Override
    public Produits toEntity(ProduitsDto produitsDto) {
        if ( produitsDto == null ) {
            return null;
        }

        Produits.ProduitsBuilder produits = Produits.builder();

        produits.id( produitsDto.getId() );
        produits.ref( produitsDto.getRef() );
        produits.name( produitsDto.getName() );
        produits.stock( produitsDto.getStock() );

        return produits.build();
    }

    @Override
    public List<ProduitsDto> toDtoList(List<Produits> produitsList) {
        if ( produitsList == null ) {
            return null;
        }

        List<ProduitsDto> list = new ArrayList<ProduitsDto>( produitsList.size() );
        for ( Produits produits : produitsList ) {
            list.add( toDto( produits ) );
        }

        return list;
    }

    @Override
    public List<Produits> toEntityList(List<ProduitsDto> produitsDtoList) {
        if ( produitsDtoList == null ) {
            return null;
        }

        List<Produits> list = new ArrayList<Produits>( produitsDtoList.size() );
        for ( ProduitsDto produitsDto : produitsDtoList ) {
            list.add( toEntity( produitsDto ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromDto(ProduitsDto dto, Produits entity) {
        if ( dto == null ) {
            return;
        }

        entity.setId( dto.getId() );
        entity.setRef( dto.getRef() );
        entity.setName( dto.getName() );
        entity.setStock( dto.getStock() );
    }
}
