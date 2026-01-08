package com.itu.aviation.repository;

import com.itu.aviation.entity.MethodePaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MethodePaiementRepository extends JpaRepository<MethodePaiement, Long> {
}
