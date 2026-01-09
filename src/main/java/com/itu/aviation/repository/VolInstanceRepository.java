package com.itu.aviation.repository;

import com.itu.aviation.entity.VolInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VolInstanceRepository extends JpaRepository<VolInstance, Long> {

    @Query("SELECT vi FROM VolInstance vi WHERE vi.avion.id = :avionId AND vi.dateDepart < :dateArrivee AND vi.dateArrivee > :dateDepart")
    List<VolInstance> findOverlapping(@Param("avionId") Long avionId, @Param("dateDepart") LocalDateTime dateDepart, @Param("dateArrivee") LocalDateTime dateArrivee);
}
