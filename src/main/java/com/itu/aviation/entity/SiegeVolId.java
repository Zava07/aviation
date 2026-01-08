package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SiegeVolId implements Serializable {

    @Column(name = "id_vol")
    private Long idVol;

    @Column(name = "id_siege")
    private Long idSiege;

    public SiegeVolId() {
    }

    public SiegeVolId(Long idVol, Long idSiege) {
        this.idVol = idVol;
        this.idSiege = idSiege;
    }

    public Long getIdVol() {
        return idVol;
    }

    public void setIdVol(Long idVol) {
        this.idVol = idVol;
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
        return Objects.equals(idVol, that.idVol) && Objects.equals(idSiege, that.idSiege);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVol, idSiege);
    }
}