package com.itu.aviation.service;

import com.itu.aviation.entity.MethodePaiement;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.MethodePaiementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MethodePaiementService {
    private final MethodePaiementRepository repo;

    public MethodePaiementService(MethodePaiementRepository repo) {
        this.repo = repo;
    }

    public List<MethodePaiement> findAll() {
        return repo.findAll();
    }

    public MethodePaiement findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("MethodePaiement not found: " + id));
    }

    public MethodePaiement create(MethodePaiement m) {
        return repo.save(m);
    }

    public MethodePaiement update(Long id, MethodePaiement m) {
        if (!repo.existsById(id))
            throw new ResourceNotFoundException("MethodePaiement not found: " + id);
        m.setId(id);
        return repo.save(m);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}