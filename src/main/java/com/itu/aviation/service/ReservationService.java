package com.itu.aviation.service;

import com.itu.aviation.entity.Reservation;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found: " + id));
    }

    public Reservation create(Reservation r) {
        return reservationRepository.save(r);
    }

    public Reservation update(Long id, Reservation r) {
        if (!reservationRepository.existsById(id))
            throw new ResourceNotFoundException("Reservation not found: " + id);
        r.setId(id);
        return reservationRepository.save(r);
    }

    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }
}