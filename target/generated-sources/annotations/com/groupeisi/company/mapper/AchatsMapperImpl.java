package com.groupeisi.company.mapper;

import com.groupeisi.company.dto.AchatsDto;
import com.groupeisi.company.entities.Achats;
import com.groupeisi.company.entities.Produits;
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
public class AchatsMapperImpl implements AchatsMapper {

    @Autowired
    private ProduitsMapper produitsMapper;

    @Override
    public AchatsDto toDto(Achats achats) {
        if ( achats == null ) {
            return null;
        }

        AchatsDto.AchatsDtoBuilder achatsDto = AchatsDto.builder();

        achatsDto.id( achats.getId() );
        achatsDto.dateP( achats.getDateP() );
        achatsDto.quantity( achats.getQuantity() );
        achatsDto.product( produitsMapper.toDto( achats.getProduct() ) );

        return achatsDto.build();
    }

    @Override
    public Achats toEntity(AchatsDto achatsDto) {
        if ( achatsDto == null ) {
            return null;
        }

        Achats.AchatsBuilder achats = Achats.builder();

        achats.id( achatsDto.getId() );
        achats.dateP( achatsDto.getDateP() );
        achats.quantity( achatsDto.getQuantity() );
        achats.product( produitsMapper.toEntity( achatsDto.getProduct() ) );

        return achats.build();
    }

    @Override
    public List<AchatsDto> toDtoList(List<Achats> achatsList) {
        if ( achatsList == null ) {
            return null;
        }

        List<AchatsDto> list = new ArrayList<AchatsDto>( achatsList.size() );
        for ( Achats achats : achatsList ) {
            list.add( toDto( achats ) );
        }

        return list;
    }

    @Override
    public List<Achats> toEntityList(List<AchatsDto> achatsDtoList) {
        if ( achatsDtoList == null ) {
            return null;
        }

        List<Achats> list = new ArrayList<Achats>( achatsDtoList.size() );
        for ( AchatsDto achatsDto : achatsDtoList ) {
            list.add( toEntity( achatsDto ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromDto(AchatsDto dto, Achats entity) {
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
