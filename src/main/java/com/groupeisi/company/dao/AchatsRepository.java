package com.groupeisi.company.dao;

import com.groupeisi.company.entities.Achats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AchatsRepository extends JpaRepository<Achats, Long> {

    List<Achats> findByProductId(Long productId);

    List<Achats> findByDatePBetween(Date debut, Date fin);
}
