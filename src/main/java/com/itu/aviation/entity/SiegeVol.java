package com.itu.aviation.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "siege_vol")
public class SiegeVol {

    @EmbeddedId
    private SiegeVolId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idVolInstance")
    @JoinColumn(name = "id_vol_instance")
    private VolInstance volInstance;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idSiege")
    @JoinColumn(name = "id_siege")
    private Siege siege;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_status_siege_vol", nullable = false)
    private StatusSiegeVol statusSiegeVol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_passager")
    private Passager passager;

    public SiegeVol() {
    }


    public SiegeVolId getId() {
        return id;
    }

    public void setId(SiegeVolId id) {
        this.id = id;
    }

    public VolInstance getVolInstance() {
        return volInstance;
    }

    public void setVolInstance(VolInstance volInstance) {
        this.volInstance = volInstance;
    }

    public Siege getSiege() {
        return siege;
    }

    public void setSiege(Siege siege) {
        this.siege = siege;
    }

    public StatusSiegeVol getStatusSiegeVol() {
        return statusSiegeVol;
    }

    public void setStatusSiegeVol(StatusSiegeVol statusSiegeVol) {
        this.statusSiegeVol = statusSiegeVol;
    }

    public Passager getPassager() {
        return passager;
    }

    public void setPassager(Passager passager) {
        this.passager = passager;
    }
}