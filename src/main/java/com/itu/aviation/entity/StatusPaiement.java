package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "status_paiement")
public class StatusPaiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_status_paiement")
    private Long id;

    @Column(name = "status", length = 50, nullable = false)
    private String status;

    @OneToMany(mappedBy = "statusPaiement", fetch = FetchType.LAZY)
    private List<Paiement> paiements = new ArrayList<>();

    public StatusPaiement() {
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

    public List<Paiement> getPaiements() {
        return paiements;
    }

    public void setPaiements(List<Paiement> paiements) {
        this.paiements = paiements;
    }
}