package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class VolEquipeId implements Serializable {
    @Column(name = "id_vol_instance")
    private Long idVolInstance;

    @Column(name = "id_membre")
    private Long idMembre;

    public VolEquipeId() {
    }

    public VolEquipeId(Long idVolInstance, Long idMembre) {
        this.idVolInstance = idVolInstance;
        this.idMembre = idMembre;
    }

    public Long getIdVolInstance() {
        return idVolInstance;
    }

    public void setIdVolInstance(Long idVolInstance) {
        this.idVolInstance = idVolInstance;
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
        return Objects.equals(idVolInstance, that.idVolInstance) && Objects.equals(idMembre, that.idMembre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVolInstance, idMembre);
    }
}