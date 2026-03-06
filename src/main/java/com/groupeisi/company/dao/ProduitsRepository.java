package com.groupeisi.company.dao;

import com.groupeisi.company.entities.Produits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProduitsRepository extends JpaRepository<Produits, Long> {

    Optional<Produits> findByRef(String ref);

    boolean existsByRef(String ref);

    boolean existsByName(String name);
}
