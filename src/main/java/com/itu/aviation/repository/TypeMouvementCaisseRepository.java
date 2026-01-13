package com.itu.aviation.repository;

import com.itu.aviation.entity.TypeMouvementCaisse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeMouvementCaisseRepository extends JpaRepository<TypeMouvementCaisse, Long> {
}