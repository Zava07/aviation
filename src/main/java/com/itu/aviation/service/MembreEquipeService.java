package com.itu.aviation.service;

import com.itu.aviation.entity.MembreEquipe;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.MembreEquipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembreEquipeService {

    private final MembreEquipeRepository repo;

    public MembreEquipeService(MembreEquipeRepository repo) {
        this.repo = repo;
    }

    public List<MembreEquipe> findAll() {
        return repo.findAll();
    }

    public MembreEquipe findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Membre not found: " + id));
    }

    public MembreEquipe create(MembreEquipe m) {
        return repo.save(m);
    }

    public MembreEquipe update(Long id, MembreEquipe m) {
        if (!repo.existsById(id))
            throw new ResourceNotFoundException("Membre not found: " + id);
        m.setId(id);
        return repo.save(m);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}