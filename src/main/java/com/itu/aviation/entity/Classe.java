package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "classe")
public class Classe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_classe")
    private Long id;

    @Column(name = "nom_classe", length = 50, nullable = false)
    private String nomClasse;

    @Column(name = "niveau")
    private Integer niveau;

    @OneToMany(mappedBy = "classe", fetch = FetchType.LAZY)
    private List<PrixVolClasse> prix = new ArrayList<>();

    @OneToMany(mappedBy = "classe", fetch = FetchType.LAZY)
    private List<Siege> sieges = new ArrayList<>();

    public Classe() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomClasse() {
        return nomClasse;
    }

    public void setNomClasse(String nomClasse) {
        this.nomClasse = nomClasse;
    }

    public Integer getNiveau() {
        return niveau;
    }

    public void setNiveau(Integer niveau) {
        this.niveau = niveau;
    }

    public List<PrixVolClasse> getPrix() {
        return prix;
    }

    public void setPrix(List<PrixVolClasse> prix) {
        this.prix = prix;
    }

    public List<Siege> getSieges() {
        return sieges;
    }

    public void setSieges(List<Siege> sieges) {
        this.sieges = sieges;
    }
}