package com.itu.aviation.repository;

import com.itu.aviation.entity.Reservation;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	long countByVol_Id(Long volInstanceId);

	Optional<Reservation> findByVol_IdAndSiege_Id(Long volInstanceId, Long siegeId);
}
