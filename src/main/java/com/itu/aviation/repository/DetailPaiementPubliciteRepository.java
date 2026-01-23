package com.itu.aviation.repository;

import com.itu.aviation.entity.DetailPaiementPublicite;
import com.itu.aviation.entity.PaiementPublicite;
import com.itu.aviation.entity.Societe;
import com.itu.aviation.entity.VolInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DetailPaiementPubliciteRepository extends JpaRepository<DetailPaiementPublicite, Long> {

    List<DetailPaiementPublicite> findByPaiementPublicite(PaiementPublicite paiementPublicite);

    List<DetailPaiementPublicite> findByPaiementPubliciteId(Long paiementPubliciteId);

    List<DetailPaiementPublicite> findBySociete(Societe societe);

    List<DetailPaiementPublicite> findBySocieteId(Long societeId);

    List<DetailPaiementPublicite> findByVolInstance(VolInstance volInstance);

    List<DetailPaiementPublicite> findByVolInstanceId(Long volInstanceId);

    @Query("SELECT d FROM DetailPaiementPublicite d WHERE d.societe.id = :societeId AND d.volInstance.id = :volInstanceId")
    List<DetailPaiementPublicite> findBySocieteAndVolInstance(@Param("societeId") Long societeId, @Param("volInstanceId") Long volInstanceId);

    @Query("SELECT COALESCE(SUM(d.montantPaye), 0) FROM DetailPaiementPublicite d WHERE d.societe.id = :societeId AND d.volInstance.id = :volInstanceId")
    BigDecimal getTotalPayeBySocieteAndVolInstance(@Param("societeId") Long societeId, @Param("volInstanceId") Long volInstanceId);

    @Query("SELECT d FROM DetailPaiementPublicite d WHERE d.societe.id = :societeId ORDER BY d.dateCreation DESC")
    List<DetailPaiementPublicite> findBySocieteIdOrderByDateDesc(@Param("societeId") Long societeId);

    @Query("SELECT d FROM DetailPaiementPublicite d JOIN d.paiementPublicite p WHERE p.id = :paiementId ORDER BY d.volInstance.dateDepart ASC")
    List<DetailPaiementPublicite> findByPaiementIdOrderByDateDepart(@Param("paiementId") Long paiementId);
}
