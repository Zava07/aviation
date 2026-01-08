package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "status_siege_vol")
public class StatusSiegeVol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_status_siege_vol")
    private Long id;

    @Column(name = "status", length = 50, nullable = false)
    private String status;

    @OneToMany(mappedBy = "statusSiegeVol", fetch = FetchType.LAZY)
    private List<SiegeVol> siegeVols = new ArrayList<>();

    public StatusSiegeVol() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SiegeVol> getSiegeVols() {
        return siegeVols;
    }

    public void setSiegeVols(List<SiegeVol> siegeVols) {
        this.siegeVols = siegeVols;
    }
}