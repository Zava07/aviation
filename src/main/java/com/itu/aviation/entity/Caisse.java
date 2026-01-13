package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "caisse")
public class Caisse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_caisse")
    private Long id;

    @Column(name = "nom_caisse", length = 100, nullable = false)
    private String nomCaisse;
    
    

    @Column(name = "solde", precision = 15, scale = 2, nullable = false)
    private BigDecimal solde;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_methode_paiement", nullable = false)
    private MethodePaiement methodePaiement;

    @Column(name = "date_last_mouv")
    private LocalDateTime dateLastMouv;

    @OneToMany(mappedBy = "caisse", fetch = FetchType.LAZY)
    private List<MouvementCaisse> mouvements = new ArrayList<>();

    public Caisse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomCaisse() {
        return nomCaisse;
    }

    public void setNomCaisse(String nomCaisse) {
        this.nomCaisse = nomCaisse;
    }
    
    public BigDecimal getSolde() {
        return solde;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }

    public MethodePaiement getMethodePaiement() {
        return methodePaiement;
    }

    public void setMethodePaiement(MethodePaiement methodePaiement) {
        this.methodePaiement = methodePaiement;
    }

    public LocalDateTime getDateLastMouv() {
        return dateLastMouv;
    }

    public void setDateLastMouv(LocalDateTime dateLastMouv) {
        this.dateLastMouv = dateLastMouv;
    }

    public List<MouvementCaisse> getMouvements() {
        return mouvements;
    }

    public void setMouvements(List<MouvementCaisse> mouvements) {
        this.mouvements = mouvements;
    }
}