package com.groupeisi.company.service;

import com.groupeisi.company.dao.AchatsRepository;
import com.groupeisi.company.dao.ProduitsRepository;
import com.groupeisi.company.dto.AchatsDto;
import com.groupeisi.company.entities.Achats;
import com.groupeisi.company.entities.Produits;
import com.groupeisi.company.mapper.AchatsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AchatsService {

    private final AchatsRepository achatsRepository;
    private final ProduitsRepository produitsRepository;
    private final AchatsMapper achatsMapper;

    /**
     * Récupère tous les achats
     */
    @Transactional(readOnly = true)
    public List<AchatsDto> findAll() {
        log.info("Récupération de tous les achats");
        List<Achats> achatsList = achatsRepository.findAll();
        log.info("{} achat(s) trouvé(s)", achatsList.size());
        return achatsMapper.toDtoList(achatsList);
    }

    /**
     * Récupère un achat par son ID
     */
    @Transactional(readOnly = true)
    public Optional<AchatsDto> findById(Long id) {
        log.info("Recherche de l'achat avec l'ID : {}", id);
        return achatsRepository.findById(id)
                .map(achats -> {
                    log.info("Achat trouvé, ID : {}", achats.getId());
                    return achatsMapper.toDto(achats);
                });
    }

    /**
     * Récupère les achats par produit
     */
    @Transactional(readOnly = true)
    public List<AchatsDto> findByProductId(Long productId) {
        log.info("Recherche des achats pour le produit ID : {}", productId);
        List<Achats> achatsList = achatsRepository.findByProductId(productId);
        log.info("{} achat(s) trouvé(s) pour le produit ID : {}", achatsList.size(), productId);
        return achatsMapper.toDtoList(achatsList);
    }

    /**
     * Récupère les achats entre deux dates
     */
    @Transactional(readOnly = true)
    public List<AchatsDto> findByDateBetween(Date debut, Date fin) {
        log.info("Recherche des achats entre {} et {}", debut, fin);
        return achatsMapper.toDtoList(achatsRepository.findByDatePBetween(debut, fin));
    }

    /**
     * Enregistre un achat et met à jour le stock du produit
     */
    public AchatsDto save(AchatsDto achatsDto) {
        log.info("Création d'un nouvel achat pour le produit ID : {}", achatsDto.getProduct().getId());

        Produits produit = produitsRepository.findById(achatsDto.getProduct().getId())
                .orElseThrow(() -> {
                    log.error("Produit non trouvé avec l'ID : {}", achatsDto.getProduct().getId());
                    return new RuntimeException("Produit non trouvé avec l'ID : " + achatsDto.getProduct().getId());
                });

        Achats achats = achatsMapper.toEntity(achatsDto);
        achats.setProduct(produit);

        // Mise à jour du stock : achat = augmentation du stock
        double newStock = produit.getStock() + achatsDto.getQuantity();
        produit.setStock(newStock);
        produitsRepository.save(produit);
        log.info("Stock mis à jour pour le produit '{}' : nouveau stock = {}", produit.getName(), newStock);

        Achats saved = achatsRepository.save(achats);
        log.info("Achat enregistré avec succès, ID : {}", saved.getId());
        return achatsMapper.toDto(saved);
    }

    /**
     * Met à jour un achat
     */
    public AchatsDto update(Long id, AchatsDto achatsDto) {
        log.info("Mise à jour de l'achat avec l'ID : {}", id);
        Achats existing = achatsRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Achat non trouvé avec l'ID : {}", id);
                    return new RuntimeException("Achat non trouvé avec l'ID : " + id);
                });
        achatsMapper.updateEntityFromDto(achatsDto, existing);
        Achats updated = achatsRepository.save(existing);
        log.info("Achat mis à jour avec succès, ID : {}", updated.getId());
        return achatsMapper.toDto(updated);
    }

    /**
     * Supprime un achat
     */
    public void deleteById(Long id) {
        log.info("Suppression de l'achat avec l'ID : {}", id);
        if (!achatsRepository.existsById(id)) {
            log.error("Impossible de supprimer : achat non trouvé avec l'ID : {}", id);
            throw new RuntimeException("Achat non trouvé avec l'ID : " + id);
        }
        achatsRepository.deleteById(id);
        log.info("Achat supprimé avec succès, ID : {}", id);
    }
}
