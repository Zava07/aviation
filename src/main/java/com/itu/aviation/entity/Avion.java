package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "avion")
public class Avion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_avion")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_modele_avion", nullable = false)
    private ModeleAvion modeleAvion;

    @Column(name = "code_avion", length = 20, nullable = false, unique = true)
    private String codeAvion;

    @Column(name = "capacite_passagers", nullable = false)
    private Integer capacitePassagers;

    @Column(name = "capacite_cargo", nullable = false)
    private Double capaciteCargo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_etat_avion", nullable = false)
    private EtatAvion etatAvion;

    @Column(name = "point_vol")
    private Integer pointVol;

    @OneToMany(mappedBy = "avion", fetch = FetchType.LAZY)
    private List<Siege> sieges = new ArrayList<>();

    @OneToMany(mappedBy = "avion", fetch = FetchType.LAZY)
    private List<VolInstance> vols = new ArrayList<>();

    public Avion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ModeleAvion getModeleAvion() {
        return modeleAvion;
    }

    public void setModeleAvion(ModeleAvion modeleAvion) {
        this.modeleAvion = modeleAvion;
    }

    public String getCodeAvion() {
        return codeAvion;
    }

    public void setCodeAvion(String codeAvion) {
        this.codeAvion = codeAvion;
    }

    public Integer getCapacitePassagers() {
        return capacitePassagers;
    }

    public void setCapacitePassagers(Integer capacitePassagers) {
        this.capacitePassagers = capacitePassagers;
    }

    public Double getCapaciteCargo() {
        return capaciteCargo;
    }

    public void setCapaciteCargo(Double capaciteCargo) {
        this.capaciteCargo = capaciteCargo;
    }

    public EtatAvion getEtatAvion() {
        return etatAvion;
    }

    public void setEtatAvion(EtatAvion etatAvion) {
        this.etatAvion = etatAvion;
    }

    public Integer getPointVol() {
        return pointVol;
    }

    public void setPointVol(Integer pointVol) {
        this.pointVol = pointVol;
    }

    public List<Siege> getSieges() {
        return sieges;
    }

    public void setSieges(List<Siege> sieges) {
        this.sieges = sieges;
    }

    public List<VolInstance> getVols() {
        return vols;
    }

    public void setVols(List<VolInstance> vols) {
        this.vols = vols;
    }
}