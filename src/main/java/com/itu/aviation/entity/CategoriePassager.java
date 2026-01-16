package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categorie_passager")
public class CategoriePassager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categorie_passager")
    private Long id;

    @Column(name = "categorie", length = 50, nullable = false)
    private String categorie;

    @Column(name = "pourcentage_reduction", precision = 5, scale = 2, nullable = true)
    private BigDecimal pourcentageReduction;

    @OneToMany(mappedBy = "categoriePassager", fetch = FetchType.LAZY)
    private List<Passager> passagers = new ArrayList<>();

    public CategoriePassager() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public List<Passager> getPassagers() {
        return passagers;
    }

    public void setPassagers(List<Passager> passagers) {
        this.passagers = passagers;
    }

    public BigDecimal getPourcentageReduction() {
        return pourcentageReduction;
    }

    public void setPourcentageReduction(BigDecimal pourcentageReduction) {
        this.pourcentageReduction = pourcentageReduction;
    }
}
