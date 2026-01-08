package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "type_bagage")
public class TypeBagage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_bagage")
    private Long id;

    @Column(name = "type", length = 50, nullable = false)
    private String type;

    @OneToMany(mappedBy = "typeBagage", fetch = FetchType.LAZY)
    private List<Bagage> bagages = new ArrayList<>();

    public TypeBagage() {
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

    public List<Bagage> getBagages() {
        return bagages;
    }

    public void setBagages(List<Bagage> bagages) {
        this.bagages = bagages;
    }
}