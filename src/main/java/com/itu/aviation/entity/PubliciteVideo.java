package com.itu.aviation.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "publicite_video")
public class PubliciteVideo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_publicite_video")
    private Long id;

    @Column(name = "titre", length = 100, nullable = false)
    private String titre;

    @Column(name = "duree_seconde")
    private Integer dureeSeconde;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_societe", nullable = false)
    private Societe societe;

    public PubliciteVideo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Integer getDureeSeconde() {
        return dureeSeconde;
    }

    public void setDureeSeconde(Integer dureeSeconde) {
        this.dureeSeconde = dureeSeconde;
    }

    public Societe getSociete() {
        return societe;
    }

    public void setSociete(Societe societe) {
        this.societe = societe;
    }
}
