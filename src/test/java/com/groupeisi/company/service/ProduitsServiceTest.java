package com.groupeisi.company.service;

import com.groupeisi.company.dao.ProduitsRepository;
import com.groupeisi.company.dto.ProduitsDto;
import com.groupeisi.company.entities.Produits;
import com.groupeisi.company.mapper.ProduitsMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests Unitaires - ProduitsService")
class ProduitsServiceTest {

    @Mock
    private ProduitsRepository produitsRepository;

    @Mock
    private ProduitsMapper produitsMapper;

    @InjectMocks
    private ProduitsService produitsService;

    private Produits produit;
    private ProduitsDto produitsDto;

    @BeforeEach
    void setUp() {
        produit = Produits.builder()
                .id(1L)
                .ref("PROD-001")
                .name("Ordinateur Portable")
                .stock(50.0)
                .build();

        produitsDto = ProduitsDto.builder()
                .id(1L)
                .ref("PROD-001")
                .name("Ordinateur Portable")
                .stock(50.0)
                .build();
    }

    // ========== findAll ==========

    @Test
    @DisplayName("findAll - doit retourner la liste de tous les produits")
    void findAll_ShouldReturnAllProduits() {
        List<Produits> produitsList = Arrays.asList(produit);
        List<ProduitsDto> dtoList = Arrays.asList(produitsDto);

        when(produitsRepository.findAll()).thenReturn(produitsList);
        when(produitsMapper.toDtoList(produitsList)).thenReturn(dtoList);

        List<ProduitsDto> result = produitsService.findAll();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Ordinateur Portable");
        verify(produitsRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findAll - doit retourner une liste vide si aucun produit")
    void findAll_ShouldReturnEmptyList_WhenNoProduits() {
        when(produitsRepository.findAll()).thenReturn(List.of());
        when(produitsMapper.toDtoList(anyList())).thenReturn(List.of());

        List<ProduitsDto> result = produitsService.findAll();

        assertThat(result).isEmpty();
    }

    // ========== findById ==========

    @Test
    @DisplayName("findById - doit retourner le produit correspondant à l'ID")
    void findById_ShouldReturnProduit_WhenExists() {
        when(produitsRepository.findById(1L)).thenReturn(Optional.of(produit));
        when(produitsMapper.toDto(produit)).thenReturn(produitsDto);

        Optional<ProduitsDto> result = produitsService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getRef()).isEqualTo("PROD-001");
    }

    @Test
    @DisplayName("findById - doit retourner Optional.empty() si l'ID n'existe pas")
    void findById_ShouldReturnEmpty_WhenNotFound() {
        when(produitsRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<ProduitsDto> result = produitsService.findById(999L);

        assertThat(result).isEmpty();
    }

    // ========== findByRef ==========

    @Test
    @DisplayName("findByRef - doit retourner le produit par référence")
    void findByRef_ShouldReturnProduit_WhenRefExists() {
        when(produitsRepository.findByRef("PROD-001")).thenReturn(Optional.of(produit));
        when(produitsMapper.toDto(produit)).thenReturn(produitsDto);

        Optional<ProduitsDto> result = produitsService.findByRef("PROD-001");

        assertThat(result).isPresent();
        assertThat(result.get().getRef()).isEqualTo("PROD-001");
    }

    // ========== save ==========

    @Test
    @DisplayName("save - doit créer un produit avec succès")
    void save_ShouldCreateProduit_Successfully() {
        when(produitsRepository.existsByRef("PROD-001")).thenReturn(false);
        when(produitsMapper.toEntity(produitsDto)).thenReturn(produit);
        when(produitsRepository.save(produit)).thenReturn(produit);
        when(produitsMapper.toDto(produit)).thenReturn(produitsDto);

        ProduitsDto result = produitsService.save(produitsDto);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Ordinateur Portable");
        verify(produitsRepository, times(1)).save(any(Produits.class));
    }

    @Test
    @DisplayName("save - doit lever une exception si la référence existe déjà")
    void save_ShouldThrowException_WhenRefAlreadyExists() {
        when(produitsRepository.existsByRef("PROD-001")).thenReturn(true);

        assertThatThrownBy(() -> produitsService.save(produitsDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("PROD-001");

        verify(produitsRepository, never()).save(any());
    }

    // ========== update ==========

    @Test
    @DisplayName("update - doit mettre à jour un produit existant")
    void update_ShouldUpdateProduit_WhenExists() {
        ProduitsDto updatedDto = ProduitsDto.builder()
                .id(1L).ref("PROD-001").name("Laptop Pro").stock(30.0).build();

        when(produitsRepository.findById(1L)).thenReturn(Optional.of(produit));
        doNothing().when(produitsMapper).updateEntityFromDto(updatedDto, produit);
        when(produitsRepository.save(produit)).thenReturn(produit);
        when(produitsMapper.toDto(produit)).thenReturn(updatedDto);

        ProduitsDto result = produitsService.update(1L, updatedDto);

        assertThat(result).isNotNull();
        verify(produitsRepository, times(1)).save(produit);
    }

    @Test
    @DisplayName("update - doit lever une exception si le produit n'existe pas")
    void update_ShouldThrowException_WhenNotFound() {
        when(produitsRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> produitsService.update(999L, produitsDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("999");
    }

    // ========== deleteById ==========

    @Test
    @DisplayName("deleteById - doit supprimer un produit existant")
    void deleteById_ShouldDeleteProduit_WhenExists() {
        when(produitsRepository.existsById(1L)).thenReturn(true);
        doNothing().when(produitsRepository).deleteById(1L);

        assertThatCode(() -> produitsService.deleteById(1L)).doesNotThrowAnyException();

        verify(produitsRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deleteById - doit lever une exception si le produit n'existe pas")
    void deleteById_ShouldThrowException_WhenNotFound() {
        when(produitsRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> produitsService.deleteById(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("999");

        verify(produitsRepository, never()).deleteById(any());
    }

    // ========== updateStock ==========

    @Test
    @DisplayName("updateStock - doit mettre à jour le stock d'un produit")
    void updateStock_ShouldUpdateStock_Successfully() {
        ProduitsDto updatedDto = ProduitsDto.builder()
                .id(1L).ref("PROD-001").name("Ordinateur Portable").stock(100.0).build();

        when(produitsRepository.findById(1L)).thenReturn(Optional.of(produit));
        when(produitsRepository.save(produit)).thenReturn(produit);
        when(produitsMapper.toDto(produit)).thenReturn(updatedDto);

        ProduitsDto result = produitsService.updateStock(1L, 100.0);

        assertThat(result).isNotNull();
        verify(produitsRepository, times(1)).save(produit);
    }
}
