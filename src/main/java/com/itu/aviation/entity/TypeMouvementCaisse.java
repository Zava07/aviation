package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "type_mouvement_caisse")
public class TypeMouvementCaisse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_mouvement_caisse")
    private Long id;

    @Column(name = "type", length = 50, nullable = false)
    private String type;

    @OneToMany(mappedBy = "typeMouvementCaisse", fetch = FetchType.LAZY)
    private List<MouvementCaisse> mouvements = new ArrayList<>();

    public TypeMouvementCaisse() {
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

    public List<MouvementCaisse> getMouvements() {
        return mouvements;
    }

    public void setMouvements(List<MouvementCaisse> mouvements) {
        this.mouvements = mouvements;
    }
}