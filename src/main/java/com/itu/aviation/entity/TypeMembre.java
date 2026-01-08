package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "type_membre")
public class TypeMembre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_membre")
    private Long id;

    @Column(name = "type", length = 50, nullable = false)
    private String type;

    @OneToMany(mappedBy = "typeMembre", fetch = FetchType.LAZY)
    private List<MembreEquipe> membres = new ArrayList<>();

    public TypeMembre() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<MembreEquipe> getMembres() {
        return membres;
    }

    public void setMembres(List<MembreEquipe> membres) {
        this.membres = membres;
    }
}