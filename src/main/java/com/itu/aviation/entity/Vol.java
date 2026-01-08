package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vol")
public class Vol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vol")
    private Long id;

    @Column(name = "numero_vol", length = 20, nullable = false, unique = true)
    private String numeroVol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_avion", nullable = false)
    private Avion avion;

    @Column(name = "date_depart", nullable = false)
    private LocalDateTime dateDepart;

    @Column(name = "date_arrivee", nullable = false)
    private LocalDateTime dateArrivee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_status_vol", nullable = false)
    private StatusVol statusVol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aeroport_origine", nullable = false)
    private Aeroport aeroportOrigine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aeroport_destination", nullable = false)
    private Aeroport aeroportDestination;

    @Column(name = "duree_prevue")
    private Integer dureePrevue;

    @Column(name = "duree_reelle")
    private Integer dureeReelle;

    @OneToMany(mappedBy = "vol", fetch = FetchType.LAZY)
    private List<PrixVolClasse> prixParClasse = new ArrayList<>();

    @OneToMany(mappedBy = "vol", fetch = FetchType.LAZY)
    private List<SiegeVol> siegeVols = new ArrayList<>();

    @OneToMany(mappedBy = "vol", fetch = FetchType.LAZY)
    private List<VolEquipe> volEquipes = new ArrayList<>();

    public Vol() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroVol() {
        return numeroVol;
    }

    public void setNumeroVol(String numeroVol) {
        this.numeroVol = numeroVol;
    }

    public Avion getAvion() {
        return avion;
    }

    public void setAvion(Avion avion) {
        this.avion = avion;
    }

    public LocalDateTime getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(LocalDateTime dateDepart) {
        this.dateDepart = dateDepart;
    }

    public LocalDateTime getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(LocalDateTime dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public StatusVol getStatusVol() {
        return statusVol;
    }

    public void setStatusVol(StatusVol statusVol) {
        this.statusVol = statusVol;
    }

    public Aeroport getAeroportOrigine() {
        return aeroportOrigine;
    }

    public void setAeroportOrigine(Aeroport aeroportOrigine) {
        this.aeroportOrigine = aeroportOrigine;
    }

    public Aeroport getAeroportDestination() {
        return aeroportDestination;
    }

    public void setAeroportDestination(Aeroport aeroportDestination) {
        this.aeroportDestination = aeroportDestination;
    }

    public Integer getDureePrevue() {
        return dureePrevue;
    }

    public void setDureePrevue(Integer dureePrevue) {
        this.dureePrevue = dureePrevue;
    }

    public Integer getDureeReelle() {
        return dureeReelle;
    }

    public void setDureeReelle(Integer dureeReelle) {
        this.dureeReelle = dureeReelle;
    }

    public List<PrixVolClasse> getPrixParClasse() {
        return prixParClasse;
    }

    public void setPrixParClasse(List<PrixVolClasse> prixParClasse) {
        this.prixParClasse = prixParClasse;
    }

    public List<SiegeVol> getSiegeVols() {
        return siegeVols;
    }

    public void setSiegeVols(List<SiegeVol> siegeVols) {
        this.siegeVols = siegeVols;
    }

    public List<VolEquipe> getVolEquipes() {
        return volEquipes;
    }

    public void setVolEquipes(List<VolEquipe> volEquipes) {
        this.volEquipes = volEquipes;
    }
}