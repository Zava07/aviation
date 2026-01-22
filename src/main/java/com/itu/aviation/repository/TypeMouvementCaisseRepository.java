package com.itu.aviation.repository;

import com.itu.aviation.entity.TypeMouvementCaisse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeMouvementCaisseRepository extends JpaRepository<TypeMouvementCaisse, Long> {
    Optional<TypeMouvementCaisse> findByTypeIgnoreCase(String type);
}