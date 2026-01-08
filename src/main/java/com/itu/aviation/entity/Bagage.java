package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "bagage")
public class Bagage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bagage")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_passager", nullable = false)
    private Passager passager;

    @Column(name = "poids", precision = 6, scale = 2, nullable = false)
    private BigDecimal poids;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type", nullable = false)
    private TypeBagage typeBagage;

    public Bagage() {
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

    public BigDecimal getPoids() {
        return poids;
    }

    public void setPoids(BigDecimal poids) {
        this.poids = poids;
    }

    public TypeBagage getTypeBagage() {
        return typeBagage;
    }

    public void setTypeBagage(TypeBagage typeBagage) {
        this.typeBagage = typeBagage;
    }
}