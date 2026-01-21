package com.itu.aviation.repository;

import com.itu.aviation.entity.PopulariteVol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PopulariteVolRepository extends JpaRepository<PopulariteVol, Long> {
    List<PopulariteVol> findByVol_IdOrderByDatePointDesc(Long volId);
    
    /**
     * Calculer le total des points d'un vol
     */
    @Query("SELECT COALESCE(SUM(pv.point), 0) FROM PopulariteVol pv WHERE pv.vol.id = :volId")
    Integer getTotalPointsByVolId(@Param("volId") Long volId);
    
    /**
     * Trouver les points obtenus dans une période donnée
     */
    @Query("SELECT pv FROM PopulariteVol pv WHERE pv.vol.id = :volId " +
           "AND pv.datePoint BETWEEN :dateDebut AND :dateFin " +
           "ORDER BY pv.datePoint DESC")
    List<PopulariteVol> findByVolIdAndPeriod(
            @Param("volId") Long volId,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin);
    
    /**
     * Calculer le total des points d'un vol pour un mois/année donné
     */
    @Query("SELECT COALESCE(SUM(pv.point), 0) FROM PopulariteVol pv " +
           "WHERE pv.vol.id = :volId " +
           "AND YEAR(pv.datePoint) = :annee " +
           "AND MONTH(pv.datePoint) = :mois")
    Integer getPointsByVolAndMonth(
            @Param("volId") Long volId,
            @Param("annee") Integer annee,
            @Param("mois") Integer mois);
    
    /**
     * Calculer le total des points d'un vol pour une année donnée
     */
    @Query("SELECT COALESCE(SUM(pv.point), 0) FROM PopulariteVol pv " +
           "WHERE pv.vol.id = :volId " +
           "AND YEAR(pv.datePoint) = :annee")
    Integer getPointsByVolAndYear(
            @Param("volId") Long volId,
            @Param("annee") Integer annee);
    
    /**
     * Trouver tous les points pour un mois/année donné
     */
    @Query("SELECT pv FROM PopulariteVol pv " +
           "WHERE YEAR(pv.datePoint) = :annee " +
           "AND MONTH(pv.datePoint) = :mois " +
           "ORDER BY pv.datePoint DESC")
    List<PopulariteVol> findByMonth(
            @Param("annee") Integer annee,
            @Param("mois") Integer mois);
    
    /**
     * Trouver tous les points pour une année donnée
     */
    @Query("SELECT pv FROM PopulariteVol pv " +
           "WHERE YEAR(pv.datePoint) = :annee " +
           "ORDER BY pv.datePoint DESC")
    List<PopulariteVol> findByYear(@Param("annee") Integer annee);
    
    /**
     * Classement des vols par points pour un mois donné
     */
    @Query("SELECT pv.vol.id, SUM(pv.point) as totalPoints FROM PopulariteVol pv " +
           "WHERE YEAR(pv.datePoint) = :annee AND MONTH(pv.datePoint) = :mois " +
           "GROUP BY pv.vol.id ORDER BY totalPoints DESC")
    List<Object[]> getRankingByMonth(@Param("annee") Integer annee, @Param("mois") Integer mois);
    
    /**
     * Classement des vols par points pour une année donnée
     */
    @Query("SELECT pv.vol.id, SUM(pv.point) as totalPoints FROM PopulariteVol pv " +
           "WHERE YEAR(pv.datePoint) = :annee " +
           "GROUP BY pv.vol.id ORDER BY totalPoints DESC")
    List<Object[]> getRankingByYear(@Param("annee") Integer annee);
    
    /**
     * Classement global des vols par points
     */
    @Query("SELECT pv.vol.id, SUM(pv.point) as totalPoints FROM PopulariteVol pv " +
           "GROUP BY pv.vol.id ORDER BY totalPoints DESC")
    List<Object[]> getGlobalRanking();
}
