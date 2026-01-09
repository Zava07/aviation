package com.itu.aviation.repository;

import com.itu.aviation.entity.StatutReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatutReservationRepository extends JpaRepository<StatutReservation, Long> {
    Optional<StatutReservation> findByNomStatusContainingIgnoreCase(String nom);
}
