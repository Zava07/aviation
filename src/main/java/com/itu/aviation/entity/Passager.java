package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "passager")
public class Passager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_passager")
    private Long id;

    @Column(name = "nom", length = 50, nullable = false)
    private String nom;

    @Column(name = "prenom", length = 50, nullable = false)
    private String prenom;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_passager", nullable = false)
    private TypePassager typePassager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categorie_passager")
    private com.itu.aviation.entity.CategoriePassager categoriePassager;

    @Column(name = "fidelite")
    private Integer fidelite;

    @OneToMany(mappedBy = "passager", fetch = FetchType.LAZY)
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "passager", fetch = FetchType.LAZY)
    private List<Bagage> bagages = new ArrayList<>();

    public Passager() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public TypePassager getTypePassager() {
        return typePassager;
    }

    public void setTypePassager(TypePassager typePassager) {
        this.typePassager = typePassager;
    }

    public Integer getFidelite() {
        return fidelite;
    }

    public void setFidelite(Integer fidelite) {
        this.fidelite = fidelite;
    }

    public com.itu.aviation.entity.CategoriePassager getCategoriePassager() {
        return categoriePassager;
    }

    public void setCategoriePassager(com.itu.aviation.entity.CategoriePassager categoriePassager) {
        this.categoriePassager = categoriePassager;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Bagage> getBagages() {
        return bagages;
    }

    public void setBagages(List<Bagage> bagages) {
        this.bagages = bagages;
    }
        public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}