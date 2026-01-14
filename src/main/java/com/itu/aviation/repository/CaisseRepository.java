package com.itu.aviation.repository;

import com.itu.aviation.entity.Caisse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CaisseRepository extends JpaRepository<Caisse, Long> {
    List<Caisse> findByMethodePaiement_Id(Long methodeId);
}