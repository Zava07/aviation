package com.itu.aviation.service;

import com.itu.aviation.entity.PrixVolClasse;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.PrixVolClasseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrixVolClasseService {
    private final PrixVolClasseRepository repo;

    public PrixVolClasseService(PrixVolClasseRepository repo) {
        this.repo = repo;
    }

    public List<PrixVolClasse> findAll() {
        return repo.findAll();
    }

    public PrixVolClasse findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("PrixVolClasse not found: " + id));
    }

    public PrixVolClasse create(PrixVolClasse p) {
        return repo.save(p);
    }

    public PrixVolClasse update(Long id, PrixVolClasse p) {
        if (!repo.existsById(id))
            throw new ResourceNotFoundException("PrixVolClasse not found: " + id);
        p.setId(id);
        return repo.save(p);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}