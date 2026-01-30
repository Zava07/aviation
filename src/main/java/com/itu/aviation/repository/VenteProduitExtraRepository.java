package com.itu.aviation.repository;

import com.itu.aviation.entity.VenteProduitExtra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VenteProduitExtraRepository extends JpaRepository<VenteProduitExtra, Long> {

    List<VenteProduitExtra> findByProduitExtraId(Long produitExtraId);

    List<VenteProduitExtra> findByVolInstanceId(Long volInstanceId);

    List<VenteProduitExtra> findByDateAchatBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT v FROM VenteProduitExtra v WHERE DATE(v.dateAchat) = DATE(:date)")
    List<VenteProduitExtra> findByDate(@Param("date") LocalDateTime date);

    @Query("SELECT COALESCE(SUM(v.prixTotal), 0) FROM VenteProduitExtra v")
    BigDecimal getTotalVentes();

    @Query("SELECT COALESCE(SUM(v.prixTotal), 0) FROM VenteProduitExtra v WHERE v.produitExtra.id = :produitId")
    BigDecimal getTotalVentesByProduit(@Param("produitId") Long produitId);

    @Query("SELECT COALESCE(SUM(v.prixTotal), 0) FROM VenteProduitExtra v WHERE v.volInstance.id = :volInstanceId")
    BigDecimal getTotalVentesByVolInstance(@Param("volInstanceId") Long volInstanceId);

    @Query("SELECT COALESCE(SUM(v.prixTotal), 0) FROM VenteProduitExtra v WHERE v.dateAchat BETWEEN :start AND :end")
    BigDecimal getTotalVentesByPeriode(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT v FROM VenteProduitExtra v ORDER BY v.dateAchat DESC")
    List<VenteProduitExtra> findAllOrderByDateDesc();
}
