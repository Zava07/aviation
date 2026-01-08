package com.itu.aviation.repository;

import com.itu.aviation.entity.EtatAvion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtatAvionRepository extends JpaRepository<EtatAvion, Long> {
}
