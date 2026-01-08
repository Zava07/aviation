package com.itu.aviation.repository;

import com.itu.aviation.entity.StatusPaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusPaiementRepository extends JpaRepository<StatusPaiement, Long> {
}
