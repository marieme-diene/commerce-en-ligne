package com.groupeisi.company.service;

import com.groupeisi.company.dao.ProduitsRepository;
import com.groupeisi.company.dao.VentesRepository;
import com.groupeisi.company.dto.VentesDto;
import com.groupeisi.company.entities.Produits;
import com.groupeisi.company.entities.Ventes;
import com.groupeisi.company.mapper.VentesMapper;
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
public class VentesService {

    private final VentesRepository ventesRepository;
    private final ProduitsRepository produitsRepository;
    private final VentesMapper ventesMapper;

    /**
     * Récupère toutes les ventes
     */
    @Transactional(readOnly = true)
    public List<VentesDto> findAll() {
        log.info("Récupération de toutes les ventes");
        List<Ventes> ventesList = ventesRepository.findAll();
        log.info("{} vente(s) trouvée(s)", ventesList.size());
        return ventesMapper.toDtoList(ventesList);
    }

    /**
     * Récupère une vente par son ID
     */
    @Transactional(readOnly = true)
    public Optional<VentesDto> findById(Long id) {
        log.info("Recherche de la vente avec l'ID : {}", id);
        return ventesRepository.findById(id)
                .map(ventes -> {
                    log.info("Vente trouvée, ID : {}", ventes.getId());
                    return ventesMapper.toDto(ventes);
                });
    }

    /**
     * Récupère les ventes par produit
     */
    @Transactional(readOnly = true)
    public List<VentesDto> findByProductId(Long productId) {
        log.info("Recherche des ventes pour le produit ID : {}", productId);
        List<Ventes> ventesList = ventesRepository.findByProductId(productId);
        log.info("{} vente(s) trouvée(s) pour le produit ID : {}", ventesList.size(), productId);
        return ventesMapper.toDtoList(ventesList);
    }

    /**
     * Récupère les ventes entre deux dates
     */
    @Transactional(readOnly = true)
    public List<VentesDto> findByDateBetween(Date debut, Date fin) {
        log.info("Recherche des ventes entre {} et {}", debut, fin);
        return ventesMapper.toDtoList(ventesRepository.findByDatePBetween(debut, fin));
    }

    /**
     * Enregistre une vente et décrémente le stock
     */
    public VentesDto save(VentesDto ventesDto) {
        log.info("Création d'une nouvelle vente pour le produit ID : {}", ventesDto.getProduct().getId());

        Produits produit = produitsRepository.findById(ventesDto.getProduct().getId())
                .orElseThrow(() -> {
                    log.error("Produit non trouvé avec l'ID : {}", ventesDto.getProduct().getId());
                    return new RuntimeException("Produit non trouvé avec l'ID : " + ventesDto.getProduct().getId());
                });

        // Vérification du stock disponible
        if (produit.getStock() < ventesDto.getQuantity()) {
            log.error("Stock insuffisant pour le produit '{}' : stock disponible = {}, quantité demandée = {}",
                    produit.getName(), produit.getStock(), ventesDto.getQuantity());
            throw new IllegalStateException("Stock insuffisant pour le produit '" + produit.getName()
                    + "'. Stock disponible : " + produit.getStock()
                    + ", quantité demandée : " + ventesDto.getQuantity());
        }

        Ventes ventes = ventesMapper.toEntity(ventesDto);
        ventes.setProduct(produit);

        // Mise à jour du stock : vente = diminution du stock
        double newStock = produit.getStock() - ventesDto.getQuantity();
        produit.setStock(newStock);
        produitsRepository.save(produit);
        log.info("Stock mis à jour pour le produit '{}' : nouveau stock = {}", produit.getName(), newStock);

        Ventes saved = ventesRepository.save(ventes);
        log.info("Vente enregistrée avec succès, ID : {}", saved.getId());
        return ventesMapper.toDto(saved);
    }

    /**
     * Met à jour une vente
     */
    public VentesDto update(Long id, VentesDto ventesDto) {
        log.info("Mise à jour de la vente avec l'ID : {}", id);
        Ventes existing = ventesRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Vente non trouvée avec l'ID : {}", id);
                    return new RuntimeException("Vente non trouvée avec l'ID : " + id);
                });
        ventesMapper.updateEntityFromDto(ventesDto, existing);
        Ventes updated = ventesRepository.save(existing);
        log.info("Vente mise à jour avec succès, ID : {}", updated.getId());
        return ventesMapper.toDto(updated);
    }

    /**
     * Supprime une vente
     */
    public void deleteById(Long id) {
        log.info("Suppression de la vente avec l'ID : {}", id);
        if (!ventesRepository.existsById(id)) {
            log.error("Impossible de supprimer : vente non trouvée avec l'ID : {}", id);
            throw new RuntimeException("Vente non trouvée avec l'ID : " + id);
        }
        ventesRepository.deleteById(id);
        log.info("Vente supprimée avec succès, ID : {}", id);
    }
}
