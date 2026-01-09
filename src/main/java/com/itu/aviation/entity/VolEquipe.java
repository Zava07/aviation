package com.itu.aviation.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vol_equipe")
public class VolEquipe {

    @EmbeddedId
    private VolEquipeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idVolInstance")
    @JoinColumn(name = "id_vol_instance")
    private VolInstance volInstance;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idMembre")
    @JoinColumn(name = "id_membre")
    private MembreEquipe membre;

    @Column(name = "role", length = 50)
    private String role;

    public VolEquipe() {
    }

    public VolEquipeId getId() {
        return id;
    }

    public void setId(VolEquipeId id) {
        this.id = id;
    }

    public VolInstance getVolInstance() {
        return volInstance;
    }

    public void setVolInstance(VolInstance volInstance) {
        this.volInstance = volInstance;
    }

    public MembreEquipe getMembre() {
        return membre;
    }

    public void setMembre(MembreEquipe membre) {
        this.membre = membre;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}