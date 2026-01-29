package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vente_produit_extra")
public class VenteProduitExtra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vente_produit_extra")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_produit_extra", nullable = false)
    private ProduitExtra produitExtra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vol_instance", nullable = false)
    private VolInstance volInstance;

    @Column(name = "quantite", nullable = false)
    private Integer quantite;

    @Column(name = "prix_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal prixTotal;

    @Column(name = "date_achat")
    private LocalDateTime dateAchat;

    public VenteProduitExtra() {
    }

    @PrePersist
    public void prePersist() {
        if (this.dateAchat == null) {
            this.dateAchat = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProduitExtra getProduitExtra() {
        return produitExtra;
    }

    public void setProduitExtra(ProduitExtra produitExtra) {
        this.produitExtra = produitExtra;
    }

    public VolInstance getVolInstance() {
        return volInstance;
    }

    public void setVolInstance(VolInstance volInstance) {
        this.volInstance = volInstance;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(BigDecimal prixTotal) {
        this.prixTotal = prixTotal;
    }

    public LocalDateTime getDateAchat() {
        return dateAchat;
    }

    public void setDateAchat(LocalDateTime dateAchat) {
        this.dateAchat = dateAchat;
    }
}
