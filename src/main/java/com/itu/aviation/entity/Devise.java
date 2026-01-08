package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "devise")
public class Devise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_devise")
    private Long id;

    @Column(name = "nom_devise", length = 20, nullable = false)
    private String nomDevise;

    @Column(name = "valeur_mga", precision = 12, scale = 2)
    private BigDecimal valeurMga;

    @OneToMany(mappedBy = "devise", fetch = FetchType.LAZY)
    private List<Paiement> paiements = new ArrayList<>();

    public Devise() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomDevise() {
        return nomDevise;
    }

    public void setNomDevise(String nomDevise) {
        this.nomDevise = nomDevise;
    }

    public BigDecimal getValeurMga() {
        return valeurMga;
    }

    public void setValeurMga(BigDecimal valeurMga) {
        this.valeurMga = valeurMga;
    }

    public List<Paiement> getPaiements() {
        return paiements;
    }

    public void setPaiements(List<Paiement> paiements) {
        this.paiements = paiements;
    }
}