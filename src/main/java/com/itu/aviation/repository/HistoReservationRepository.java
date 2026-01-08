package com.itu.aviation.repository;

import com.itu.aviation.entity.HistoReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoReservationRepository extends JpaRepository<HistoReservation, Long> {
}
