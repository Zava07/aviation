package com.itu.aviation.service;

import com.itu.aviation.entity.Billet;
import com.itu.aviation.entity.Paiement;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.BilletRepository;
import com.itu.aviation.repository.PaiementRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PaiementService {

    private final PaiementRepository paiementRepository;
    private final BilletRepository billetRepository;

    public PaiementService(PaiementRepository paiementRepository, BilletRepository billetRepository) {
        this.paiementRepository = paiementRepository;
        this.billetRepository = billetRepository;
    }

    public List<Paiement> findAll() {
        return paiementRepository.findAll();
    }

    public Paiement findById(Long id) {
        return paiementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement not found: " + id));
    }

    public Paiement create(Paiement p) {
        return paiementRepository.save(p);
    }

    public Paiement update(Long id, Paiement p) {
        if (!paiementRepository.existsById(id))
            throw new ResourceNotFoundException("Paiement not found: " + id);
        p.setId(id);
        return paiementRepository.save(p);
    }

    public void delete(Long id) {
        paiementRepository.deleteById(id);
    }

    /**
     * Find paiements by reservation id
     */
    public List<Paiement> findByReservationId(Long reservationId) {
        if (reservationId == null) return Collections.emptyList();
        return paiementRepository.findByReservationId(reservationId);
    }

    /**
     * Find paiements by billet id (via billet -> reservation)
     */
    public List<Paiement> findByBilletId(Long billetId) {
        if (billetId == null) return Collections.emptyList();
        Billet b = billetRepository.findById(billetId).orElse(null);
        if (b == null || b.getReservation() == null || b.getReservation().getId() == null) return Collections.emptyList();
        return findByReservationId(b.getReservation().getId());
    }
}