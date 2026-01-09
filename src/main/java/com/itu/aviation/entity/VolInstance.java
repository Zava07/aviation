package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vol_instance")
public class VolInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vol_instance")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vol", nullable = false)
    private Vol vol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_avion", nullable = false)
    private Avion avion;

    @Column(name = "date_depart", nullable = false)
    private LocalDateTime dateDepart;

    @Column(name = "date_arrivee", nullable = false)
    private LocalDateTime dateArrivee;

    @Column(name = "duree_prevue")
    private Integer dureePrevue;

    @Column(name = "duree_reelle")
    private Integer dureeReelle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_status_vol", nullable = false)
    private StatusVol statusVol;

    @OneToMany(mappedBy = "volInstance", fetch = FetchType.LAZY)
    private List<SiegeVol> siegeVols = new ArrayList<>();

    @OneToMany(mappedBy = "volInstance", fetch = FetchType.LAZY)
    private List<VolEquipe> volEquipes = new ArrayList<>();

    public VolInstance() {
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

    public StatusVol getStatusVol() {
        return statusVol;
    }

    public void setStatusVol(StatusVol statusVol) {
        this.statusVol = statusVol;
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
