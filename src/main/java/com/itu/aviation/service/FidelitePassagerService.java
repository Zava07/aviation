package com.itu.aviation.service;

import com.itu.aviation.entity.FidelitePassager;
import com.itu.aviation.entity.Passager;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.FidelitePassagerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FidelitePassagerService {

    private final FidelitePassagerRepository fidelitePassagerRepository;

    public FidelitePassagerService(FidelitePassagerRepository fidelitePassagerRepository) {
        this.fidelitePassagerRepository = fidelitePassagerRepository;
    }

    public List<FidelitePassager> findAll() {
        return fidelitePassagerRepository.findAll();
    }

    public FidelitePassager findById(Long id) {
        return fidelitePassagerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FidelitePassager not found: " + id));
    }

    /**
     * Récupérer l'historique des points d'un passager
     */
    public List<FidelitePassager> findByPassagerId(Long passagerId) {
        return fidelitePassagerRepository.findByPassagerIdOrderByDateObtentionDesc(passagerId);
    }

    /**
     * Calculer le total des points d'un passager
     */
    public Integer getTotalPoints(Long passagerId) {
        return fidelitePassagerRepository.getTotalPointsByPassagerId(passagerId);
    }

    /**
     * Récupérer les points obtenus dans une période
     */
    public List<FidelitePassager> findByPassagerAndPeriod(Long passagerId, LocalDateTime dateDebut, LocalDateTime dateFin) {
        return fidelitePassagerRepository.findByPassagerIdAndPeriod(passagerId, dateDebut, dateFin);
    }

    /**
     * Récupérer les points d'un passager pour un mois/année donné
     */
    public Integer getPointsByMonth(Long passagerId, Integer annee, Integer mois) {
        return fidelitePassagerRepository.getPointsByPassagerAndMonth(passagerId, annee, mois);
    }

    /**
     * Récupérer les points d'un passager pour une année donnée
     */
    public Integer getPointsByYear(Long passagerId, Integer annee) {
        return fidelitePassagerRepository.getPointsByPassagerAndYear(passagerId, annee);
    }

    /**
     * Récupérer tous les points pour un mois/année donné
     */
    public List<FidelitePassager> findByMonth(Integer annee, Integer mois) {
        return fidelitePassagerRepository.findByMonth(annee, mois);
    }

    /**
     * Ajouter des points de fidélité à un passager avec la date d'obtention (date de réservation)
     */
    @Transactional
    public FidelitePassager addPoints(Passager passager, Integer points, LocalDateTime dateObtention) {
        FidelitePassager fidelite = new FidelitePassager();
        fidelite.setPassager(passager);
        fidelite.setPoints(points);
        fidelite.setDateObtention(dateObtention != null ? dateObtention : LocalDateTime.now());
        return fidelitePassagerRepository.save(fidelite);
    }

    /**
     * Ajouter des points de fidélité à un passager (date actuelle)
     */
    @Transactional
    public FidelitePassager addPoints(Passager passager, Integer points) {
        return addPoints(passager, points, LocalDateTime.now());
    }

    /**
     * Créer un enregistrement de fidélité
     */
    public FidelitePassager create(FidelitePassager fidelitePassager) {
        return fidelitePassagerRepository.save(fidelitePassager);
    }

    /**
     * Mettre à jour un enregistrement de fidélité
     */
    public FidelitePassager update(Long id, FidelitePassager fidelitePassager) {
        if (!fidelitePassagerRepository.existsById(id)) {
            throw new ResourceNotFoundException("FidelitePassager not found: " + id);
        }
        fidelitePassager.setId(id);
        return fidelitePassagerRepository.save(fidelitePassager);
    }

    /**
     * Supprimer un enregistrement de fidélité
     */
    public void delete(Long id) {
        if (!fidelitePassagerRepository.existsById(id)) {
            throw new ResourceNotFoundException("FidelitePassager not found: " + id);
        }
        fidelitePassagerRepository.deleteById(id);
    }
}
