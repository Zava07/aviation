package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produit_extra")
public class ProduitExtra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produit_extra")
    private Long id;

    @Column(name = "nom_produit", nullable = false, length = 100)
    private String nomProduit;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "prix", nullable = false, precision = 10, scale = 2)
    private BigDecimal prix;

    @OneToMany(mappedBy = "produitExtra", fetch = FetchType.LAZY)
    private List<VenteProduitExtra> ventes = new ArrayList<>();

    public ProduitExtra() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public List<VenteProduitExtra> getVentes() {
        return ventes;
    }

    public void setVentes(List<VenteProduitExtra> ventes) {
        this.ventes = ventes;
    }
}
