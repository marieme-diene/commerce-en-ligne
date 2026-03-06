package com.groupeisi.company.service;

import com.groupeisi.company.dao.ProduitsRepository;
import com.groupeisi.company.dto.ProduitsDto;
import com.groupeisi.company.entities.Produits;
import com.groupeisi.company.mapper.ProduitsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProduitsService {

    private final ProduitsRepository produitsRepository;
    private final ProduitsMapper produitsMapper;

    /**
     * Récupère tous les produits
     */
    @Transactional(readOnly = true)
    public List<ProduitsDto> findAll() {
        log.info("Récupération de tous les produits");
        List<Produits> produits = produitsRepository.findAll();
        log.info("{} produit(s) trouvé(s)", produits.size());
        return produitsMapper.toDtoList(produits);
    }

    /**
     * Récupère un produit par son ID
     */
    @Transactional(readOnly = true)
    public Optional<ProduitsDto> findById(Long id) {
        log.info("Recherche du produit avec l'ID : {}", id);
        return produitsRepository.findById(id)
                .map(produits -> {
                    log.info("Produit trouvé : {}", produits.getName());
                    return produitsMapper.toDto(produits);
                });
    }

    /**
     * Récupère un produit par sa référence
     */
    @Transactional(readOnly = true)
    public Optional<ProduitsDto> findByRef(String ref) {
        log.info("Recherche du produit avec la référence : {}", ref);
        return produitsRepository.findByRef(ref)
                .map(produitsMapper::toDto);
    }

    /**
     * Crée un nouveau produit
     */
    public ProduitsDto save(ProduitsDto produitsDto) {
        log.info("Création d'un nouveau produit : {}", produitsDto.getName());
        if (produitsRepository.existsByRef(produitsDto.getRef())) {
            log.error("Un produit avec la référence '{}' existe déjà", produitsDto.getRef());
            throw new IllegalArgumentException("Un produit avec la référence '" + produitsDto.getRef() + "' existe déjà");
        }
        Produits produits = produitsMapper.toEntity(produitsDto);
        Produits saved = produitsRepository.save(produits);
        log.info("Produit créé avec succès, ID : {}", saved.getId());
        return produitsMapper.toDto(saved);
    }

    /**
     * Met à jour un produit existant
     */
    public ProduitsDto update(Long id, ProduitsDto produitsDto) {
        log.info("Mise à jour du produit avec l'ID : {}", id);
        Produits existing = produitsRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Produit non trouvé avec l'ID : {}", id);
                    return new RuntimeException("Produit non trouvé avec l'ID : " + id);
                });
        produitsMapper.updateEntityFromDto(produitsDto, existing);
        Produits updated = produitsRepository.save(existing);
        log.info("Produit mis à jour avec succès : {}", updated.getName());
        return produitsMapper.toDto(updated);
    }

    /**
     * Supprime un produit par son ID
     */
    public void deleteById(Long id) {
        log.info("Suppression du produit avec l'ID : {}", id);
        if (!produitsRepository.existsById(id)) {
            log.error("Impossible de supprimer : produit non trouvé avec l'ID : {}", id);
            throw new RuntimeException("Produit non trouvé avec l'ID : " + id);
        }
        produitsRepository.deleteById(id);
        log.info("Produit supprimé avec succès, ID : {}", id);
    }

    /**
     * Met à jour le stock d'un produit
     */
    public ProduitsDto updateStock(Long id, Double newStock) {
        log.info("Mise à jour du stock du produit ID : {} -> stock : {}", id, newStock);
        Produits produits = produitsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'ID : " + id));
        produits.setStock(newStock);
        Produits updated = produitsRepository.save(produits);
        log.info("Stock mis à jour avec succès pour le produit : {}", updated.getName());
        return produitsMapper.toDto(updated);
    }
}
