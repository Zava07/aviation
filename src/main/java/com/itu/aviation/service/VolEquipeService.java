package com.itu.aviation.service;

import com.itu.aviation.entity.VolEquipe;
import com.itu.aviation.entity.VolEquipeId;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.VolEquipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VolEquipeService {
    private final VolEquipeRepository repo;

    public VolEquipeService(VolEquipeRepository repo) {
        this.repo = repo;
    }

    public List<VolEquipe> findAll() {
        return repo.findAll();
    }

    public VolEquipe findById(VolEquipeId id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("VolEquipe not found: " + id));
    }

    public VolEquipe create(VolEquipe v) {
        return repo.save(v);
    }

    public VolEquipe update(VolEquipeId id, VolEquipe v) {
        if (!repo.existsById(id))
            throw new ResourceNotFoundException("VolEquipe not found: " + id);
        v.setId(id);
        return repo.save(v);
    }

    public void delete(VolEquipeId id) {
        repo.deleteById(id);
    }
}