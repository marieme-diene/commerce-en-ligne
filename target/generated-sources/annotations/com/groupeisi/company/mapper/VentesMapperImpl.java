package com.groupeisi.company.mapper;

import com.groupeisi.company.dto.VentesDto;
import com.groupeisi.company.entities.Produits;
import com.groupeisi.company.entities.Ventes;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-14T01:22:48+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class VentesMapperImpl implements VentesMapper {

    @Autowired
    private ProduitsMapper produitsMapper;

    @Override
    public VentesDto toDto(Ventes ventes) {
        if ( ventes == null ) {
            return null;
        }

        VentesDto.VentesDtoBuilder ventesDto = VentesDto.builder();

        ventesDto.id( ventes.getId() );
        ventesDto.dateP( ventes.getDateP() );
        ventesDto.quantity( ventes.getQuantity() );
        ventesDto.product( produitsMapper.toDto( ventes.getProduct() ) );

        return ventesDto.build();
    }

    @Override
    public Ventes toEntity(VentesDto ventesDto) {
        if ( ventesDto == null ) {
            return null;
        }

        Ventes.VentesBuilder ventes = Ventes.builder();

        ventes.id( ventesDto.getId() );
        ventes.dateP( ventesDto.getDateP() );
        ventes.quantity( ventesDto.getQuantity() );
        ventes.product( produitsMapper.toEntity( ventesDto.getProduct() ) );

        return ventes.build();
    }

    @Override
    public List<VentesDto> toDtoList(List<Ventes> ventesList) {
        if ( ventesList == null ) {
            return null;
        }

        List<VentesDto> list = new ArrayList<VentesDto>( ventesList.size() );
        for ( Ventes ventes : ventesList ) {
            list.add( toDto( ventes ) );
        }

        return list;
    }

    @Override
    public List<Ventes> toEntityList(List<VentesDto> ventesDtoList) {
        if ( ventesDtoList == null ) {
            return null;
        }

        List<Ventes> list = new ArrayList<Ventes>( ventesDtoList.size() );
        for ( VentesDto ventesDto : ventesDtoList ) {
            list.add( toEntity( ventesDto ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromDto(VentesDto dto, Ventes entity) {
        if ( dto == null ) {
            return;
        }

        entity.setId( dto.getId() );
        entity.setDateP( dto.getDateP() );
        entity.setQuantity( dto.getQuantity() );
        if ( dto.getProduct() != null ) {
            if ( entity.getProduct() == null ) {
                entity.setProduct( Produits.builder().build() );
            }
            produitsMapper.updateEntityFromDto( dto.getProduct(), entity.getProduct() );
        }
        else {
            entity.setProduct( null );
        }
    }
}
