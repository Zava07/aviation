package com.itu.aviation.service;

import com.itu.aviation.entity.*;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.PubliciteRepository;
import com.itu.aviation.repository.PubliciteVideoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DiffusionService {

    private final PubliciteRepository publiciteRepository;
    private final PubliciteVideoRepository publiciteVideoRepository;
    private final SocieteService societeService;
    private final VolInstanceService volInstanceService;
    private final ConfigService configService;

    public DiffusionService(PubliciteRepository publiciteRepository,
                            PubliciteVideoRepository publiciteVideoRepository,
                            SocieteService societeService,
                            VolInstanceService volInstanceService,
                            ConfigService configService) {
        this.publiciteRepository = publiciteRepository;
        this.publiciteVideoRepository = publiciteVideoRepository;
        this.societeService = societeService;
        this.volInstanceService = volInstanceService;
        this.configService = configService;
    }

    public List<Publicite> findAll() {
        return publiciteRepository.findAll();
    }

    public Publicite findById(Long id) {
        return publiciteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Diffusion non trouvée: " + id));
    }

    public List<Publicite> findByYearAndMonth(int year, int month) {
        if (month == 0) {
            return publiciteRepository.findByYear(year);
        }
        return publiciteRepository.findByYearAndMonth(year, month);
    }

    /**
     * Crée des diffusions multiples basées sur le nombre de fois spécifié
     */
    public List<Publicite> createMultiple(Long societeId, Long publiciteVideoId, Long volInstanceId, int nombreFois) {
        Societe societe = societeService.findById(societeId);
        VolInstance volInstance = volInstanceService.findById(volInstanceId);
        PubliciteVideo publiciteVideo = publiciteVideoId != null ? 
                publiciteVideoRepository.findById(publiciteVideoId).orElse(null) : null;

        Double montant = configService.getCoutPubliciter();
        LocalDateTime datePublicite = volInstance.getDateDepart();

        List<Publicite> diffusions = new ArrayList<>();
        for (int i = 0; i < nombreFois; i++) {
            Publicite p = new Publicite();
            p.setSociete(societe);
            p.setVolInstance(volInstance);
            p.setPubliciteVideo(publiciteVideo);
            p.setMontant(montant);
            p.setDatePublicite(datePublicite);
            diffusions.add(publiciteRepository.save(p));
        }
        return diffusions;
    }

    public Publicite update(Long id, Long societeId, Long publiciteVideoId, Long volInstanceId, Double montant) {
        Publicite p = findById(id);
        
        if (societeId != null) {
            p.setSociete(societeService.findById(societeId));
        }
        if (volInstanceId != null) {
            VolInstance vi = volInstanceService.findById(volInstanceId);
            p.setVolInstance(vi);
            p.setDatePublicite(vi.getDateDepart());
        }
        if (publiciteVideoId != null) {
            PubliciteVideo pv = publiciteVideoRepository.findById(publiciteVideoId).orElse(null);
            p.setPubliciteVideo(pv);
        }
        if (montant != null) {
            p.setMontant(montant);
        }
        return publiciteRepository.save(p);
    }

    public void delete(Long id) {
        if (!publiciteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Diffusion non trouvée: " + id);
        }
        publiciteRepository.deleteById(id);
    }

    /**
     * CA réel: somme des montants dans la table publicite
     */
    public Double getCAReelByPeriode(int year, int month) {
        Double total;
        if (month == 0) {
            total = publiciteRepository.getTotalMontantByYear(year);
        } else {
            total = publiciteRepository.getTotalMontantByYearAndMonth(year, month);
        }
        return total != null ? total : 0.0;
    }

    /**
     * CA théorique: nombre de diffusions * cout_publiciter de la config actuelle
     */
    public Double getCATheoriqueByPeriode(int year, int month) {
        Long count;
        if (month == 0) {
            count = publiciteRepository.countByYear(year);
        } else {
            count = publiciteRepository.countByYearAndMonth(year, month);
        }
        Double coutConfig = configService.getCoutPubliciter();
        return (count != null ? count : 0L) * coutConfig;
    }
}
