package com.itu.aviation.repository;

import com.itu.aviation.entity.Vol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface VolRepository extends JpaRepository<Vol, Long> {
    Vol findByNumeroVol(String numeroVol);

    @Query("SELECT v FROM Vol v WHERE v.avion.id = :avionId AND v.dateDepart < :dateArrivee AND v.dateArrivee > :dateDepart")
    List<Vol> findOverlapping(@Param("avionId") Long avionId, @Param("dateDepart") java.time.LocalDateTime dateDepart, @Param("dateArrivee") java.time.LocalDateTime dateArrivee);
}
