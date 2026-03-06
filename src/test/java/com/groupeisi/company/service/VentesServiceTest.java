package com.groupeisi.company.service;

import com.groupeisi.company.dao.ProduitsRepository;
import com.groupeisi.company.dao.VentesRepository;
import com.groupeisi.company.dto.ProduitsDto;
import com.groupeisi.company.dto.VentesDto;
import com.groupeisi.company.entities.Produits;
import com.groupeisi.company.entities.Ventes;
import com.groupeisi.company.mapper.VentesMapper;
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
@DisplayName("Tests Unitaires - VentesService")
class VentesServiceTest {

    @Mock
    private VentesRepository ventesRepository;

    @Mock
    private ProduitsRepository produitsRepository;

    @Mock
    private VentesMapper ventesMapper;

    @InjectMocks
    private VentesService ventesService;

    private Produits produit;
    private ProduitsDto produitsDto;
    private Ventes vente;
    private VentesDto ventesDto;

    @BeforeEach
    void setUp() {
        produit = Produits.builder()
                .id(1L).ref("PROD-001").name("Ordinateur Portable").stock(50.0)
                .build();

        produitsDto = ProduitsDto.builder()
                .id(1L).ref("PROD-001").name("Ordinateur Portable").stock(50.0)
                .build();

        vente = Ventes.builder()
                .id(1L).dateP(new Date()).quantity(10.0).product(produit)
                .build();

        ventesDto = VentesDto.builder()
                .id(1L).dateP(new Date()).quantity(10.0).product(produitsDto)
                .build();
    }

    @Test
    @DisplayName("findAll - doit retourner la liste de toutes les ventes")
    void findAll_ShouldReturnAllVentes() {
        when(ventesRepository.findAll()).thenReturn(Arrays.asList(vente));
        when(ventesMapper.toDtoList(anyList())).thenReturn(Arrays.asList(ventesDto));

        List<VentesDto> result = ventesService.findAll();

        assertThat(result).hasSize(1);
        verify(ventesRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findById - doit retourner la vente correspondant à l'ID")
    void findById_ShouldReturnVente_WhenExists() {
        when(ventesRepository.findById(1L)).thenReturn(Optional.of(vente));
        when(ventesMapper.toDto(vente)).thenReturn(ventesDto);

        Optional<VentesDto> result = ventesService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("findById - doit retourner Optional.empty() si non trouvé")
    void findById_ShouldReturnEmpty_WhenNotFound() {
        when(ventesRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<VentesDto> result = ventesService.findById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("save - doit enregistrer la vente et décrémenter le stock")
    void save_ShouldCreateVente_AndDecreaseStock() {
        when(produitsRepository.findById(1L)).thenReturn(Optional.of(produit));
        when(ventesMapper.toEntity(ventesDto)).thenReturn(vente);
        when(ventesRepository.save(vente)).thenReturn(vente);
        when(ventesMapper.toDto(vente)).thenReturn(ventesDto);
        when(produitsRepository.save(produit)).thenReturn(produit);

        VentesDto result = ventesService.save(ventesDto);

        assertThat(result).isNotNull();
        // Le stock doit avoir diminué de 10.0
        assertThat(produit.getStock()).isEqualTo(40.0);
        verify(produitsRepository, times(1)).save(produit);
        verify(ventesRepository, times(1)).save(vente);
    }

    @Test
    @DisplayName("save - doit lever une exception si stock insuffisant")
    void save_ShouldThrowException_WhenStockInsuffisant() {
        ventesDto.setQuantity(100.0); // Demande plus que le stock disponible (50.0)

        when(produitsRepository.findById(1L)).thenReturn(Optional.of(produit));
        when(ventesMapper.toEntity(ventesDto)).thenReturn(vente);

        assertThatThrownBy(() -> ventesService.save(ventesDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Stock insuffisant");

        verify(ventesRepository, never()).save(any());
    }

    @Test
    @DisplayName("save - doit lever une exception si le produit n'existe pas")
    void save_ShouldThrowException_WhenProductNotFound() {
        when(produitsRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ventesService.save(ventesDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("1");

        verify(ventesRepository, never()).save(any());
    }

    @Test
    @DisplayName("deleteById - doit supprimer la vente si existante")
    void deleteById_ShouldDelete_WhenExists() {
        when(ventesRepository.existsById(1L)).thenReturn(true);
        doNothing().when(ventesRepository).deleteById(1L);

        assertThatCode(() -> ventesService.deleteById(1L)).doesNotThrowAnyException();

        verify(ventesRepository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteById - doit lever une exception si la vente n'existe pas")
    void deleteById_ShouldThrowException_WhenNotFound() {
        when(ventesRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> ventesService.deleteById(99L))
                .isInstanceOf(RuntimeException.class);

        verify(ventesRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("findByProductId - doit retourner les ventes d'un produit")
    void findByProductId_ShouldReturnVentes() {
        when(ventesRepository.findByProductId(1L)).thenReturn(Arrays.asList(vente));
        when(ventesMapper.toDtoList(anyList())).thenReturn(Arrays.asList(ventesDto));

        List<VentesDto> result = ventesService.findByProductId(1L);

        assertThat(result).hasSize(1);
        verify(ventesRepository).findByProductId(1L);
    }
}
