package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity pour stocker la répartition des paiements de publicité par vol instance.
 * Quand une société paie un montant partiel, il est distribué proportionnellement 
 * sur toutes les instances de vol concernées.
 */
@Entity
@Table(name = "detail_paiement_publicite")
public class DetailPaiementPublicite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detail_paiement")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paiement_publicite", nullable = false)
    private PaiementPublicite paiementPublicite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vol_instance", nullable = false)
    private VolInstance volInstance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_societe", nullable = false)
    private Societe societe;

    @Column(name = "nombre_diffusions", nullable = false)
    private Integer nombreDiffusions;

    @Column(name = "montant_total_diffusions", precision = 15, scale = 2, nullable = false)
    private BigDecimal montantTotalDiffusions;

    @Column(name = "montant_deja_paye", precision = 15, scale = 2)
    private BigDecimal montantDejaPaye;

    @Column(name = "montant_paye", precision = 15, scale = 2, nullable = false)
    private BigDecimal montantPaye;

    @Column(name = "reste_a_payer", precision = 15, scale = 2)
    private BigDecimal resteAPayer;

    @Column(name = "pourcentage_applique", precision = 10, scale = 4)
    private BigDecimal pourcentageApplique;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    public DetailPaiementPublicite() {
    }

    @PrePersist
    public void prePersist() {
        if (dateCreation == null) {
            dateCreation = LocalDateTime.now();
        }
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaiementPublicite getPaiementPublicite() {
        return paiementPublicite;
    }

    public void setPaiementPublicite(PaiementPublicite paiementPublicite) {
        this.paiementPublicite = paiementPublicite;
    }

    public VolInstance getVolInstance() {
        return volInstance;
    }

    public void setVolInstance(VolInstance volInstance) {
        this.volInstance = volInstance;
    }

    public Societe getSociete() {
        return societe;
    }

    public void setSociete(Societe societe) {
        this.societe = societe;
    }

    public Integer getNombreDiffusions() {
        return nombreDiffusions;
    }

    public void setNombreDiffusions(Integer nombreDiffusions) {
        this.nombreDiffusions = nombreDiffusions;
    }

    public BigDecimal getMontantTotalDiffusions() {
        return montantTotalDiffusions;
    }

    public void setMontantTotalDiffusions(BigDecimal montantTotalDiffusions) {
        this.montantTotalDiffusions = montantTotalDiffusions;
    }

    public BigDecimal getMontantDejaPaye() {
        return montantDejaPaye;
    }

    public void setMontantDejaPaye(BigDecimal montantDejaPaye) {
        this.montantDejaPaye = montantDejaPaye;
    }

    public BigDecimal getMontantPaye() {
        return montantPaye;
    }

    public void setMontantPaye(BigDecimal montantPaye) {
        this.montantPaye = montantPaye;
    }

    public BigDecimal getResteAPayer() {
        return resteAPayer;
    }

    public void setResteAPayer(BigDecimal resteAPayer) {
        this.resteAPayer = resteAPayer;
    }

    public BigDecimal getPourcentageApplique() {
        return pourcentageApplique;
    }

    public void setPourcentageApplique(BigDecimal pourcentageApplique) {
        this.pourcentageApplique = pourcentageApplique;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
}
