package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SiegeVolId implements Serializable {

    @Column(name = "id_vol_instance")
    private Long idVolInstance;

    @Column(name = "id_siege")
    private Long idSiege;

    public SiegeVolId() {
    }


    public SiegeVolId(Long idVolInstance, Long idSiege) {
        this.idVolInstance = idVolInstance;
        this.idSiege = idSiege;
    }

    public Long getIdVolInstance() {
        return idVolInstance;
    }

    public void setIdVolInstance(Long idVolInstance) {
        this.idVolInstance = idVolInstance;
    }

    public Long getIdSiege() {
        return idSiege;
    }

    public void setIdSiege(Long idSiege) {
        this.idSiege = idSiege;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SiegeVolId that = (SiegeVolId) o;
        return Objects.equals(idVolInstance, that.idVolInstance) && Objects.equals(idSiege, that.idSiege);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVolInstance, idSiege);
    }
}