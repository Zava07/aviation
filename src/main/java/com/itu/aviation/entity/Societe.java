package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "societe")
public class Societe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_societe")
    private Long id;

    @Column(name = "nom_societe", length = 100, nullable = false)
    private String nomSociete;

    @OneToMany(mappedBy = "societe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Publicite> publicites = new ArrayList<>();

    @OneToMany(mappedBy = "societe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PubliciteVideo> videos = new ArrayList<>();

    public Societe() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomSociete() {
        return nomSociete;
    }

    public void setNomSociete(String nomSociete) {
        this.nomSociete = nomSociete;
    }

    public List<Publicite> getPublicites() {
        return publicites;
    }

    public void setPublicites(List<Publicite> publicites) {
        this.publicites = publicites;
    }

    public List<PubliciteVideo> getVideos() {
        return videos;
    }

    public void setVideos(List<PubliciteVideo> videos) {
        this.videos = videos;
    }
}