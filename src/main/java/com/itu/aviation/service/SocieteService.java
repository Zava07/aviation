package com.itu.aviation.service;

import com.itu.aviation.entity.Societe;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.SocieteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SocieteService {

    private final SocieteRepository societeRepository;

    public SocieteService(SocieteRepository societeRepository) {
        this.societeRepository = societeRepository;
    }

    public List<Societe> findAll() {
        return societeRepository.findAll();
    }

    public Societe findById(Long id) {
        return societeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Société non trouvée: " + id));
    }

    public Optional<Societe> findByNom(String nom) {
        return societeRepository.findByNomSocieteIgnoreCase(nom);
    }

    public Societe create(Societe societe) {
        if (societe.getId() == null && societeRepository.existsByNomSocieteIgnoreCase(societe.getNomSociete())) {
            throw new IllegalArgumentException("Une société avec ce nom existe déjà: " + societe.getNomSociete());
        }
        return societeRepository.save(societe);
    }

    public Societe update(Long id, Societe societeDetails) {
        Societe societe = findById(id);
        if (!societe.getNomSociete().equalsIgnoreCase(societeDetails.getNomSociete())
                && societeRepository.existsByNomSocieteIgnoreCase(societeDetails.getNomSociete())) {
            throw new IllegalArgumentException("Une société avec ce nom existe déjà: " + societeDetails.getNomSociete());
        }
        societe.setNomSociete(societeDetails.getNomSociete());
        return societeRepository.save(societe);
    }

    public void delete(Long id) {
        if (!societeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Société non trouvée: " + id);
        }
        societeRepository.deleteById(id);
    }
}