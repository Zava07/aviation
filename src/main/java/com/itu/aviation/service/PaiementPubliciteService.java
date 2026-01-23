package com.itu.aviation.service;

import com.itu.aviation.entity.*;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.CaisseRepository;
import com.itu.aviation.repository.DetailPaiementPubliciteRepository;
import com.itu.aviation.repository.MouvementCaisseRepository;
import com.itu.aviation.repository.PaiementPubliciteRepository;
import com.itu.aviation.repository.PubliciteRepository;
import com.itu.aviation.repository.TypeMouvementCaisseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PaiementPubliciteService {

    private final PaiementPubliciteRepository paiementPubliciteRepository;
    private final PubliciteRepository publiciteRepository;
    private final SocieteService societeService;
    private final CaisseRepository caisseRepository;
    private final MouvementCaisseRepository mouvementCaisseRepository;
    private final TypeMouvementCaisseRepository typeMouvementCaisseRepository;
    private final DetailPaiementPubliciteRepository detailPaiementPubliciteRepository;

    public PaiementPubliciteService(PaiementPubliciteRepository paiementPubliciteRepository,
                                    PubliciteRepository publiciteRepository,
                                    SocieteService societeService,
                                    CaisseRepository caisseRepository,
                                    MouvementCaisseRepository mouvementCaisseRepository,
                                    TypeMouvementCaisseRepository typeMouvementCaisseRepository,
                                    DetailPaiementPubliciteRepository detailPaiementPubliciteRepository) {
        this.paiementPubliciteRepository = paiementPubliciteRepository;
        this.publiciteRepository = publiciteRepository;
        this.societeService = societeService;
        this.caisseRepository = caisseRepository;
        this.mouvementCaisseRepository = mouvementCaisseRepository;
        this.typeMouvementCaisseRepository = typeMouvementCaisseRepository;
        this.detailPaiementPubliciteRepository = detailPaiementPubliciteRepository;
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
     * Créer un paiement avec répartition proportionnelle par vol instance.
     * Le montant payé est distribué sur toutes les instances de vol proportionnellement
     * au reste à payer de chaque instance.
     */
    public PaiementPublicite creerPaiement(Long societeId, BigDecimal montant, Long caisseId, String reference, LocalDateTime datePaiement) {
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
        paiement.setDatePaiement(datePaiement != null ? datePaiement : LocalDateTime.now());

        paiement = paiementPubliciteRepository.save(paiement);

        // Créer les détails de paiement avec répartition proportionnelle
        creerDetailsPaiementProportionnel(paiement, societe, montant);

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
     * Créer les détails de paiement répartis proportionnellement par vol instance.
     * 
     * Exemple:
     * - vol_instance 1: montant diffusion 200 Ar, déjà payé 0 -> reste 200
     * - vol_instance 2: montant diffusion 300 Ar, déjà payé 0 -> reste 300
     * - vol_instance 3: montant diffusion 400 Ar, déjà payé 0 -> reste 400
     * Total reste à payer = 900 Ar
     * 
     * Si société paie 500 Ar:
     * - Pourcentage = 500/900 = 55.56%
     * - vol_instance 1 reçoit: 200 * 55.56% = 111.11 Ar
     * - vol_instance 2 reçoit: 300 * 55.56% = 166.67 Ar
     * - vol_instance 3 reçoit: 400 * 55.56% = 222.22 Ar
     */
    private void creerDetailsPaiementProportionnel(PaiementPublicite paiement, Societe societe, BigDecimal montantPaye) {
        // Récupérer toutes les diffusions de cette société
        List<Publicite> diffusions = publiciteRepository.findBySocieteId(societe.getId());
        
        if (diffusions.isEmpty()) {
            return;
        }

        // Grouper les diffusions par vol instance et calculer les montants
        Map<Long, VolInstanceBilan> bilanParInstance = new HashMap<>();
        
        for (Publicite pub : diffusions) {
            if (pub.getVolInstance() == null) continue;
            
            Long viId = pub.getVolInstance().getId();
            VolInstanceBilan bilan = bilanParInstance.computeIfAbsent(viId, 
                k -> new VolInstanceBilan(pub.getVolInstance()));
            
            bilan.nombreDiffusions++;
            bilan.montantTotal = bilan.montantTotal.add(
                pub.getMontant() != null ? BigDecimal.valueOf(pub.getMontant()) : BigDecimal.ZERO
            );
        }

        // Calculer le montant déjà payé pour chaque vol instance
        for (VolInstanceBilan bilan : bilanParInstance.values()) {
            BigDecimal dejaPaye = detailPaiementPubliciteRepository
                .getTotalPayeBySocieteAndVolInstance(societe.getId(), bilan.volInstance.getId());
            bilan.montantDejaPaye = dejaPaye != null ? dejaPaye : BigDecimal.ZERO;
            bilan.resteAPayer = bilan.montantTotal.subtract(bilan.montantDejaPaye);
            // S'assurer que le reste n'est pas négatif
            if (bilan.resteAPayer.compareTo(BigDecimal.ZERO) < 0) {
                bilan.resteAPayer = BigDecimal.ZERO;
            }
        }

        // Calculer le total reste à payer pour toutes les instances
        BigDecimal totalResteAPayer = BigDecimal.ZERO;
        for (VolInstanceBilan bilan : bilanParInstance.values()) {
            totalResteAPayer = totalResteAPayer.add(bilan.resteAPayer);
        }

        // Si rien à payer, on ne crée pas de détails
        if (totalResteAPayer.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        // Calculer le pourcentage: montantPaye / totalResteAPayer
        BigDecimal pourcentage = montantPaye.divide(totalResteAPayer, 6, RoundingMode.HALF_UP);
        // Cap à 100%
        if (pourcentage.compareTo(BigDecimal.ONE) > 0) {
            pourcentage = BigDecimal.ONE;
        }

        // Créer les détails de paiement pour chaque vol instance avec reste > 0
        BigDecimal sommePaye = BigDecimal.ZERO;
        List<DetailPaiementPublicite> details = new ArrayList<>();
        
        for (VolInstanceBilan bilan : bilanParInstance.values()) {
            if (bilan.resteAPayer.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            // Calculer le montant à payer pour cette instance
            BigDecimal montantInstance = bilan.resteAPayer.multiply(pourcentage)
                .setScale(2, RoundingMode.HALF_UP);
            
            // S'assurer de ne pas dépasser le reste à payer
            if (montantInstance.compareTo(bilan.resteAPayer) > 0) {
                montantInstance = bilan.resteAPayer;
            }

            DetailPaiementPublicite detail = new DetailPaiementPublicite();
            detail.setPaiementPublicite(paiement);
            detail.setVolInstance(bilan.volInstance);
            detail.setSociete(societe);
            detail.setNombreDiffusions(bilan.nombreDiffusions);
            detail.setMontantTotalDiffusions(bilan.montantTotal);
            detail.setMontantDejaPaye(bilan.montantDejaPaye);
            detail.setMontantPaye(montantInstance);
            detail.setResteAPayer(bilan.resteAPayer.subtract(montantInstance));
            detail.setPourcentageApplique(pourcentage.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP));
            
            details.add(detail);
            sommePaye = sommePaye.add(montantInstance);
        }

        // Ajuster pour éviter les erreurs d'arrondi
        if (!details.isEmpty() && sommePaye.compareTo(montantPaye) != 0) {
            BigDecimal diff = montantPaye.subtract(sommePaye);
            // Ajouter/soustraire la différence au dernier détail
            DetailPaiementPublicite last = details.get(details.size() - 1);
            last.setMontantPaye(last.getMontantPaye().add(diff));
            last.setResteAPayer(last.getResteAPayer().subtract(diff));
        }

        // Sauvegarder tous les détails
        for (DetailPaiementPublicite detail : details) {
            detailPaiementPubliciteRepository.save(detail);
        }
    }

    /**
     * Classe interne pour le bilan par vol instance
     */
    private static class VolInstanceBilan {
        VolInstance volInstance;
        int nombreDiffusions = 0;
        BigDecimal montantTotal = BigDecimal.ZERO;
        BigDecimal montantDejaPaye = BigDecimal.ZERO;
        BigDecimal resteAPayer = BigDecimal.ZERO;

        VolInstanceBilan(VolInstance volInstance) {
            this.volInstance = volInstance;
        }
    }

    /**
     * Récupérer les détails de paiement pour un paiement donné
     */
    public List<DetailPaiementPublicite> getDetailsPaiement(Long paiementId) {
        return detailPaiementPubliciteRepository.findByPaiementIdOrderByDateDepart(paiementId);
    }

    /**
     * Récupérer le bilan détaillé par vol instance pour une société
     */
    public List<VolInstancePaiementBilan> getBilanParVolInstance(Long societeId) {
        List<Publicite> diffusions = publiciteRepository.findBySocieteId(societeId);
        
        Map<Long, VolInstancePaiementBilan> bilanMap = new HashMap<>();
        
        for (Publicite pub : diffusions) {
            if (pub.getVolInstance() == null) continue;
            
            Long viId = pub.getVolInstance().getId();
            VolInstancePaiementBilan bilan = bilanMap.computeIfAbsent(viId, 
                k -> new VolInstancePaiementBilan(pub.getVolInstance()));
            
            bilan.nombreDiffusions++;
            bilan.montantTotal = bilan.montantTotal.add(
                pub.getMontant() != null ? BigDecimal.valueOf(pub.getMontant()) : BigDecimal.ZERO
            );
        }

        // Calculer les paiements déjà effectués pour chaque instance
        for (VolInstancePaiementBilan bilan : bilanMap.values()) {
            BigDecimal paye = detailPaiementPubliciteRepository
                .getTotalPayeBySocieteAndVolInstance(societeId, bilan.volInstance.getId());
            bilan.montantPaye = paye != null ? paye : BigDecimal.ZERO;
            bilan.resteAPayer = bilan.montantTotal.subtract(bilan.montantPaye);
            if (bilan.resteAPayer.compareTo(BigDecimal.ZERO) < 0) {
                bilan.resteAPayer = BigDecimal.ZERO;
            }
        }

        return new ArrayList<>(bilanMap.values());
    }

    /**
     * Classe pour le bilan de paiement par vol instance
     */
    public static class VolInstancePaiementBilan {
        private final VolInstance volInstance;
        private int nombreDiffusions = 0;
        private BigDecimal montantTotal = BigDecimal.ZERO;
        private BigDecimal montantPaye = BigDecimal.ZERO;
        private BigDecimal resteAPayer = BigDecimal.ZERO;

        public VolInstancePaiementBilan(VolInstance volInstance) {
            this.volInstance = volInstance;
        }

        public VolInstance getVolInstance() { return volInstance; }
        public int getNombreDiffusions() { return nombreDiffusions; }
        public BigDecimal getMontantTotal() { return montantTotal; }
        public BigDecimal getMontantPaye() { return montantPaye; }
        public BigDecimal getResteAPayer() { return resteAPayer; }
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
