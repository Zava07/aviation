package com.itu.aviation.service;

import com.itu.aviation.entity.PaiementDetail;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.PaiementDetailRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaiementDetailService {

    private final PaiementDetailRepository repo;

    public PaiementDetailService(PaiementDetailRepository repo) {
        this.repo = repo;
    }

    public List<PaiementDetail> findAll() {
        return repo.findAll();
    }

    public PaiementDetail findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("PaiementDetail not found: " + id));
    }

    public PaiementDetail create(PaiementDetail p) {
        return repo.save(p);
    }

    public PaiementDetail update(Long id, PaiementDetail p) {
        PaiementDetail existing = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("PaiementDetail not found: " + id));
        existing.setPaiement(p.getPaiement());
        existing.setMethodePaiement(p.getMethodePaiement());
        existing.setMontant(p.getMontant());
        return repo.save(existing);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}