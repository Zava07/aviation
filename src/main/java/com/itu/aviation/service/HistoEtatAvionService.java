package com.itu.aviation.service;

import com.itu.aviation.entity.HistoEtatAvion;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.HistoEtatAvionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoEtatAvionService {
    private final HistoEtatAvionRepository repo;

    public HistoEtatAvionService(HistoEtatAvionRepository repo) {
        this.repo = repo;
    }

    public List<HistoEtatAvion> findAll() {
        return repo.findAll();
    }

    public HistoEtatAvion findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("HistoEtatAvion not found: " + id));
    }

    public HistoEtatAvion create(HistoEtatAvion h) {
        return repo.save(h);
    }

    public HistoEtatAvion update(Long id, HistoEtatAvion h) {
        if (!repo.existsById(id))
            throw new ResourceNotFoundException("HistoEtatAvion not found: " + id);
        h.setId(id);
        return repo.save(h);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}