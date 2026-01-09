package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "billet")
public class Billet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_billet")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reservation", nullable = false)
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_siege", nullable = false)
    private Siege siege;

    @Transient
    private java.math.BigDecimal prixFinal;

    @Column(name = "numero_billet", nullable = false, unique = true, length = 50)
    private String numeroBillet;

    @Column(name = "date_emission", nullable = true)
    private LocalDateTime dateEmission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_status_billet", nullable = false)
    private StatusBillet statusBillet;

    public Billet() {
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

    public Siege getSiege() {
        return siege;
    }

    public void setSiege(Siege siege) {
        this.siege = siege;
    }

    public java.math.BigDecimal getPrixFinal() {
        if (this.prixFinal != null) return this.prixFinal;
        if (this.reservation != null && this.reservation.getPrix() != null) return this.reservation.getPrix();
        return null;
    }

    public void setPrixFinal(java.math.BigDecimal prixFinal) {
        this.prixFinal = prixFinal;
    }

    public String getNumeroBillet() {
        return numeroBillet;
    }

    public void setNumeroBillet(String numeroBillet) {
        this.numeroBillet = numeroBillet;
    }

    public LocalDateTime getDateEmission() {
        return dateEmission;
    }

    public void setDateEmission(LocalDateTime dateEmission) {
        this.dateEmission = dateEmission;
    }

    public StatusBillet getStatusBillet() {
        return statusBillet;
    }

    public void setStatusBillet(StatusBillet statusBillet) {
        this.statusBillet = statusBillet;
    }
}