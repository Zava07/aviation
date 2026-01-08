package com.itu.aviation.repository;

import com.itu.aviation.entity.Vol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolRepository extends JpaRepository<Vol, Long> {
    Vol findByNumeroVol(String numeroVol);
}
