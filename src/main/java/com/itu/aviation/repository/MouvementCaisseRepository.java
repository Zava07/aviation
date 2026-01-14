package com.itu.aviation.repository;

import com.itu.aviation.entity.MouvementCaisse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MouvementCaisseRepository extends JpaRepository<MouvementCaisse, Long> {
    List<MouvementCaisse> findByPaiementId(Long paiementId);
    List<MouvementCaisse> findByCaisseId(Long caisseId);
    List<MouvementCaisse> findByCaisseIdOrderByDateMouvementDesc(Long caisseId);
} 