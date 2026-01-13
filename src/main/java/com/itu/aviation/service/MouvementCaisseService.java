package com.itu.aviation.service;

import com.itu.aviation.entity.MouvementCaisse;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.MouvementCaisseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MouvementCaisseService {

    private final MouvementCaisseRepository repo;

    public MouvementCaisseService(MouvementCaisseRepository repo) {
        this.repo = repo;
    }

    public List<MouvementCaisse> findAll() {
        return repo.findAll();
    }

    public MouvementCaisse findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("MouvementCaisse not found: " + id));
    }

    public MouvementCaisse create(MouvementCaisse m) {
        return repo.save(m);
    }

    public MouvementCaisse update(Long id, MouvementCaisse m) {
        MouvementCaisse existing = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("MouvementCaisse not found: " + id));
        existing.setPaiement(m.getPaiement());
        existing.setCaisse(m.getCaisse());
        existing.setMontant(m.getMontant());
        existing.setTypeMouvementCaisse(m.getTypeMouvementCaisse());
        existing.setDateMouvement(m.getDateMouvement());
        return repo.save(existing);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    /**
     * Find mouvements by paiement id
     */
    public List<MouvementCaisse> findByPaiementId(Long paiementId) {
        if (paiementId == null) return java.util.Collections.emptyList();
        return repo.findByPaiementId(paiementId);
    }
}