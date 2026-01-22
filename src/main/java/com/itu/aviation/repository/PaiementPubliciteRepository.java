package com.itu.aviation.repository;

import com.itu.aviation.entity.PaiementPublicite;
import com.itu.aviation.entity.Societe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaiementPubliciteRepository extends JpaRepository<PaiementPublicite, Long> {

    List<PaiementPublicite> findBySociete(Societe societe);

    List<PaiementPublicite> findBySocieteId(Long societeId);

    List<PaiementPublicite> findByDatePaiementBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COALESCE(SUM(p.montant), 0) FROM PaiementPublicite p WHERE p.societe.id = :societeId")
    BigDecimal getTotalPaiementsBySociete(@Param("societeId") Long societeId);

    @Query("SELECT COALESCE(SUM(p.montant), 0) FROM PaiementPublicite p WHERE p.societe.id = :societeId AND p.datePaiement BETWEEN :start AND :end")
    BigDecimal getTotalPaiementsBySocieteAndPeriode(@Param("societeId") Long societeId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT p FROM PaiementPublicite p WHERE p.societe.id = :societeId ORDER BY p.datePaiement DESC")
    List<PaiementPublicite> findBySocieteIdOrderByDateDesc(@Param("societeId") Long societeId);
}
