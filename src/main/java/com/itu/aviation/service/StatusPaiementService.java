package com.itu.aviation.service;

import com.itu.aviation.entity.StatusPaiement;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.StatusPaiementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusPaiementService {
    private final StatusPaiementRepository repo;

    public StatusPaiementService(StatusPaiementRepository repo) {
        this.repo = repo;
    }

    public List<StatusPaiement> findAll() {
        return repo.findAll();
    }

    public StatusPaiement findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("StatusPaiement not found: " + id));
    }

    public StatusPaiement create(StatusPaiement s) {
        return repo.save(s);
    }

    public StatusPaiement update(Long id, StatusPaiement s) {
        if (!repo.existsById(id))
            throw new ResourceNotFoundException("StatusPaiement not found: " + id);
        s.setId(id);
        return repo.save(s);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}