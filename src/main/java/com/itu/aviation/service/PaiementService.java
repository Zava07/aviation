package com.itu.aviation.service;

import com.itu.aviation.entity.Paiement;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.PaiementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaiementService {

    private final PaiementRepository paiementRepository;

    public PaiementService(PaiementRepository paiementRepository) {
        this.paiementRepository = paiementRepository;
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
}