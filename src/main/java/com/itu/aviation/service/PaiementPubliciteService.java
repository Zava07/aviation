package com.itu.aviation.service;

import com.itu.aviation.entity.*;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.CaisseRepository;
import com.itu.aviation.repository.MouvementCaisseRepository;
import com.itu.aviation.repository.PaiementPubliciteRepository;
import com.itu.aviation.repository.PubliciteRepository;
import com.itu.aviation.repository.TypeMouvementCaisseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PaiementPubliciteService {

    private final PaiementPubliciteRepository paiementPubliciteRepository;
    private final PubliciteRepository publiciteRepository;
    private final SocieteService societeService;
    private final CaisseRepository caisseRepository;
    private final MouvementCaisseRepository mouvementCaisseRepository;
    private final TypeMouvementCaisseRepository typeMouvementCaisseRepository;

    public PaiementPubliciteService(PaiementPubliciteRepository paiementPubliciteRepository,
                                    PubliciteRepository publiciteRepository,
                                    SocieteService societeService,
                                    CaisseRepository caisseRepository,
                                    MouvementCaisseRepository mouvementCaisseRepository,
                                    TypeMouvementCaisseRepository typeMouvementCaisseRepository) {
        this.paiementPubliciteRepository = paiementPubliciteRepository;
        this.publiciteRepository = publiciteRepository;
        this.societeService = societeService;
        this.caisseRepository = caisseRepository;
        this.mouvementCaisseRepository = mouvementCaisseRepository;
        this.typeMouvementCaisseRepository = typeMouvementCaisseRepository;
    }

    public List<PaiementPublicite> findAll() {
        return paiementPubliciteRepository.findAll();
    }

    public PaiementPublicite findById(Long id) {
        return paiementPubliciteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement non trouvé: " + id));
    }

    public List<PaiementPublicite> findBySocieteId(Long societeId) {
        return paiementPubliciteRepository.findBySocieteIdOrderByDateDesc(societeId);
    }

    public List<PaiementPublicite> getPaiementsBySociete(Long societeId) {
        return findBySocieteId(societeId);
    }

    /**
     * Créer un paiement et mettre à jour la caisse
     */
    public PaiementPublicite creerPaiement(Long societeId, BigDecimal montant, Long caisseId, String reference) {
        Societe societe = societeService.findById(societeId);
        Caisse caisse = caisseRepository.findById(caisseId)
                .orElseThrow(() -> new ResourceNotFoundException("Caisse non trouvée: " + caisseId));

        // Créer le paiement
        PaiementPublicite paiement = new PaiementPublicite();
        paiement.setSociete(societe);
        paiement.setMontant(montant);
        paiement.setCaisse(caisse);
        paiement.setMethodePaiement(caisse.getMethodePaiement());
        paiement.setReference(reference);
        paiement.setDatePaiement(LocalDateTime.now());

        paiement = paiementPubliciteRepository.save(paiement);

        // Créer le mouvement de caisse (crédit - entrée d'argent)
        TypeMouvementCaisse typeCredit = typeMouvementCaisseRepository.findByTypeIgnoreCase("credit")
                .orElseThrow(() -> new ResourceNotFoundException("Type mouvement 'credit' non trouvé"));

        MouvementCaisse mouvement = new MouvementCaisse();
        mouvement.setCaisse(caisse);
        mouvement.setMontant(montant);
        mouvement.setTypeMouvementCaisse(typeCredit);
        mouvement.setDateMouvement(LocalDateTime.now());
        mouvementCaisseRepository.save(mouvement);

        // Mettre à jour le solde de la caisse
        caisse.setSolde(caisse.getSolde().add(montant));
        caisse.setDateLastMouv(LocalDateTime.now());
        caisseRepository.save(caisse);

        return paiement;
    }

    /**
     * Montant total des diffusions (à payer) pour une société
     */
    public BigDecimal getMontantTotalDiffusionsBySociete(Long societeId) {
        Double total = publiciteRepository.getTotalMontantBySociete(societeId);
        return total != null ? BigDecimal.valueOf(total) : BigDecimal.ZERO;
    }

    /**
     * Total des paiements effectués par une société
     */
    public BigDecimal getTotalPaiementsBySociete(Long societeId) {
        return paiementPubliciteRepository.getTotalPaiementsBySociete(societeId);
    }

    /**
     * Reste à payer pour une société = Total diffusions - Total paiements
     */
    public BigDecimal getResteAPayerBySociete(Long societeId) {
        BigDecimal totalDiffusions = getMontantTotalDiffusionsBySociete(societeId);
        BigDecimal totalPaiements = getTotalPaiementsBySociete(societeId);
        return totalDiffusions.subtract(totalPaiements);
    }

    /**
     * Récupère le bilan de paiement pour toutes les sociétés
     */
    public List<SocieteBilan> getBilanToutesSocietes() {
        return societeService.findAll().stream()
                .map(s -> new SocieteBilan(
                        s,
                        getMontantTotalDiffusionsBySociete(s.getId()),
                        getTotalPaiementsBySociete(s.getId()),
                        getResteAPayerBySociete(s.getId())
                ))
                .toList();
    }

    public void delete(Long id) {
        if (!paiementPubliciteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Paiement non trouvé: " + id);
        }
        paiementPubliciteRepository.deleteById(id);
    }

    /**
     * Classe interne pour le bilan d'une société
     */
    public static class SocieteBilan {
        private final Societe societe;
        private final BigDecimal totalDiffusions;
        private final BigDecimal totalPaiements;
        private final BigDecimal resteAPayer;

        public SocieteBilan(Societe societe, BigDecimal totalDiffusions, BigDecimal totalPaiements, BigDecimal resteAPayer) {
            this.societe = societe;
            this.totalDiffusions = totalDiffusions;
            this.totalPaiements = totalPaiements;
            this.resteAPayer = resteAPayer;
        }

        public Societe getSociete() { return societe; }
        public BigDecimal getTotalDiffusions() { return totalDiffusions; }
        public BigDecimal getTotalPaiements() { return totalPaiements; }
        public BigDecimal getResteAPayer() { return resteAPayer; }
    }
}
