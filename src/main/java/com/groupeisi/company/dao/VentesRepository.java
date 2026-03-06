package com.groupeisi.company.dao;

import com.groupeisi.company.entities.Ventes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VentesRepository extends JpaRepository<Ventes, Long> {

    List<Ventes> findByProductId(Long productId);

    List<Ventes> findByDatePBetween(Date debut, Date fin);
}
