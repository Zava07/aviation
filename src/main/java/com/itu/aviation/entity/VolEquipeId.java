package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class VolEquipeId implements Serializable {
    @Column(name = "id_vol")
    private Long idVol;

    @Column(name = "id_membre")
    private Long idMembre;

    public VolEquipeId() {
    }

    public VolEquipeId(Long idVol, Long idMembre) {
        this.idVol = idVol;
        this.idMembre = idMembre;
    }

    public Long getIdVol() {
        return idVol;
    }

    public void setIdVol(Long idVol) {
        this.idVol = idVol;
    }

    public Long getIdMembre() {
        return idMembre;
    }

    public void setIdMembre(Long idMembre) {
        this.idMembre = idMembre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        VolEquipeId that = (VolEquipeId) o;
        return Objects.equals(idVol, that.idVol) && Objects.equals(idMembre, that.idMembre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVol, idMembre);
    }
}