package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "popularite_vol")
public class PopulariteVol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historique_point_vol")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vol", nullable = false)
    private Vol vol;

    @Column(name = "point", nullable = false)
    private Integer point;

    @Column(name = "date_point")
    private LocalDateTime datePoint;

    public PopulariteVol() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vol getVol() {
        return vol;
    }

    public void setVol(Vol vol) {
        this.vol = vol;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public LocalDateTime getDatePoint() {
        return datePoint;
    }

    public void setDatePoint(LocalDateTime datePoint) {
        this.datePoint = datePoint;
    }
}
