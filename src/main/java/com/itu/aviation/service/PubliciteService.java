package com.itu.aviation.service;

import com.itu.aviation.entity.Publicite;
import com.itu.aviation.entity.Societe;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.PubliciteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PubliciteService {

    private final PubliciteRepository publiciteRepository;
    private final SocieteService societeService;

    public PubliciteService(PubliciteRepository publiciteRepository, SocieteService societeService) {
        this.publiciteRepository = publiciteRepository;
        this.societeService = societeService;
    }

    public List<Publicite> findAll() {
        return publiciteRepository.findAll();
    }

    public Publicite findById(Long id) {
        return publiciteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publicité non trouvée: " + id));
    }

    public List<Publicite> findBySocieteId(Long societeId) {
        return publiciteRepository.findBySocieteId(societeId);
    }

    public List<Publicite> findByPeriode(LocalDateTime start, LocalDateTime end) {
        return publiciteRepository.findByDatePubliciteBetween(start, end);
    }

    public Double getTotalMontantBySociete(Long societeId) {
        Double total = publiciteRepository.getTotalMontantBySociete(societeId);
        return total != null ? total : 0.0;
    }

    public Double getTotalMontantByPeriode(LocalDateTime start, LocalDateTime end) {
        Double total = publiciteRepository.getTotalMontantByPeriode(start, end);
        return total != null ? total : 0.0;
    }

    public Publicite create(Publicite publicite) {
        if (publicite.getSociete() == null || publicite.getSociete().getId() == null) {
            throw new IllegalArgumentException("La publicité doit être associée à une société");
        }
        Societe societe = societeService.findById(publicite.getSociete().getId());
        publicite.setSociete(societe);
        return publiciteRepository.save(publicite);
    }

    public Publicite update(Long id, Publicite details) {
        Publicite p = findById(id);
        if (details.getMontant() != null) p.setMontant(details.getMontant());
        if (details.getDatePublicite() != null) p.setDatePublicite(details.getDatePublicite());
        if (details.getSociete() != null && details.getSociete().getId() != null) {
            Societe s = societeService.findById(details.getSociete().getId());
            p.setSociete(s);
        }
        return publiciteRepository.save(p);
    }

    public void delete(Long id) {
        if (!publiciteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Publicité non trouvée: " + id);
        }
        publiciteRepository.deleteById(id);
    }
}