package com.itu.aviation.service;

import com.itu.aviation.entity.VolEquipe;
import com.itu.aviation.entity.VolEquipeId;
import com.itu.aviation.entity.VolInstance;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.VolEquipeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    
    public List<VolEquipe> findByMembreId(Long membreId) {
        return repo.findByMembreId(membreId);
    }
    
    /**
     * Vérifie si un membre a des conflits d'affectation pour une période donnée
     * @return Liste des affectations en conflit (vide si pas de conflit)
     */
    public List<VolEquipe> findConflictingAffectations(Long membreId, LocalDateTime dateDepart, LocalDateTime dateArrivee) {
        return repo.findConflictingAffectations(membreId, dateDepart, dateArrivee);
    }
    
    /**
     * Vérifie si une affectation existe déjà
     */
    public boolean existsById(VolEquipeId id) {
        return repo.existsById(id);
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