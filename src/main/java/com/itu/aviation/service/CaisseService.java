package com.itu.aviation.service;

import com.itu.aviation.entity.Caisse;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.CaisseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaisseService {

    private final CaisseRepository repo;

    public CaisseService(CaisseRepository repo) {
        this.repo = repo;
    }

    public List<Caisse> findAll() {
        return repo.findAll();
    }

    public List<Caisse> findByMethodeId(Long methodeId) {
        if (methodeId == null) return repo.findAll();
        return repo.findByMethodePaiement_Id(methodeId);
    }

    public Caisse findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Caisse not found: " + id));
    }

    public Caisse create(Caisse c) {
        return repo.save(c);
    }

    public Caisse update(Long id, Caisse c) {
        Caisse existing = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Caisse not found: " + id));
        existing.setSolde(c.getSolde());
        existing.setMethodePaiement(c.getMethodePaiement());
        existing.setDateLastMouv(c.getDateLastMouv());
        return repo.save(existing);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}