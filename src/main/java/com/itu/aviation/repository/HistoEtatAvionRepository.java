package com.itu.aviation.repository;

import com.itu.aviation.entity.HistoEtatAvion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoEtatAvionRepository extends JpaRepository<HistoEtatAvion, Long> {
}
