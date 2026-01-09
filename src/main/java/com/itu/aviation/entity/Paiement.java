package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "paiement")
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paiement")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reservation", nullable = false)
    private Reservation reservation;

    @Column(name = "montant", precision = 12, scale = 2, nullable = false)
    private BigDecimal montant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_devise", nullable = false)
    private Devise devise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_methode_paiement", nullable = false)
    private MethodePaiement methodePaiement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_status_paiement", nullable = false)
    private StatusPaiement statusPaiement;

    @Column(name = "date_paiement", nullable = true)
    private LocalDateTime datePaiement;

    public Paiement() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public Devise getDevise() {
        return devise;
    }

    public void setDevise(Devise devise) {
        this.devise = devise;
    }

    public MethodePaiement getMethodePaiement() {
        return methodePaiement;
    }

    public void setMethodePaiement(MethodePaiement methodePaiement) {
        this.methodePaiement = methodePaiement;
    }

    public StatusPaiement getStatusPaiement() {
        return statusPaiement;
    }

    public void setStatusPaiement(StatusPaiement statusPaiement) {
        this.statusPaiement = statusPaiement;
    }

    public LocalDateTime getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(LocalDateTime datePaiement) {
        this.datePaiement = datePaiement;
    }
}