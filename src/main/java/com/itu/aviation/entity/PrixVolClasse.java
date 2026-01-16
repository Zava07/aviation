package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "prix_vol_classe")
public class PrixVolClasse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prix")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vol", nullable = false)
    private Vol vol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_classe", nullable = false)
    private Classe classe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categorie_passager")
    private com.itu.aviation.entity.CategoriePassager categoriePassager;

    @Column(name = "prix", precision = 12, scale = 2, nullable = false)
    private BigDecimal prix;

    public PrixVolClasse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vol getVol() {
        return vol;
    }

    public void setVol(Vol vol) {
        this.vol = vol;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public com.itu.aviation.entity.CategoriePassager getCategoriePassager() {
        return categoriePassager;
    }

    public void setCategoriePassager(com.itu.aviation.entity.CategoriePassager categoriePassager) {
        this.categoriePassager = categoriePassager;
    }
}