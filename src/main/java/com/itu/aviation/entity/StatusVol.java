package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "status_vol")
public class StatusVol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_status_vol")
    private Long id;

    @Column(name = "status", length = 50, nullable = false)
    private String status;

    @OneToMany(mappedBy = "statusVol", fetch = FetchType.LAZY)
    private List<Vol> vols = new ArrayList<>();

    public StatusVol() {
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

    public List<Vol> getVols() {
        return vols;
    }

    public void setVols(List<Vol> vols) {
        this.vols = vols;
    }
}