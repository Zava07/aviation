package com.itu.aviation.service;

import com.itu.aviation.entity.PopulariteVol;
import com.itu.aviation.entity.Vol;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.PopulariteVolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PopulariteVolService {

    private final PopulariteVolRepository populariteVolRepository;

    public PopulariteVolService(PopulariteVolRepository populariteVolRepository) {
        this.populariteVolRepository = populariteVolRepository;
    }

    public List<PopulariteVol> findAll() {
        return populariteVolRepository.findAll();
    }

    public PopulariteVol findById(Long id) {
        return populariteVolRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("PopulariteVol not found: " + id));
    }

    public PopulariteVol create(PopulariteVol p) {
        return populariteVolRepository.save(p);
    }

    public void delete(Long id) {
        populariteVolRepository.deleteById(id);
    }

    public List<PopulariteVol> findByVolId(Long volId) {
        if (volId == null) return List.of();
        return populariteVolRepository.findByVol_IdOrderByDatePointDesc(volId);
    }
    
    /**
     * Calculer le total des points d'un vol
     */
    public Integer getTotalPoints(Long volId) {
        return populariteVolRepository.getTotalPointsByVolId(volId);
    }
    
    /**
     * Récupérer les points obtenus dans une période
     */
    public List<PopulariteVol> findByVolAndPeriod(Long volId, LocalDateTime dateDebut, LocalDateTime dateFin) {
        return populariteVolRepository.findByVolIdAndPeriod(volId, dateDebut, dateFin);
    }
    
    /**
     * Récupérer les points d'un vol pour un mois/année donné
     */
    public Integer getPointsByMonth(Long volId, Integer annee, Integer mois) {
        return populariteVolRepository.getPointsByVolAndMonth(volId, annee, mois);
    }
    
    /**
     * Récupérer les points d'un vol pour une année donnée
     */
    public Integer getPointsByYear(Long volId, Integer annee) {
        return populariteVolRepository.getPointsByVolAndYear(volId, annee);
    }
    
    /**
     * Récupérer tous les points pour un mois/année donné
     */
    public List<PopulariteVol> findByMonth(Integer annee, Integer mois) {
        return populariteVolRepository.findByMonth(annee, mois);
    }
    
    /**
     * Récupérer tous les points pour une année
     */
    public List<PopulariteVol> findByYear(Integer annee) {
        return populariteVolRepository.findByYear(annee);
    }
    
    /**
     * Classement des vols par points pour un mois donné
     */
    public List<Object[]> getRankingByMonth(Integer annee, Integer mois) {
        return populariteVolRepository.getRankingByMonth(annee, mois);
    }
    
    /**
     * Classement des vols par points pour une année donnée
     */
    public List<Object[]> getRankingByYear(Integer annee) {
        return populariteVolRepository.getRankingByYear(annee);
    }
    
    /**
     * Classement global des vols par points
     */
    public List<Object[]> getGlobalRanking() {
        return populariteVolRepository.getGlobalRanking();
    }
    
    /**
     * Ajouter des points de popularité à un vol avec la date
     */
    @Transactional
    public PopulariteVol addPoints(Vol vol, Integer points, LocalDateTime datePoint) {
        PopulariteVol pv = new PopulariteVol();
        pv.setVol(vol);
        pv.setPoint(points);
        pv.setDatePoint(datePoint != null ? datePoint : LocalDateTime.now());
        return populariteVolRepository.save(pv);
    }
    
    /**
     * Ajouter des points de popularité à un vol (date actuelle)
     */
    @Transactional
    public PopulariteVol addPoints(Vol vol, Integer points) {
        return addPoints(vol, points, LocalDateTime.now());
    }
}
