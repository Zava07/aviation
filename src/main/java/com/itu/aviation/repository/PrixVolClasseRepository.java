package com.itu.aviation.repository;

import com.itu.aviation.entity.PrixVolClasse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrixVolClasseRepository extends JpaRepository<PrixVolClasse, Long> {
}
