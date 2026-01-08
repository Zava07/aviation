package com.itu.aviation.service;

import com.itu.aviation.entity.ModeleAvion;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.ModeleAvionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModeleAvionService {
    private final ModeleAvionRepository repo;

    public ModeleAvionService(ModeleAvionRepository repo) {
        this.repo = repo;
    }

    public List<ModeleAvion> findAll() {
        return repo.findAll();
    }

    public ModeleAvion findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("ModeleAvion not found: " + id));
    }

    public ModeleAvion create(ModeleAvion m) {
        return repo.save(m);
    }

    public ModeleAvion update(Long id, ModeleAvion m) {
        if (!repo.existsById(id))
            throw new ResourceNotFoundException("ModeleAvion not found: " + id);
        m.setId(id);
        return repo.save(m);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}