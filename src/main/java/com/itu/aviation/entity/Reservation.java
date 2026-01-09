package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reservation")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_passager", nullable = false)
    private Passager passager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vol_instance", nullable = false)
    private VolInstance vol;

    @Column(name = "date_reservation", nullable = true)
    private LocalDateTime dateReservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_status_reservation", nullable = false)
    private StatutReservation statutReservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_classe")
    private Classe classe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_siege")
    private Siege siege;

    @Column(name = "prix", precision = 12, scale = 2)
    private java.math.BigDecimal prix;

    public Reservation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Passager getPassager() {
        return passager;
    }

    public void setPassager(Passager passager) {
        this.passager = passager;
    }

    public VolInstance getVol() {
        return vol;
    }

    public void setVol(VolInstance vol) {
        this.vol = vol;
    }

    public LocalDateTime getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDateTime dateReservation) {
        this.dateReservation = dateReservation;
    }

    public StatutReservation getStatutReservation() {
        return statutReservation;
    }

    public void setStatutReservation(StatutReservation statutReservation) {
        this.statutReservation = statutReservation;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public Siege getSiege() {
        return siege;
    }

    public void setSiege(Siege siege) {
        this.siege = siege;
    }

    public java.math.BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(java.math.BigDecimal prix) {
        this.prix = prix;
    }
}