package com.itu.aviation.repository;

import com.itu.aviation.entity.VolEquipe;
import com.itu.aviation.entity.VolEquipeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VolEquipeRepository extends JpaRepository<VolEquipe, VolEquipeId> {
    
    // Trouver toutes les affectations d'un membre
    @Query("SELECT ve FROM VolEquipe ve WHERE ve.membre.id = :membreId")
    List<VolEquipe> findByMembreId(@Param("membreId") Long membreId);
    
    // Trouver les affectations d'un membre qui chevauchent une période donnée
    @Query("SELECT ve FROM VolEquipe ve " +
           "WHERE ve.membre.id = :membreId " +
           "AND ve.volInstance.dateDepart < :dateArrivee " +
           "AND ve.volInstance.dateArrivee > :dateDepart")
    List<VolEquipe> findConflictingAffectations(
            @Param("membreId") Long membreId,
            @Param("dateDepart") LocalDateTime dateDepart,
            @Param("dateArrivee") LocalDateTime dateArrivee);
}
