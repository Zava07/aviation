package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "status_billet")
public class StatusBillet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_status_billet")
    private Long id;

    @Column(name = "status", length = 50, nullable = false)
    private String status;

    @OneToMany(mappedBy = "statusBillet", fetch = FetchType.LAZY)
    private List<Billet> billets = new ArrayList<>();

    public StatusBillet() {
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

    public List<Billet> getBillets() {
        return billets;
    }

    public void setBillets(List<Billet> billets) {
        this.billets = billets;
    }
}