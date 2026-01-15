package com.itu.aviation.repository;

import com.itu.aviation.entity.FidelitePassager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FidelitePassagerRepository extends JpaRepository<FidelitePassager, Long> {
    
    /**
     * Trouver tous les points de fidélité d'un passager
     */
    List<FidelitePassager> findByPassagerId(Long passagerId);
    
    /**
     * Trouver les points de fidélité d'un passager ordonnés par date
     */
    List<FidelitePassager> findByPassagerIdOrderByDateObtentionDesc(Long passagerId);
    
    /**
     * Calculer le total des points de fidélité d'un passager
     */
    @Query("SELECT COALESCE(SUM(fp.points), 0) FROM FidelitePassager fp WHERE fp.passager.id = :passagerId")
    Integer getTotalPointsByPassagerId(@Param("passagerId") Long passagerId);
    
    /**
     * Trouver les points obtenus dans une période donnée
     */
    @Query("SELECT fp FROM FidelitePassager fp WHERE fp.passager.id = :passagerId " +
           "AND fp.dateObtention BETWEEN :dateDebut AND :dateFin " +
           "ORDER BY fp.dateObtention DESC")
    List<FidelitePassager> findByPassagerIdAndPeriod(
            @Param("passagerId") Long passagerId,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin);
    
    /**
     * Calculer le total des points d'un passager pour un mois/année donné
     */
    @Query("SELECT COALESCE(SUM(fp.points), 0) FROM FidelitePassager fp " +
           "WHERE fp.passager.id = :passagerId " +
           "AND YEAR(fp.dateObtention) = :annee " +
           "AND MONTH(fp.dateObtention) = :mois")
    Integer getPointsByPassagerAndMonth(
            @Param("passagerId") Long passagerId,
            @Param("annee") Integer annee,
            @Param("mois") Integer mois);
    
    /**
     * Calculer le total des points d'un passager pour une année donnée
     */
    @Query("SELECT COALESCE(SUM(fp.points), 0) FROM FidelitePassager fp " +
           "WHERE fp.passager.id = :passagerId " +
           "AND YEAR(fp.dateObtention) = :annee")
    Integer getPointsByPassagerAndYear(
            @Param("passagerId") Long passagerId,
            @Param("annee") Integer annee);
    
    /**
     * Trouver tous les points pour un mois/année donné
     */
    @Query("SELECT fp FROM FidelitePassager fp " +
           "WHERE YEAR(fp.dateObtention) = :annee " +
           "AND MONTH(fp.dateObtention) = :mois " +
           "ORDER BY fp.dateObtention DESC")
    List<FidelitePassager> findByMonth(
            @Param("annee") Integer annee,
            @Param("mois") Integer mois);
}
