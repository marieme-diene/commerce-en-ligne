package com.groupeisi.company.service;

import com.groupeisi.company.dao.AchatsRepository;
import com.groupeisi.company.dao.ProduitsRepository;
import com.groupeisi.company.dto.AchatsDto;
import com.groupeisi.company.dto.ProduitsDto;
import com.groupeisi.company.entities.Achats;
import com.groupeisi.company.entities.Produits;
import com.groupeisi.company.mapper.AchatsMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests Unitaires - AchatsService")
class AchatsServiceTest {

    @Mock
    private AchatsRepository achatsRepository;

    @Mock
    private ProduitsRepository produitsRepository;

    @Mock
    private AchatsMapper achatsMapper;

    @InjectMocks
    private AchatsService achatsService;

    private Produits produit;
    private ProduitsDto produitsDto;
    private Achats achat;
    private AchatsDto achatsDto;

    @BeforeEach
    void setUp() {
        produit = Produits.builder()
                .id(1L).ref("PROD-001").name("Ordinateur Portable").stock(50.0)
                .build();

        produitsDto = ProduitsDto.builder()
                .id(1L).ref("PROD-001").name("Ordinateur Portable").stock(50.0)
                .build();

        achat = Achats.builder()
                .id(1L).dateP(new Date()).quantity(10.0).product(produit)
                .build();

        achatsDto = AchatsDto.builder()
                .id(1L).dateP(new Date()).quantity(10.0).product(produitsDto)
                .build();
    }

    @Test
    @DisplayName("findAll - doit retourner la liste de tous les achats")
    void findAll_ShouldReturnAllAchats() {
        when(achatsRepository.findAll()).thenReturn(Arrays.asList(achat));
        when(achatsMapper.toDtoList(anyList())).thenReturn(Arrays.asList(achatsDto));

        List<AchatsDto> result = achatsService.findAll();

        assertThat(result).hasSize(1);
        verify(achatsRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findById - doit retourner l'achat correspondant à l'ID")
    void findById_ShouldReturnAchat_WhenExists() {
        when(achatsRepository.findById(1L)).thenReturn(Optional.of(achat));
        when(achatsMapper.toDto(achat)).thenReturn(achatsDto);

        Optional<AchatsDto> result = achatsService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("findById - doit retourner Optional.empty() si non trouvé")
    void findById_ShouldReturnEmpty_WhenNotFound() {
        when(achatsRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<AchatsDto> result = achatsService.findById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("save - doit enregistrer l'achat et augmenter le stock")
    void save_ShouldCreateAchat_AndIncreaseStock() {
        when(produitsRepository.findById(1L)).thenReturn(Optional.of(produit));
        when(achatsMapper.toEntity(achatsDto)).thenReturn(achat);
        when(achatsRepository.save(achat)).thenReturn(achat);
        when(achatsMapper.toDto(achat)).thenReturn(achatsDto);
        when(produitsRepository.save(produit)).thenReturn(produit);

        AchatsDto result = achatsService.save(achatsDto);

        assertThat(result).isNotNull();
        // Le stock doit avoir augmenté de 10.0
        assertThat(produit.getStock()).isEqualTo(60.0);
        verify(produitsRepository, times(1)).save(produit);
        verify(achatsRepository, times(1)).save(achat);
    }

    @Test
    @DisplayName("save - doit lever une exception si le produit n'existe pas")
    void save_ShouldThrowException_WhenProductNotFound() {
        when(produitsRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> achatsService.save(achatsDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("1");

        verify(achatsRepository, never()).save(any());
    }

    @Test
    @DisplayName("deleteById - doit supprimer l'achat si existant")
    void deleteById_ShouldDelete_WhenExists() {
        when(achatsRepository.existsById(1L)).thenReturn(true);
        doNothing().when(achatsRepository).deleteById(1L);

        assertThatCode(() -> achatsService.deleteById(1L)).doesNotThrowAnyException();

        verify(achatsRepository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteById - doit lever une exception si l'achat n'existe pas")
    void deleteById_ShouldThrowException_WhenNotFound() {
        when(achatsRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> achatsService.deleteById(99L))
                .isInstanceOf(RuntimeException.class);

        verify(achatsRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("findByProductId - doit retourner les achats d'un produit")
    void findByProductId_ShouldReturnAchats() {
        when(achatsRepository.findByProductId(1L)).thenReturn(Arrays.asList(achat));
        when(achatsMapper.toDtoList(anyList())).thenReturn(Arrays.asList(achatsDto));

        List<AchatsDto> result = achatsService.findByProductId(1L);

        assertThat(result).hasSize(1);
        verify(achatsRepository).findByProductId(1L);
    }
}
