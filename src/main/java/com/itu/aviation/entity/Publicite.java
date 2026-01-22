package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "publicite")
public class Publicite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_publicite")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_societe", nullable = false)
    private Societe societe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_publicite_video")
    private PubliciteVideo publiciteVideo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vol_instance", nullable = false)
    private VolInstance volInstance;

    @Column(name = "montant", nullable = false)
    private Double montant;

    @Column(name = "date_publicite")
    private LocalDateTime datePublicite;

    public Publicite() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Societe getSociete() {
        return societe;
    }

    public void setSociete(Societe societe) {
        this.societe = societe;
    }

    public PubliciteVideo getPubliciteVideo() {
        return publiciteVideo;
    }

    public void setPubliciteVideo(PubliciteVideo publiciteVideo) {
        this.publiciteVideo = publiciteVideo;
    }

    public VolInstance getVolInstance() {
        return volInstance;
    }

    public void setVolInstance(VolInstance volInstance) {
        this.volInstance = volInstance;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public LocalDateTime getDatePublicite() {
        return datePublicite;
    }

    public void setDatePublicite(LocalDateTime datePublicite) {
        this.datePublicite = datePublicite;
    }
}