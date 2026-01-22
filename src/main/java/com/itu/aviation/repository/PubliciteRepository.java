package com.itu.aviation.repository;

import com.itu.aviation.entity.Publicite;
import com.itu.aviation.entity.Societe;
import com.itu.aviation.entity.VolInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PubliciteRepository extends JpaRepository<Publicite, Long> {
    List<Publicite> findBySociete(Societe societe);
    List<Publicite> findBySocieteId(Long societeId);
    List<Publicite> findByVolInstance(VolInstance volInstance);
    List<Publicite> findByDatePubliciteBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT p FROM Publicite p WHERE YEAR(p.datePublicite) = :year AND MONTH(p.datePublicite) = :month ORDER BY p.datePublicite DESC")
    List<Publicite> findByYearAndMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT p FROM Publicite p WHERE YEAR(p.datePublicite) = :year ORDER BY p.datePublicite DESC")
    List<Publicite> findByYear(@Param("year") int year);

    @Query("SELECT SUM(p.montant) FROM Publicite p WHERE p.societe.id = :societeId")
    Double getTotalMontantBySociete(@Param("societeId") Long societeId);

    @Query("SELECT SUM(p.montant) FROM Publicite p WHERE p.datePublicite BETWEEN :start AND :end")
    Double getTotalMontantByPeriode(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT SUM(p.montant) FROM Publicite p WHERE YEAR(p.datePublicite) = :year AND MONTH(p.datePublicite) = :month")
    Double getTotalMontantByYearAndMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT SUM(p.montant) FROM Publicite p WHERE YEAR(p.datePublicite) = :year")
    Double getTotalMontantByYear(@Param("year") int year);

    @Query("SELECT COUNT(p) FROM Publicite p WHERE YEAR(p.datePublicite) = :year AND MONTH(p.datePublicite) = :month")
    Long countByYearAndMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT COUNT(p) FROM Publicite p WHERE YEAR(p.datePublicite) = :year")
    Long countByYear(@Param("year") int year);
}